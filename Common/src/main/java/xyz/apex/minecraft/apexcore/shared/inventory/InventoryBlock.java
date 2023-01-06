package xyz.apex.minecraft.apexcore.shared.inventory;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import xyz.apex.minecraft.apexcore.shared.multiblock.MultiBlock;
import xyz.apex.minecraft.apexcore.shared.multiblock.MultiBlockType;
import xyz.apex.minecraft.apexcore.shared.multiblock.SimpleMultiBlock;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntityEntry;
import xyz.apex.minecraft.apexcore.shared.registry.entry.MenuEntry;

import java.util.Optional;

public abstract class InventoryBlock<T extends InventoryBlockEntity, M extends InventoryMenu> extends BaseEntityBlock
{
    protected InventoryBlock(Properties properties)
    {
        super(properties);
    }

    public abstract BlockEntityEntry<T> getInventoryBlockEntityType();

    public abstract MenuEntry<M> getInventoryMenuType();

    @Nullable
    protected T getBlockEntity(BlockGetter level, BlockPos pos, BlockState blockState)
    {
        return getInventoryBlockEntityType().get(level, pos);
    }

    protected final Optional<T> getOptionalBlockEntity(BlockGetter level, BlockPos pos, BlockState blockState)
    {
        return Optional.ofNullable(getBlockEntity(level, pos, blockState));
    }

    public InteractionResult openMenu(Level level, BlockPos pos, BlockState blockState, Player player)
    {
        var blockEntity = getBlockEntity(level, pos, blockState);
        if(blockEntity == null) return InteractionResult.PASS;

        if(player instanceof ServerPlayer serverPlayer)
        {
            var displayName = blockEntity.getDisplayName();
            getInventoryMenuType().open(serverPlayer, displayName, data -> writeInventoryData(data, level, pos, blockState));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return openMenu(level, pos, blockState, player);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState)
    {
        return getInventoryBlockEntityType().create(pos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(!stack.hasCustomHoverName()) return;
        getOptionalBlockEntity(level, pos, blockState).ifPresent(blockEntity -> blockEntity.setCustomName(stack.getDisplayName()));
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
        onRemovePre(blockState, level, pos, newBlockState, isMoving);
        onRemovePost(blockState, level, pos, newBlockState, isMoving);
        super.onRemove(blockState, level, pos, newBlockState, isMoving);
    }

    protected void onRemovePre(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
        if(!blockState.is(newBlockState.getBlock()))
        {
            getOptionalBlockEntity(level, pos, blockState).ifPresent(blockEntity -> {
                Inventory.dropContents(level, pos, blockEntity);
                level.updateNeighbourForOutputSignal(pos, blockState.getBlock());
            });
        }
    }

    protected void onRemovePost(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
    {
        return getOptionalBlockEntity(level, pos, blockState).map(Inventory::getRedstoneSignalFromInventory).orElse(0);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos)
    {
        return getOptionalBlockEntity(level, pos, blockState).map(blockEntity -> {
            var displayName = blockEntity.getDisplayName();
            return getInventoryMenuType().asProvider(displayName, data -> writeInventoryData(data, level, pos, blockState));
        }).orElse(null);
    }

    protected void writeInventoryData(FriendlyByteBuf data, Level level, BlockPos pos, BlockState blockState)
    {
        data.writeBlockPos(pos);
    }

    public static abstract class AsMultiBlock<T extends InventoryBlockEntity, M extends InventoryMenu> extends InventoryBlock<T, M> implements MultiBlock
    {
        protected final MultiBlockType multiBlockType;

        protected AsMultiBlock(MultiBlockType multiBlockType, Properties properties)
        {
            super(properties);

            this.multiBlockType = multiBlockType;
            SimpleMultiBlock.replaceBlockStateContainer(this); // must be after we set the MultiBlockType field
            registerDefaultState(multiBlockType.registerDefaultBlockState(defaultBlockState()));
        }

        @Override
        public boolean isSameBlockTypeForMultiBlock(BlockState blockState)
        {
            return blockState.is(this);
        }

        // this method *SHOULD* be override if block entity type changes
        // return correct block entity type at origin point
        // return some delegate type at all other points
        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState)
        {
            return multiBlockType.isOrigin(blockState) ? getInventoryBlockEntityType().create(pos, blockState) : new InventoryBlockEntity.Delegate(getInventoryBlockEntityType().get(), pos, blockState);
        }

        @Override
        public final MultiBlockType getMultiBlockType()
        {
            return multiBlockType;
        }

        @Nullable
        @Override
        public BlockState getStateForPlacement(BlockPlaceContext ctx)
        {
            return multiBlockType.getStateForPlacement(this, defaultBlockState(), ctx);
        }

        @Override
        public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos)
        {
            return multiBlockType.canSurvive(this, level, pos, blockState);
        }

        @Override
        public void onPlace(BlockState blockState, Level level, BlockPos pos, BlockState oldBlockState, boolean isMoving)
        {
            multiBlockType.onPlace(this, blockState, level, pos, oldBlockState);
        }

        @Override
        protected void onRemovePost(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
        {
            multiBlockType.onRemove(this, blockState, level, pos, newBlockState);
        }

        @SuppressWarnings("ConstantValue")
        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
        {
            // null on first call, as it's set in constructor and this method is called from super
            // none-null on second call, as that's fired in our constructor and replaces the vanilla state definition
            if(multiBlockType != null) multiBlockType.registerBlockProperty(builder::add);
        }

        @Override
        public RenderShape getRenderShape(BlockState blockState)
        {
            return multiBlockType.isOrigin(blockState) ? RenderShape.MODEL : RenderShape.INVISIBLE;
        }

        @Override
        public InteractionResult openMenu(Level level, BlockPos pos, BlockState blockState, Player player)
        {
            if(!isSameBlockTypeForMultiBlock(blockState)) return InteractionResult.PASS;

            if(!multiBlockType.isOrigin(blockState))
            {
                var originPos = multiBlockType.getOriginPos(this, blockState, pos);
                var originBlockState = level.getBlockState(originPos);
                return openMenu(level, originPos, originBlockState, player);
            }

            return super.openMenu(level, pos, blockState, player);
        }
    }
}
