package dev.apexstudios.apexcore.common.network;

import com.google.common.collect.Streams;
import io.netty.buffer.Unpooled;
import net.covers1624.quack.util.LazyValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public sealed class Packet<T>
{
    private final String id;
    private final NetworkManager manager;
    private final BiConsumer<T, FriendlyByteBuf> encoder;
    private final Function<FriendlyByteBuf, T> decoder;

    protected Packet(NetworkManager manager, String id, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder)
    {
        this.id = id;
        this.manager = manager;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public final String id()
    {
        return id;
    }

    public final NetworkManager manager()
    {
        return manager;
    }

    public final FriendlyByteBuf encode(T packet)
    {
        var buffer = Packet.newBuffer();
        encoder.accept(packet, buffer);
        return buffer;
    }

    public final T decode(FriendlyByteBuf buffer)
    {
        return decoder.apply(buffer);
    }

    public static FriendlyByteBuf newBuffer()
    {
        return new FriendlyByteBuf(Unpooled.buffer());
    }

    public static FriendlyByteBuf readBuffer(FriendlyByteBuf buffer)
    {
        return new FriendlyByteBuf(Unpooled.wrappedBuffer(buffer.readByteArray(32600)));
    }

    public static void writeBuffer(FriendlyByteBuf buffer, FriendlyByteBuf data)
    {
        buffer.writeVarInt(data.readableBytes());
        buffer.writeBytes(data);
    }

    public static final class S2C<T> extends Packet<T>
    {
        private final Consumer<T> consumer;

        @ApiStatus.Internal
        public S2C(NetworkManager manager, String id, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<T> consumer)
        {
            super(manager, id, encoder, decoder);

            this.consumer = consumer;
        }

        public void consume(T packet)
        {
            consumer.accept(packet);
        }

        public void sendTo(T packet, MinecraftServer server, Predicate<ServerPlayer> filter)
        {
            sendTo(packet, server.getPlayerList().getPlayers().stream().filter(filter));
        }

        public void sendTo(T packet, MinecraftServer server)
        {
            sendTo(packet, server, $ -> true);
        }

        public void sendTo(T packet, ServerPlayer player)
        {
            manager().sendToClient(this, packet, player);
        }

        public void sendTo(T packet, Predicate<ServerPlayer> filter, ServerPlayer... players)
        {
            sendTo(packet, Stream.of(players).filter(Objects::nonNull).filter(filter));
        }

        public void sendTo(T packet, ServerPlayer... players)
        {
            sendTo(packet, $ -> true, players);
        }

        public void sendTo(T packet, Iterable<ServerPlayer> players, Predicate<ServerPlayer> filter)
        {
            sendTo(packet, Streams.stream(players).filter(filter));
        }

        public void sendTo(T packet, Iterable<ServerPlayer> players)
        {
            sendTo(packet, players, $ -> true);
        }

        public void sendTo(T packet, Stream<ServerPlayer> players)
        {
            // to only encode once, no need to encode if no players to send data to
            var encoded = new LazyValue<>(() -> encode(packet));
            players.forEach(player -> manager().sendToClient(this, encoded.get(), player));
        }

        public void sendTo(T packet, ServerLevel level, Predicate<ServerPlayer> filter)
        {
            sendTo(packet, level.players().stream().filter(filter));
        }

        public void sendTo(T packet, ServerLevel level)
        {
            sendTo(packet, level, $ -> true);
        }

        public void sendTo(T packet, LevelChunk chunk, Predicate<ServerPlayer> filter)
        {
            if(chunk.getLevel().getChunkSource() instanceof ServerChunkCache server)
                sendTo(packet, server.chunkMap.getPlayers(chunk.getPos(), false).stream().filter(filter));
        }

        public void sendTo(T packet, LevelChunk chunk)
        {
            sendTo(packet, chunk, $ -> true);
        }
    }

    public static final class C2S<T> extends Packet<T>
    {
        private final BiConsumer<ServerPlayer, T> consumer;

        @ApiStatus.Internal
        public C2S(NetworkManager manager, String id, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<ServerPlayer, T> consumer)
        {
            super(manager, id, encoder, decoder);

            this.consumer = consumer;
        }

        public void consume(ServerPlayer sender, T packet)
        {
            consumer.accept(sender, packet);
        }

        public void send(T packet)
        {
            manager().sendToServer(this, packet);
        }
    }
}
