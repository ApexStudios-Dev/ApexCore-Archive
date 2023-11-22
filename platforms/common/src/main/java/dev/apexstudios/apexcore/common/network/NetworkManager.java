package dev.apexstudios.apexcore.common.network;

import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface NetworkManager
{
    int VERSION = 1;

    String ownerId();

    <T> Packet.S2C<T> registerS2C(String packetName, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<T> handler);

    <T> Packet.C2S<T> registerC2S(String packetName, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<ServerPlayer, T> handler);

    void sendToClient(Packet<?> packet, FriendlyByteBuf buffer, ServerPlayer player);

    default <T> void sendToClient(Packet<T> packet, T data, ServerPlayer player)
    {
        sendToClient(packet, packet.encode(data), player);
    }

    void sendToServer(Packet<?> packet, FriendlyByteBuf buffer);

    default <T> void sendToServer(Packet<T> packet, T data)
    {
        sendToServer(packet, packet.encode(data));
    }

    static NetworkManager get(String ownerId)
    {
        return PlatformFactory.get().networkManager(ownerId);
    }
}
