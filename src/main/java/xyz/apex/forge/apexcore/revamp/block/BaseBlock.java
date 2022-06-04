package xyz.apex.forge.apexcore.revamp.block;

import io.netty.buffer.Unpooled;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import xyz.apex.forge.apexcore.lib.util.ContainerHelper;
import xyz.apex.forge.apexcore.lib.util.INameableMutable;
import xyz.apex.java.utility.nullness.NonnullConsumer;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public class BaseBlock extends Block implements IWaterLoggable
{
	public static final DirectionProperty FACING_4_WAY = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public BaseBlock(Properties properties)
	{
		super(properties);

		postBlockConstructor();
	}

	// region: Core
	protected void registerProperties(NonnullConsumer<Property<?>> consumer)
	{
	}

	private void postBlockConstructor()
	{
		StateContainer.Builder<Block, BlockState> builder = new StateContainer.Builder<>(this);
		registerProperties(builder::add);
		StateContainer<Block, BlockState> stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
		registerDefaultState(stateDefinition.any());
		ObfuscationReflectionHelper.setPrivateValue(Block.class, this, stateDefinition, "field_176227_L");
	}
	// endregion

	// region: Overrides
	@Override
	public final BlockState getStateForPlacement(BlockItemUseContext ctx)
	{
		BlockState blockState = defaultBlockState();

		World level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();

		if(blockState.hasProperty(WATERLOGGED))
		{
			FluidState fluidState = level.getFluidState(pos);
			boolean waterLogged = fluidState.is(FluidTags.WATER);
			blockState = blockState.setValue(WATERLOGGED, waterLogged);
		}

		if(blockState.hasProperty(FACING_4_WAY))
			blockState = blockState.setValue(FACING_4_WAY, getFourWayFacing(ctx));

		return blockState;
	}

	@Override
	public BlockState updateShape(BlockState blockState, Direction facing, BlockState facingBlockState, IWorld level, BlockPos pos, BlockPos facingPos)
	{
		if(blockState.hasProperty(WATERLOGGED) && blockState.getValue(WATERLOGGED))
			level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

		return super.updateShape(blockState, facing, facingBlockState, level, pos, facingPos);
	}

	@Override
	protected final void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
	{
	}

	@Override
	public final BlockState rotate(BlockState blockState, Rotation rotation)
	{
		if(blockState.hasProperty(FACING_4_WAY))
			return blockState.setValue(FACING_4_WAY, rotation.rotate(blockState.getValue(FACING_4_WAY)));

		return blockState;
	}

	@Override
	public final BlockState mirror(BlockState blockState, Mirror mirror)
	{
		if(blockState.hasProperty(FACING_4_WAY))
			return blockState.rotate(mirror.getRotation(blockState.getValue(FACING_4_WAY)));

		return blockState;
	}
	// endregion

	// region: WaterLogging
	@Override
	public boolean propagatesSkylightDown(BlockState blockState, IBlockReader level, BlockPos pos)
	{
		if(blockState.hasProperty(WATERLOGGED))
			return !blockState.getValue(WATERLOGGED);
		return super.propagatesSkylightDown(blockState, level, pos);
	}

	@Override
	public final FluidState getFluidState(BlockState blockState)
	{
		return blockState.hasProperty(WATERLOGGED) && blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
	}

	@Override
	public final boolean canPlaceLiquid(IBlockReader level, BlockPos pos, BlockState blockState, Fluid fluid)
	{
		return blockState.hasProperty(WATERLOGGED) && IWaterLoggable.super.canPlaceLiquid(level, pos, blockState, fluid);
	}

	@Override
	public final boolean placeLiquid(IWorld level, BlockPos pos, BlockState blockState, FluidState fluidState)
	{
		return blockState.hasProperty(WATERLOGGED) && IWaterLoggable.super.placeLiquid(level, pos, blockState, fluidState);
	}

	@Override
	public final Fluid takeLiquid(IWorld level, BlockPos pos, BlockState blockState)
	{
		return !blockState.hasProperty(WATERLOGGED) ? Fluids.EMPTY : IWaterLoggable.super.takeLiquid(level, pos, blockState);
	}
	// endregion

	// region: FourWay
	protected Direction getFourWayFacing(BlockItemUseContext ctx)
	{
		return ctx.getHorizontalDirection().getOpposite();
	}
	// endregion

	public static abstract class WithBlockEntity<BLOCK_ENTITY extends TileEntity> extends BaseBlock implements ITileEntityProvider
	{
		public WithBlockEntity(Properties properties)
		{
			super(properties);
		}

		// region: Core
		protected abstract TileEntityType<BLOCK_ENTITY> getBlockEntityType();
		// endregion

		// region: Overrides
		@Nullable
		protected final BLOCK_ENTITY getBlockEntity(IBlockReader level, BlockPos pos)
		{
			return getBlockEntityType().getBlockEntity(level, pos);
		}

		@Override
		public final boolean triggerEvent(BlockState blockState, World level, BlockPos pos, int event, int param)
		{
			BLOCK_ENTITY blockEntity = getBlockEntity(level, pos);
			return blockEntity != null && blockEntity.triggerEvent(event, param);
		}

		@Nullable
		@Override
		public final TileEntity newBlockEntity(IBlockReader level)
		{
			return getBlockEntityType().create();
		}

		@Nullable
		@Override
		public final TileEntity createTileEntity(BlockState state, IBlockReader world)
		{
			return getBlockEntityType().create();
		}

		@Override
		public final boolean hasTileEntity(BlockState state)
		{
			return true;
		}
		// endregion
	}

	public static abstract class WithContainer<BLOCK_ENTITY extends TileEntity, CONTAINER extends Container> extends WithBlockEntity<BLOCK_ENTITY>
	{
		public WithContainer(Properties properties)
		{
			super(properties);
		}

		// region: Core
		protected abstract ContainerType<CONTAINER> getContainerType();

		protected final ActionResultType tryOpenContainerScreen(BlockState blockState, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
		{
			INamedContainerProvider provider = getMenuProvider(blockState, level, pos);

			if(provider != null)
			{
				if(level.isClientSide)
					return ActionResultType.SUCCESS;

				if(player instanceof ServerPlayerEntity)
				{
					NetworkHooks.openGui((ServerPlayerEntity) player, provider, buffer -> buffer.writeBlockPos(pos));
					return ActionResultType.CONSUME;
				}
			}

			return ActionResultType.PASS;
		}
		// endregion

		// region: Overrides
		@Override
		public final boolean hasAnalogOutputSignal(BlockState blockState)
		{
			return true;
		}

		@Override
		public final int getAnalogOutputSignal(BlockState blockState, World level, BlockPos pos)
		{
			return ContainerHelper.getRedstoneSignalFromContainer(level, pos);
		}

		@Override
		public ActionResultType use(BlockState blockState, World level, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
		{
			return tryOpenContainerScreen(blockState, level, pos, player, hand, rayTraceResult);
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		public void setPlacedBy(World level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
		{
			super.setPlacedBy(level, pos, blockState, placer, stack);

			BLOCK_ENTITY blockEntity = getBlockEntity(level, pos);

			if(blockEntity instanceof INameableMutable && stack.hasCustomHoverName())
			{
				ITextComponent customName = stack.getHoverName();
				((INameableMutable) blockEntity).setCustomName(customName);
			}
		}

		@OverridingMethodsMustInvokeSuper
		@Override
		public void onRemove(BlockState blockState, World level, BlockPos pos, BlockState newBlockState, boolean isMoving)
		{
			if(!blockState.is(newBlockState.getBlock()))
			{
				BLOCK_ENTITY blockEntity = getBlockEntity(level, pos);

				if(blockEntity != null)
				{
					blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
						for(int i = 0; i < itemHandler.getSlots(); i++)
						{
							ItemStack stack = itemHandler.getStackInSlot(i);
							InventoryHelper.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
						}
					});
				}
			}

			super.onRemove(blockState, level, pos, newBlockState, isMoving);
		}

		@Nullable
		@Override
		public final INamedContainerProvider getMenuProvider(BlockState blockState, World level, BlockPos pos)
		{
			BLOCK_ENTITY blockEntity = getBlockEntity(level, pos);

			if(blockEntity != null)
			{
				ITextComponent containerName = new TranslationTextComponent(getDescriptionId());

				if(blockEntity instanceof INameable)
					containerName = ((INameable) blockEntity).getDisplayName();

				return new SimpleNamedContainerProvider((windowId, playerInventory, player) -> {
					PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
					buffer.writeBlockPos(pos);
					return getContainerType().create(windowId, playerInventory, buffer);
				}, containerName);
			}

			return null;
		}
		// endregion
	}
}
