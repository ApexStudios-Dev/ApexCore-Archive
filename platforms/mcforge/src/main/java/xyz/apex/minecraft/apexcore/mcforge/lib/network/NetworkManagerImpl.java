package xyz.apex.minecraft.apexcore.mcforge.lib.network;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.network.C2SPacket;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.network.Packet;
import xyz.apex.minecraft.apexcore.common.lib.network.S2CPacket;

import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class NetworkManagerImpl implements NetworkManager
{
    private static final Map<String, NetworkManager> MAP = Maps.newConcurrentMap();
    private static final int VERSION = 2;

    private final String ownerId;
    private final Map<String, C2SPacketImpl<?>> client2ServerPackets = Maps.newHashMap();
    private final Map<String, S2CPacketImpl<?>> server2ClientPackets = Maps.newHashMap();
    public final SimpleChannel channel;

    private NetworkManagerImpl(String ownerId)
    {
        this.ownerId = ownerId;

        channel = ChannelBuilder
                .named(new ResourceLocation(ownerId, "network"))
                .networkProtocolVersion(VERSION)
                .acceptedVersions(Channel.VersionTest.exact(VERSION))
                .simpleChannel()

                .messageBuilder(DummyPacket.class)
                    .encoder(DummyPacket::encode)
                    .decoder(DummyPacket::new)
                    .consumerNetworkThread((SimpleChannel.MessageBuilder.ToBooleanBiFunction<DummyPacket, CustomPayloadEvent.Context>) DummyPacket::consume)
                .add();
    }

    @Override
    public String getOwnerId()
    {
        return ownerId;
    }

    @Override
    public <T> C2SPacket<T> registerServerBound(String packetKey, Encoder<T> encoder, Decoder<T> decoder, Supplier<Supplier<C2SPacket.Handler<T>>> handler)
    {
        var packet = new C2SPacketImpl<>(this, packetKey, encoder, decoder, handler);
        if(client2ServerPackets.put(packetKey, packet) != null)
            throw new IllegalStateException("Attempt to register C2S packet with duplicate id: '%s:%s'".formatted(ownerId, packetKey));
        ApexCore.LOGGER.debug("[{}] Registering C2S Network Packet: {}", ownerId, packetKey);
        return packet;
    }

    @Override
    public <T> S2CPacket<T> registerClientBound(String packetKey, Encoder<T> encoder, Decoder<T> decoder, Supplier<Supplier<S2CPacket.Handler<T>>> handler)
    {
        var packet = new S2CPacketImpl<>(this, packetKey, encoder, decoder, handler);
        if(server2ClientPackets.put(packetKey, packet) != null)
            throw new IllegalStateException("Attempt to register S2C packet with duplicate id: '%s:%s'".formatted(ownerId, packetKey));
        ApexCore.LOGGER.debug("[{}] Registering S2C Network Packet: {}", ownerId, packetKey);
        return packet;
    }

    public static NetworkManager getOrCreate(String ownerId)
    {
        return MAP.computeIfAbsent(ownerId, NetworkManagerImpl::new);
    }

    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    static final class DummyPacket<T>
    {
        private final boolean client2server;
        private final Packet<T> packet;
        private final T packetData;

        DummyPacket(Packet<T> packet, T packetData, boolean client2server)
        {
            this.client2server = client2server;
            this.packet = packet;
            this.packetData = packetData;
        }

        private DummyPacket(FriendlyByteBuf buffer)
        {
            client2server = buffer.readBoolean();
            var packetId = buffer.readResourceLocation();
            var networkManager = (NetworkManagerImpl) getOrCreate(buffer.readUtf());
            packet = (Packet<T>) (client2server ? networkManager.client2ServerPackets : networkManager.server2ClientPackets).get(packetId);
            packetData = packet.decoder().decode(buffer);
        }

        private void encode(FriendlyByteBuf buffer)
        {
            buffer.writeBoolean(client2server);
            buffer.writeResourceLocation(packet.packetId());
            buffer.writeUtf(packet.manager().getOwnerId());
            packet.encoder().encode(packetData, buffer);
        }

        private boolean consume(CustomPayloadEvent.Context context)
        {
            context.enqueueWork(() -> {
                var manager = packet.manager();

                if(client2server)
                {
                    var sender = Objects.requireNonNull(context.getSender());
                    ((C2SPacket<T>) packet).handler().handle(sender.server, manager, sender, packetData);
                }
                else
                {
                    PhysicalSide.CLIENT.runWhenOn(() -> () -> {
                        var client = Minecraft.getInstance();
                        ((S2CPacket<T>) packet).handler().handle(client, manager, packetData);
                    });
                }
            });

            return true;
        }
    }
}
