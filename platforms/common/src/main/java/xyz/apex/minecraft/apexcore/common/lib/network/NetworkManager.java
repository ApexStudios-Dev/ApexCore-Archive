package xyz.apex.minecraft.apexcore.common.lib.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

import java.util.function.Supplier;

/**
 * Interface for all network managers.
 */
@ApiStatus.NonExtendable
public interface NetworkManager
{
    /**
     * Returns registered Server2Client packet.
     *
     * @param packetKey Registration key for this packet.
     * @param encoder   Encoder used to encode this packet.
     * @param decoder   Decoder used to decode this packet.
     * @param handler   Handler used to process this packet.
     * @param <T>       Type of data packet handles.
     * @return Registered Server2Client packet.
     */
    <T> C2SPacket<T> registerServerBound(String packetKey, Encoder<T> encoder, Decoder<T> decoder, Supplier<Supplier<C2SPacket.Handler<T>>> handler);

    /**
     * Returns registered Client2Server packet.
     *
     * @param packetKey Registration key for this packet.
     * @param encoder   Encoder used to encode this packet.
     * @param decoder   Decoder used to decode this packet.
     * @param handler   Handler used to process this packet.
     * @param <T>       Type of data packet handles.
     * @return Registered Client2Server packet.
     */
    <T> S2CPacket<T> registerClientBound(String packetKey, Encoder<T> encoder, Decoder<T> decoder, Supplier<Supplier<S2CPacket.Handler<T>>> handler);

    /**
     * @return Owner id for this network manager.
     */
    String getOwnerId();

    /**
     * Returns network manager instance for given owner id.
     *
     * @param ownerId Owner id to create network manager for.
     * @return Network manager for given owner id.
     */
    static NetworkManager create(String ownerId)
    {
        return ApexCore.INSTANCE.createNetworkManager(ownerId);
    }

    /**
     * Encoder used to encode given packet data.
     *
     * @param <T> Packet data to be encoded.
     */
    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Encoder<T>
    {
        /**
         * Encodes the given packet data into the given buffer.
         *
         * @param packet Packet data to be encoded.
         * @param buffer Buffer to encode data into.
         */
        void encode(T packet, FriendlyByteBuf buffer);

        /**
         * Creates a new buffer and encodes the given packet data into it.
         *
         * @param packet Packet data to be encoded.
         * @return Buffer with given packet data encoded.
         */
        default FriendlyByteBuf encode(T packet)
        {
            var buffer = new FriendlyByteBuf(Unpooled.buffer());
            encode(packet, buffer);
            buffer.readerIndex(0);
            return buffer;
        }
    }

    /**
     * Decoder used to decode given packet data.
     *
     * @param <T> Packet data to be decoded.
     */
    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Decoder<T>
    {
        /**
         * Decodes and returns packet data from the given buffer.
         *
         * @param buffer Buffer to decode packet data from.
         * @return Decoded packet data.
         */
        T decode(FriendlyByteBuf buffer);
    }
}
