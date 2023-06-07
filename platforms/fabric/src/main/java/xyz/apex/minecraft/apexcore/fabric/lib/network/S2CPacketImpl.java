package xyz.apex.minecraft.apexcore.fabric.lib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.network.S2CPacket;

import java.util.function.Supplier;

final class S2CPacketImpl<T> extends PacketImpl<T> implements S2CPacket<T>
{
    private final Supplier<Supplier<Handler<T>>> handler;

    S2CPacketImpl(NetworkManager manager, String packetKey, NetworkManager.Encoder<T> encoder, NetworkManager.Decoder<T> decoder, Supplier<Supplier<Handler<T>>> handler)
    {
        super(manager, packetKey, encoder, decoder);

        this.handler = handler;

        PhysicalSide.CLIENT.runWhenOn(() -> () -> ClientPlayNetworking.registerGlobalReceiver(packetId(), (client, vanillaHandler, buffer, responseSender) -> handler().handle(client, manager(), decoder().decode(buffer))));
    }

    @Override
    public Handler<T> handler()
    {
        return handler.get().get();
    }

    @Override
    public void sendToClient(ServerPlayer player, T packet)
    {
        ServerPlayNetworking.send(player, packetId(), encoder().encode(packet));
    }
}
