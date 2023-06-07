package xyz.apex.minecraft.apexcore.common.lib.network;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

/**
 * Base interface for all packets.
 *
 * @param <T> Type of data packet encodes and decodes.
 */
@ApiStatus.NonExtendable
public interface Packet<T>
{
    /**
     * @return Network manager this packet is registered to.
     */
    NetworkManager manager();

    /**
     * @return Internal packet id.
     */
    ResourceLocation packetId();

    /**
     * @return Encoder used to encode this packet.
     */
    NetworkManager.Encoder<T> encoder();

    /**
     * @return Decoder used to decode this packet.
     */
    NetworkManager.Decoder<T> decoder();
}
