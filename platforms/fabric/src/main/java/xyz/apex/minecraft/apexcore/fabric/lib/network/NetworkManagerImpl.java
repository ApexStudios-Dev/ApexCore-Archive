package xyz.apex.minecraft.apexcore.fabric.lib.network;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.network.C2SPacket;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.network.S2CPacket;

import java.util.Map;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class NetworkManagerImpl implements NetworkManager
{
    private static final Map<String, NetworkManager> MAP = Maps.newConcurrentMap();

    private final String ownerId;
    private final Map<String, C2SPacketImpl<?>> client2ServerPackets = Maps.newHashMap();
    private final Map<String, S2CPacketImpl<?>> server2ClientPackets = Maps.newHashMap();

    private NetworkManagerImpl(String ownerId)
    {
        this.ownerId = ownerId;
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
}
