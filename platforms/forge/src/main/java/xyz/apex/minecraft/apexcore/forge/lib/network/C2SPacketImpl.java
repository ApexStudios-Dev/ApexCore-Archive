package xyz.apex.minecraft.apexcore.forge.lib.network;

import xyz.apex.minecraft.apexcore.common.lib.network.C2SPacket;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;

import java.util.function.Supplier;

final class C2SPacketImpl<T> extends PacketImpl<T> implements C2SPacket<T>
{
    private final Supplier<Supplier<Handler<T>>> handler;

    C2SPacketImpl(NetworkManagerImpl manager, String packetKey, NetworkManager.Encoder<T> encoder, NetworkManager.Decoder<T> decoder, Supplier<Supplier<Handler<T>>> handler)
    {
        super(manager, packetKey, encoder, decoder);

        this.handler = handler;
    }

    @Override
    public Handler<T> handler()
    {
        return handler.get().get();
    }

    @Override
    public void sendToServer(T packet)
    {
        manager.channel.sendToServer(new NetworkManagerImpl.DummyPacket<>(this, packet, true));
    }
}
