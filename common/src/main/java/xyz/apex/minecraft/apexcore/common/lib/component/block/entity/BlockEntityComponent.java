package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.component.BaseComponent;

import java.util.Objects;

/**
 * Base implementation for block entity components.
 */
public class BlockEntityComponent extends BaseComponent<BlockEntity, BlockEntityComponentHolder>
{
    protected BlockEntityComponent(BlockEntityComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    @Nullable
    protected Tag serialize(boolean forNetwork)
    {
        return null;
    }

    protected void deserialize(Tag tag, boolean fromNetwork)
    {
    }

    protected boolean savesToItem()
    {
        return true;
    }

    protected boolean triggerEvent(int id, int type)
    {
        return false;
    }

    protected final void setChanged()
    {
        toGameObject().setChanged();
    }

    protected final Level getLevel()
    {
        return Objects.requireNonNull(toGameObject().getLevel());
    }

    protected final BlockPos getPos()
    {
        return toGameObject().getBlockPos();
    }

    protected final BlockState getBlockState()
    {
        return toGameObject().getBlockState();
    }
}
