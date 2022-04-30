package xyz.apex.forge.apexcore.lib.net;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.Validate;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.java.utility.nullness.NonnullPredicate;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NetworkManager
{
	public static final PacketDistributor<ServerPlayerEntity> ALL_EXCEPT = new PacketDistributor<>(NetworkManager::allExcept, NetworkDirection.PLAY_TO_CLIENT);

	private final SimpleChannel instance;
	private final BiMap<Class<? extends AbstractPacket>, Integer> packetIdMap = HashBiMap.create();
	private int packetIdCounter = 0;

	public NetworkManager(String modId, String channelName, String channelVersion)
	{
		this(modId, channelName, () -> channelVersion, channelVersion::equals, channelVersion::equals);
	}

	public NetworkManager(String modId, String channelName, NonnullSupplier<String> channelVersionSupplier, NonnullPredicate<String> clientAcceptedVersions, NonnullPredicate<String> serverAcceptedVersions)
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
		int packetId = packetIdCounter++;
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

	public void sendTo(AbstractPacket packet, ServerPlayerEntity player)
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

	private void encodePacket(PacketHolder packet, PacketBuffer buffer)
	{
		Class<? extends AbstractPacket> packetType = packet.packet.getClass();
		Validate.isTrue(packetIdMap.containsKey(packetType), "Attempt to encode unregistered PacketType: '%s'", packetType);
		int packetId = packetIdMap.get(packetType);
		buffer.writeInt(packetId);
		packet.packet.encode(buffer);
	}

	private PacketHolder decodePacket(PacketBuffer buffer)
	{
		int packetId = buffer.readInt();
		Class<? extends AbstractPacket> packetType = packetIdMap.inverse().get(packetId);
		AbstractPacket packet = null;

		try
		{
			packet = packetType.getConstructor(PacketBuffer.class).newInstance(buffer);
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
		NetworkEvent.Context ctx = ctxSupplier.get();
		packet.packet.process(this, ctx);
		ctx.setPacketHandled(true);
	}

	private static Consumer<IPacket<?>> allExcept(PacketDistributor<ServerPlayerEntity> packetDistributor, Supplier<ServerPlayerEntity> playerSupplier)
	{
		return packet -> {
			ServerPlayerEntity player = playerSupplier.get();

			for(ServerPlayerEntity otherPlayer : player.server.getPlayerList().getPlayers())
			{
				if(!player.getUUID().equals(otherPlayer.getUUID()))
					otherPlayer.connection.send(packet);
			}
		};
	}
}
