package xyz.apex.minecraft.apexcore.shared.inventory;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

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

    protected InteractionResult openMenu(Level level, BlockPos pos, BlockState blockState, Player player, InteractionHand hand, @Nullable Direction side)
    {
        var blockEntity = getBlockEntity(level, pos, blockState);
        if(blockEntity == null) return InteractionResult.PASS;

        if(player instanceof ServerPlayer serverPlayer)
        {
            var displayName = blockEntity.getDisplayName();
            getInventoryMenuType().open(serverPlayer, displayName, data -> writeInventoryData(data, level, pos, blockState, side));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        var side = hit.getDirection();
        return openMenu(level, pos, blockState, player, hand, side);
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
        if(!blockState.is(newBlockState.getBlock()))
        {
            getOptionalBlockEntity(level, pos, blockState).ifPresent(blockEntity -> Inventory.dropContents(level, pos, blockEntity.inventory));
            level.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(blockState, level, pos, newBlockState, isMoving);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos)
    {
        return getOptionalBlockEntity(level, pos, blockState).map(blockEntity -> {
            var displayName = blockEntity.getDisplayName();
            return getInventoryMenuType().asProvider(displayName, data -> writeInventoryData(data, level, pos, blockState, null));
        }).orElse(null);
    }

    protected void writeInventoryData(FriendlyByteBuf data, Level level, BlockPos pos, BlockState blockState, @Nullable Direction side)
    {
        data.writeBlockPos(pos);

        var writeSide = side != null;
        data.writeBoolean(writeSide);
        if(writeSide) data.writeEnum(side);
    }
}
