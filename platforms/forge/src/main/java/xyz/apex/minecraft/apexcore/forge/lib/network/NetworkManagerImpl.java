package xyz.apex.minecraft.apexcore.forge.lib.network;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
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
    private static final String VERSION = "1";

    private final String ownerId;
    private final Map<String, C2SPacketImpl<?>> client2ServerPackets = Maps.newHashMap();
    private final Map<String, S2CPacketImpl<?>> server2ClientPackets = Maps.newHashMap();
    public final SimpleChannel channel;

    private NetworkManagerImpl(String ownerId)
    {
        this.ownerId = ownerId;

        channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(ownerId, "network"), () -> VERSION, VERSION::equals, VERSION::equals);
        channel.registerMessage(0, DummyPacket.class, DummyPacket::encode, DummyPacket::new, DummyPacket::consume);
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

        private static <T> void encode(DummyPacket<T> packet, FriendlyByteBuf buffer)
        {
            buffer.writeBoolean(packet.client2server);
            buffer.writeResourceLocation(packet.packet.packetId());
            buffer.writeUtf(packet.packet.manager().getOwnerId());
            packet.packet.encoder().encode(packet.packetData, buffer);
        }

        private static <T> void consume(DummyPacket<T> packet, Supplier<NetworkEvent.Context> context)
        {
            var ctx = context.get();

            ctx.enqueueWork(() -> {
                var manager = packet.packet.manager();

                if(packet.client2server)
                {
                    var sender = Objects.requireNonNull(ctx.getSender());
                    ((C2SPacket<T>) packet.packet).handler().handle(sender.server, manager, sender, packet.packetData);
                }
                else
                {
                    PhysicalSide.CLIENT.runWhenOn(() -> () -> {
                        var client = Minecraft.getInstance();
                        ((S2CPacket<T>) packet.packet).handler().handle(client, manager, packet.packetData);
                    });
                }
            });

            ctx.setPacketHandled(true);
        }
    }
}
