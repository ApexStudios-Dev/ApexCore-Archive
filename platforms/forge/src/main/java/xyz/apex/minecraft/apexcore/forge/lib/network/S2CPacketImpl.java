package xyz.apex.minecraft.apexcore.forge.lib.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.network.S2CPacket;

import java.util.function.Supplier;

final class S2CPacketImpl<T> extends PacketImpl<T> implements S2CPacket<T>
{
    private final Supplier<Supplier<Handler<T>>> handler;

    S2CPacketImpl(NetworkManagerImpl manager, String packetKey, NetworkManager.Encoder<T> encoder, NetworkManager.Decoder<T> decoder, Supplier<Supplier<Handler<T>>> handler)
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
    public void sendToClient(ServerPlayer player, T packet)
    {
        manager.channel.send(PacketDistributor.PLAYER.with(() -> player), new NetworkManagerImpl.DummyPacket<>(this, packet, false));
    }
}
