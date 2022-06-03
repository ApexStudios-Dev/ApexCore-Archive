package xyz.apex.forge.apexcore.revamp.net.packet;

import io.netty.buffer.Unpooled;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.lib.net.AbstractPacket;
import xyz.apex.forge.apexcore.lib.net.NetworkManager;
import xyz.apex.forge.apexcore.revamp.block.entity.InventoryBlockEntity;

public final class SyncContainerPacket extends AbstractPacket
{
	private final BlockPos pos;
	private final PacketBuffer buffer;

	private SyncContainerPacket(BlockPos pos, PacketBuffer buffer)
	{
		super();

		this.pos = pos;
		this.buffer = buffer;
	}

	public SyncContainerPacket(PacketBuffer buffer)
	{
		super(buffer);

		pos = buffer.readBlockPos();
		this.buffer = buffer;
	}

	@Override
	protected void encode(PacketBuffer buffer)
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
		Minecraft mc = Minecraft.getInstance();

		if(mc.level == null)
			return;

		TileEntity tileEntity = mc.level.getBlockEntity(pos);

		if(tileEntity instanceof InventoryBlockEntity)
			((InventoryBlockEntity) tileEntity).readContainerSyncData(buffer);
	}

	public static void sendToClient(InventoryBlockEntity blockEntity)
	{
		World level = blockEntity.getLevel();

		if(level == null || level.isClientSide)
			return;

		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
		blockEntity.writeContainerSyncData(buffer);
		BlockPos blockPos = blockEntity.getBlockPos();
		ApexCore.NETWORK.sendTo(new SyncContainerPacket(blockPos, buffer), PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 192D, level.dimension())));
	}
}
