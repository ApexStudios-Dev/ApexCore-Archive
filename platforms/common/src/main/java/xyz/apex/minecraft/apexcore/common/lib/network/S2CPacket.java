package xyz.apex.minecraft.apexcore.common.lib.network;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

/**
 * Interface for all ClientBound packets.
 *
 * @param <T> Type of data packet encodes and decodes.
 */
@ApiStatus.NonExtendable
public interface S2CPacket<T> extends Packet<T>
{
    /**
     * @return Handler used to process this packet.
     */
    Handler<T> handler();

    /**
     * Sends the given packet data to the given client.
     *
     * @param player Client to send the packet to.
     * @param packet Packet data to be sent.
     */
    void sendToClient(ServerPlayer player, T packet);

    /**
     * Handler used to process the decoded packet.
     *
     * @param <T> Type of packet data.
     */
    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Handler<T>
    {
        /**
         * Process the decoded packet data.
         *
         * @param client  Minecraft client instance.
         * @param manager Network manager packet is registered to.
         * @param packet  Decoded packet data.
         */
        void handle(Minecraft client, NetworkManager manager, T packet);
    }
}
