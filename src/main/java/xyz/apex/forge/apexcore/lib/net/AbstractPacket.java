package xyz.apex.forge.apexcore.lib.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

public abstract class AbstractPacket
{
	/**
	 * The default constructor for packets.<br>
	 *<br>
	 * This is used on by the implementor to create the initial<br>
	 * instance for the packet, you can add as many parameters here as need be.<br>
	 * <br>
	 * The ({@link #AbstractPacket(FriendlyByteBuf) decoder}) constructor below should always exist<br>
	 * and be publicly visible from your packet class<br>
	 */
	public AbstractPacket()
	{
	}

	/**
	 * The decoder constructor for packets.<br>
	 * <br>
	 * This is used to construct the packet instance on the receiving end,<br>
	 * the {@link FriendlyByteBuf buffer} is passed in so that any fields created in the<br>
	 * {@link #AbstractPacket() default} constructor can also be marked as final while also<br>
	 * decoding them at the same time.<br>
	 * <br>
	 * @param buffer The buffer to decode packet data from.<br>
	 */
	public AbstractPacket(FriendlyByteBuf buffer)
	{
	}

	/**
	 * Encoder methods for packets.<br>
	 * <br>
	 * Use this method to encode any data created during the {@link #AbstractPacket() default}<br>
	 * constructor.<br>
	 * <br>
	 * @param buffer The buffer to encode packet data into.<br>
	 */
	protected abstract void encode(FriendlyByteBuf buffer);

	/**
	 * The packet consumer.<br>
	 * <br>
	 * Use this method to process the data after it has been successfully<br>
	 * encoded, sent, received &amp; decoded.<br>
	 * <br>
	 * IMPORTANT: No need to call {@link NetworkEvent.Context#setPacketHandled(boolean) setPacketHandled()} here,<br>
	 * this is done for you inside of the {@link NetworkManager} after this method is done processing.<br>
	 * <br>
	 * @param ctx The network context to be used while processing this packet.<br>
	 * @param network The network manager used to send this packet, use this to also reply if necessary.<br>
	 */
	protected abstract void process(NetworkManager network, NetworkEvent.Context ctx);
}
