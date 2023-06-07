package xyz.apex.minecraft.apexcore.fabric.lib.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import xyz.apex.minecraft.apexcore.common.lib.network.C2SPacket;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;

import java.util.function.Supplier;

final class C2SPacketImpl<T> extends PacketImpl<T> implements C2SPacket<T>
{
    private final Supplier<Supplier<Handler<T>>> handler;

    C2SPacketImpl(NetworkManager manager, String packetKey, NetworkManager.Encoder<T> encoder, NetworkManager.Decoder<T> decoder, Supplier<Supplier<Handler<T>>> handler)
    {
        super(manager, packetKey, encoder, decoder);

        this.handler = handler;

        ServerPlayNetworking.registerGlobalReceiver(packetId(), (server, sender, vanillaHandler, buffer, responseSender) -> handler().handle(server, manager(), sender, decoder().decode(buffer)));
    }

    @Override
    public Handler<T> handler()
    {
        return handler.get().get();
    }

    @Override
    public void sendToServer(T packet)
    {
        ClientPlayNetworking.send(packetId(), encoder().encode(packet));
    }
}
