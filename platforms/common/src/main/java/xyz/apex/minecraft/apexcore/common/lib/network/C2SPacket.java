package xyz.apex.minecraft.apexcore.common.lib.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

/**
 * Interface for all ServerBound packets.
 *
 * @param <T> Type of data packet encodes and decodes.
 */
@ApiStatus.NonExtendable
public interface C2SPacket<T> extends Packet<T>
{
    /**
     * @return Handler used to process this packet.
     */
    Handler<T> handler();

    /**
     * Sends the given packet data to the server.
     *
     * @param packet Packet data to be sent.
     */
    void sendToServer(T packet);

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
         * @param server  Minecraft server instance.
         * @param manager Network manager packet is registered to.
         * @param sender  Packet sender.
         * @param packet  Decoded packet data.
         */
        void handle(MinecraftServer server, NetworkManager manager, ServerPlayer sender, T packet);
    }
}
