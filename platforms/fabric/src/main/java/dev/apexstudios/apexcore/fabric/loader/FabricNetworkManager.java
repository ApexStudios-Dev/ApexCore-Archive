package dev.apexstudios.apexcore.fabric.loader;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import dev.apexstudios.apexcore.common.network.Packet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

final class FabricNetworkManager implements NetworkManager
{
    private static final Map<String, NetworkManager> MAP = Maps.newHashMap();

    private final String ownerId;
    private final Map<String, Packet<?>> packets = Maps.newHashMap();
    private final ResourceLocation networkId;

    private FabricNetworkManager(String ownerId)
    {
        this.ownerId = ownerId;
        networkId = new ResourceLocation(ownerId, "%s_internal/network".formatted(ApexCore.ID));

        PhysicalSide.CLIENT.runWhenOn(() -> () -> ClientPlayNetworking.registerGlobalReceiver(networkId, (client, handler, buffer, responseSender) -> {
            var id = buffer.readUtf();
            var packet = Objects.requireNonNull(packets.get(id), () -> "Failed to decode packet! Unknown packet: %s".formatted(id));
            var data =  packet.decode(Packet.readBuffer(buffer));

            ((Packet.S2C) packet).consume(data);
        }));

        ServerPlayNetworking.registerGlobalReceiver(networkId, (server, player, handler, buffer, responseSender) -> {
            var id = buffer.readUtf();
            var packet = Objects.requireNonNull(packets.get(id), () -> "Failed to decode packet! Unknown packet: %s".formatted(id));
            var data =  packet.decode(Packet.readBuffer(buffer));

            ((Packet.C2S) packet).consume(player, data);
        });
    }

    @Override
    public String ownerId()
    {
        return ownerId;
    }

    @Override
    public <T> Packet.S2C<T> registerS2C(String packetName, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<T> handler)
    {
        return register(packetName, new Packet.S2C<>(this, packetName, encoder, decoder, handler));
    }

    @Override
    public <T> Packet.C2S<T> registerC2S(String packetName, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<ServerPlayer, T> handler)
    {
        return register(packetName, new Packet.C2S<>(this, packetName, encoder, decoder, handler));
    }

    @Override
    public void sendToClient(Packet<?> packet, FriendlyByteBuf buffer, ServerPlayer player)
    {
        ServerPlayNetworking.send(player, networkId, encode(packet, buffer));
    }

    @Override
    public void sendToServer(Packet<?> packet, FriendlyByteBuf buffer)
    {
        ClientPlayNetworking.send(networkId, encode(packet, buffer));
    }

    private <T, P extends Packet<T>> P register(String packetName, P packet)
    {
        if(packets.put(packetName, packet) != null)
            throw new IllegalStateException("Duplicate Packet registration: %s".formatted(packet));

        return packet;
    }

    private FriendlyByteBuf encode(Packet<?> packet, FriendlyByteBuf data)
    {
        data.readerIndex(0);

        var buffer = Packet.newBuffer();
        buffer.writeUtf(packet.id());
        Packet.writeBuffer(buffer, data);
        return buffer;
    }

    static NetworkManager get(String ownerId)
    {
        return MAP.computeIfAbsent(ownerId, FabricNetworkManager::new);
    }
}
