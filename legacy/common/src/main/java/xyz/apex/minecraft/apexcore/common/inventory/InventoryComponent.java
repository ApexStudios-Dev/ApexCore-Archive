package xyz.apex.minecraft.apexcore.common.inventory;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.ComponentBlock;
import xyz.apex.minecraft.apexcore.common.component.ComponentType;
import xyz.apex.minecraft.apexcore.common.component.ComponentTypes;
import xyz.apex.minecraft.apexcore.common.component.SimpleComponent;
import xyz.apex.minecraft.apexcore.common.component.components.BlockEntityComponent;

import java.util.Optional;

public final class InventoryComponent extends SimpleComponent
{
    public static final ComponentType<InventoryComponent> COMPONENT_TYPE = ComponentType
            .builder(new ResourceLocation(ApexCore.ID, "inventory"), InventoryComponent.class)
                .requires(ComponentTypes.BLOCK_ENTITY)
            .register();

    @ApiStatus.Internal // public cause reflection
    public InventoryComponent(ComponentBlock block)
    {
        super(block);
    }

    public Optional<InventoryBlockEntity> getBlockEntity(BlockGetter level, BlockPos pos)
    {
        return getOptionalComponent(ComponentTypes.BLOCK_ENTITY)
                .flatMap(component -> BlockEntityComponent.lookupBlockEntity(level, pos))
                .filter(InventoryBlockEntity.class::isInstance)
                .map(InventoryBlockEntity.class::cast);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(!stack.hasCustomHoverName()) return;
        getBlockEntity(level, pos).ifPresent(blockEntity -> blockEntity.setCustomName(stack.getDisplayName()));
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving)
    {
        if(blockState.is(newBlockState.getBlock())) return;

        getBlockEntity(level, pos).ifPresent(blockEntity -> {
            Inventory.dropContents(level, pos, blockEntity);
            level.updateNeighbourForOutputSignal(pos, blockState.getBlock());
        });
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState)
    {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos)
    {
        return getBlockEntity(level, pos).map(Inventory::getRedstoneSignalFromInventory).orElse(0);
    }
}
