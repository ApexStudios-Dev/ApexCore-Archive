package xyz.apex.minecraft.apexcore.mcforge.lib.network;

import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.network.Packet;

abstract class PacketImpl<T> implements Packet<T>
{
    protected final NetworkManagerImpl manager;
    private final ResourceLocation packetId;
    private final NetworkManager.Encoder<T> encoder;
    private final NetworkManager.Decoder<T> decoder;

    protected PacketImpl(NetworkManagerImpl manager, String packetKey, NetworkManager.Encoder<T> encoder, NetworkManager.Decoder<T> decoder)
    {
        this.manager = manager;
        this.encoder = encoder;
        this.decoder = decoder;

        packetId = new ResourceLocation(manager.getOwnerId(), packetKey);
    }

    @Override
    public final NetworkManager manager()
    {
        return manager;
    }

    @Override
    public final ResourceLocation packetId()
    {
        return packetId;
    }

    @Override
    public final NetworkManager.Encoder<T> encoder()
    {
        return encoder;
    }

    @Override
    public final NetworkManager.Decoder<T> decoder()
    {
        return decoder;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof Packet<?> other))
            return false;
        return packetId.equals(other.packetId());
    }

    @Override
    public int hashCode()
    {
        return packetId.hashCode();
    }

    @Override
    public String toString()
    {
        return "Packet(%s)".formatted(packetId);
    }
}
