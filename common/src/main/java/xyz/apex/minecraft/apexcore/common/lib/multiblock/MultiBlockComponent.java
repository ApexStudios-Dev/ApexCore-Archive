package xyz.apex.minecraft.apexcore.common.lib.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentTypes;

import java.util.Objects;

/**
 * Component for managing multi blocks.
 */
public final class MultiBlockComponent extends BlockComponent
{
    public static final BlockComponentType<MultiBlockComponent> COMPONENT_TYPE = BlockComponentType.register(ApexCore.ID, "multi_block", MultiBlockComponent::new);

    @Nullable
    private MultiBlockPattern pattern;

    private MultiBlockComponent(BlockComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    /**
     * Bind a multi block pattern to this component.
     * <p>
     * Must be called during component registration, failure to do sure will result in {@link NullPointerException}.
     *
     * @param pattern Pattern to bind to this component.
     * @return This component instance.
     */
    public MultiBlockComponent withPattern(MultiBlockPattern pattern)
    {
        Validate.isTrue(!registeredComponents());
        this.pattern = pattern;
        return this;
    }

    /**
     * @return Multi block pattern bound to this component.
     */
    public MultiBlockPattern getPattern()
    {
        return Objects.requireNonNull(pattern);
    }

    /**
     * Returns block entity component associated with this block component.
     *
     * @param level level to look up block entity in.
     * @param pos   position to look up block entity at.
     * @return Block entity component associated with this block component.
     */
    public MultiBlockEntityComponent getBlockEntityComponent(BlockGetter level, BlockPos pos)
    {
        // multi blocks require a block entity component holder
        // with the multi block entity component registered
        var blockEntity = level.getBlockEntity(pos);
        Validate.notNull(blockEntity);
        Validate.isInstanceOf(BlockEntityComponentHolder.class, blockEntity);
        return ((BlockEntityComponentHolder) blockEntity).getComponentOrThrow(BlockEntityComponentTypes.MULTI_BLOCK);
    }

    @Nullable
    @Override
    protected BlockState getStateForPlacement(BlockState placementBlockState, BlockPlaceContext ctx)
    {
        var pattern = getPattern();
        var level = ctx.getLevel();
        var originWS = ctx.getClickedPos();

        for(var localSpace : pattern.getPositions())
        {
            var worldSpace = originWS.offset(localSpace);
            if(level.isOutsideBuildHeight(worldSpace)) return null;
            if(!level.getBlockState(worldSpace).canBeReplaced()) return null;

            var entities = level.getEntities((Entity) null, new AABB(worldSpace), entity -> entity.isAlive() && !entity.isSpectator());
            if(!entities.isEmpty()) return null;
        }

        return placementBlockState;
    }

    @Override
    protected void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        getBlockEntityComponent(level, pos).onPlace(childPos -> {
            if(level.isEmptyBlock(childPos)) return;
            level.destroyBlock(childPos, true);
        });
    }

    @Override
    protected void playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player)
    {
        getBlockEntityComponent(level, pos).onRemove(pos);
    }
}
