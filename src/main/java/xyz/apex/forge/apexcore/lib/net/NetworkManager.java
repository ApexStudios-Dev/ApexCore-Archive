package xyz.apex.forge.apexcore.lib.net;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.Validate;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import xyz.apex.forge.apexcore.core.ApexCore;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class NetworkManager
{
	public static final PacketDistributor<ServerPlayer> ALL_EXCEPT = new PacketDistributor<>(NetworkManager::allExcept, NetworkDirection.PLAY_TO_CLIENT);

	private final SimpleChannel instance;
	private final BiMap<Class<? extends AbstractPacket>, Integer> packetIdMap = HashBiMap.create();
	private int packetIdCounter = 0;

	public NetworkManager(String modId, String channelName, String channelVersion)
	{
		this(modId, channelName, () -> channelVersion, channelVersion::equals, channelVersion::equals);
	}

	public NetworkManager(String modId, String channelName, Supplier<String> channelVersionSupplier, Predicate<String> clientAcceptedVersions, Predicate<String> serverAcceptedVersions)
	{
		instance = NetworkRegistry.ChannelBuilder
				.named(new ResourceLocation(modId, channelName))
				.clientAcceptedVersions(clientAcceptedVersions)
				.serverAcceptedVersions(serverAcceptedVersions)
				.networkProtocolVersion(channelVersionSupplier)
		.simpleChannel();

		instance.registerMessage(0, PacketHolder.class, this::encodePacket, this::decodePacket, this::consumePacket);
	}

	public void registerPacket(Class<? extends AbstractPacket> packetType)
	{
		var packetId = packetIdCounter++;
		packetIdMap.put(packetType, packetId);
	}

	public void registerPackets(Class<? extends AbstractPacket>... packetTypes)
	{
		Arrays.stream(packetTypes).forEach(this::registerPacket);
	}

	public void sendToServer(AbstractPacket packet)
	{
		instance.sendToServer(new PacketHolder(packet));
	}

	public void sendTo(AbstractPacket packet, ServerPlayer player)
	{
		instance.sendTo(new PacketHolder(packet), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	public void sendTo(AbstractPacket packet, PacketDistributor.PacketTarget packetTarget)
	{
		instance.send(packetTarget, new PacketHolder(packet));
	}

	public void reply(AbstractPacket packet, NetworkEvent.Context ctx)
	{
		instance.reply(new PacketHolder(packet), ctx);
	}

	private void encodePacket(PacketHolder packet, FriendlyByteBuf buffer)
	{
		var packetType = packet.packet.getClass();
		Validate.isTrue(packetIdMap.containsKey(packetType), "Attempt to encode unregistered PacketType: '%s'", packetType);
		var packetId = packetIdMap.get(packetType);
		buffer.writeInt(packetId);
		packet.packet.encode(buffer);
	}

	private PacketHolder decodePacket(FriendlyByteBuf buffer)
	{
		var packetId = buffer.readInt();
		var packetType = packetIdMap.inverse().get(packetId);
		AbstractPacket packet = null;

		try
		{
			packet = packetType.getConstructor(FriendlyByteBuf.class).newInstance(buffer);
		}
		catch(InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e)
		{
			ApexCore.LOGGER.error("Failed to decode PacketType: '{}'", packetType, e);
		}

		Validate.notNull(packet);
		return new PacketHolder(packet);
	}

	private void consumePacket(PacketHolder packet, Supplier<NetworkEvent.Context> ctxSupplier)
	{
		var ctx = ctxSupplier.get();
		packet.packet.process(this, ctx);
		ctx.setPacketHandled(true);
	}

	private static Consumer<Packet<?>> allExcept(PacketDistributor<ServerPlayer> packetDistributor, Supplier<ServerPlayer> playerSupplier)
	{
		return packet -> {
			var player = playerSupplier.get();

			for(var otherPlayer : player.server.getPlayerList().getPlayers())
			{
				if(!player.getUUID().equals(otherPlayer.getUUID()))
					otherPlayer.connection.send(packet);
			}
		};
	}
}
