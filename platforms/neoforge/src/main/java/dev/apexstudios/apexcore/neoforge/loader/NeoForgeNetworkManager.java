package dev.apexstudios.apexcore.neoforge.loader;

import com.google.common.collect.Maps;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import dev.apexstudios.apexcore.common.network.Packet;
import net.covers1624.quack.util.LazyValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.simple.SimpleChannel;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

final class NeoForgeNetworkManager implements NetworkManager
{
    private static final String VERSION_STR = String.valueOf(VERSION);

    private static final Map<String, NetworkManager> MAP = Maps.newHashMap();

    private final String ownerId;
    private final SimpleChannel channel;
    private final Map<String, Packet<?>> packets = Maps.newHashMap();

    private NeoForgeNetworkManager(String ownerId)
    {
        this.ownerId = ownerId;

        channel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ownerId, "%s_internal/network".formatted(ApexCore.ID)))
                .networkProtocolVersion(() -> VERSION_STR)
                .serverAcceptedVersions(VERSION_STR::equals)
                .clientAcceptedVersions(VERSION_STR::equals)
                .simpleChannel();


        channel.messageBuilder(Data.class, 0)
               .encoder(Data::encode)
                .decoder(Data::new)
                .consumerMainThread((data, context) -> data.consume(this, context))
               .noResponse()
       .add();
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
        channel.send(PacketDistributor.PLAYER.with(() -> player), new Data(packet.id(), buffer));
    }

    @Override
    public void sendToServer(Packet<?> packet, FriendlyByteBuf buffer)
    {
        channel.sendToServer(new Data(packet.id(), buffer));
    }

    private <T, P extends Packet<T>> P register(String packetName, P packet)
    {
        if(packets.put(packetName, packet) != null)
            throw new IllegalStateException("Duplicate Packet registration: %s".formatted(packet));

        return packet;
    }

    static NetworkManager get(String ownerId)
    {
        return MAP.computeIfAbsent(ownerId, NeoForgeNetworkManager::new);
    }

    record Data(String id, FriendlyByteBuf data)
    {
        Data(FriendlyByteBuf buffer)
        {
            this(buffer.readUtf(), Packet.readBuffer(buffer));
        }

        void encode(FriendlyByteBuf buffer)
        {
            data.readerIndex(0);

            buffer.writeUtf(id);
            Packet.writeBuffer(buffer, data);
        }

        void consume(NeoForgeNetworkManager manager, NetworkEvent.Context context)
        {
            var packet = Objects.requireNonNull(manager.packets.get(id), () -> "Failed to decode packet! Unknown packet: %s".formatted(id));
            var decoded = new LazyValue<>(() -> packet.decode(data));

            if(packet instanceof Packet.C2S client)
                client.consume(context.getSender(), decoded.get());
            if(packet instanceof Packet.S2C server)
                server.consume(decoded.get());

            context.setPacketHandled(true);
        }
    }
}
