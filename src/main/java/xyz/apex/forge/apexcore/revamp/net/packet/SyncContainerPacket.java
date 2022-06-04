package xyz.apex.forge.apexcore.revamp.net.packet;

import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.lib.net.AbstractPacket;
import xyz.apex.forge.apexcore.lib.net.NetworkManager;
import xyz.apex.forge.apexcore.revamp.block.entity.InventoryBlockEntity;

public final class SyncContainerPacket extends AbstractPacket
{
	private final BlockPos pos;
	private final FriendlyByteBuf buffer;

	private SyncContainerPacket(BlockPos pos, FriendlyByteBuf buffer)
	{
		super();

		this.pos = pos;
		this.buffer = buffer;
	}

	public SyncContainerPacket(FriendlyByteBuf buffer)
	{
		super(buffer);

		pos = buffer.readBlockPos();
		this.buffer = buffer;
	}

	@Override
	protected void encode(FriendlyByteBuf buffer)
	{
		buffer.writeBlockPos(pos);
		buffer.writeBytes(this.buffer);
	}

	@Override
	protected void process(NetworkManager network, NetworkEvent.Context ctx)
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::processClient);
	}

	@OnlyIn(Dist.CLIENT)
	private void processClient()
	{
		var mc = Minecraft.getInstance();

		if(mc.level == null)
			return;

		var tileEntity = mc.level.getBlockEntity(pos);

		if(tileEntity instanceof InventoryBlockEntity inventory)
			inventory.readContainerSyncData(buffer);
	}

	public static void sendToClient(InventoryBlockEntity blockEntity)
	{
		var level = blockEntity.getLevel();

		if(level == null || level.isClientSide)
			return;

		var buffer = new FriendlyByteBuf(Unpooled.buffer());
		blockEntity.writeContainerSyncData(buffer);
		var blockPos = blockEntity.getBlockPos();
		ApexCore.NETWORK.sendTo(new SyncContainerPacket(blockPos, buffer), PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 192D, level.dimension())));
	}
}
