package xyz.apex.minecraft.apexcore.common.lib.multiblock;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentType;

import java.util.List;
import java.util.function.Consumer;

/**
 * Block entity aspect of {@link MultiBlockComponent}.
 * <p>
 * Does nothing other than manging which blocks are connected to which master block.
 */
public final class MultiBlockEntityComponent extends BlockEntityComponent
{
    public static final BlockEntityComponentType<MultiBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(ApexCore.ID, "multi_block", MultiBlockEntityComponent::new);

    private static final String NBT_MASTER = "Master";
    private static final String NBT_CHILDREN = "Children";

    private final List<BlockPos> children = Lists.newArrayList();
    @Nullable
    private BlockPos masterPos = null;
    private boolean init = false;

    private MultiBlockEntityComponent(BlockEntityComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    /**
     * @return True if this component is master / origin point.
     */
    public boolean isMaster()
    {
        return masterPos == null;
    }

    /**
     * @return Position of master / origin point.
     */
    public BlockPos getMasterPos()
    {
        return masterPos == null ? getPos() : masterPos;
    }

    /**
     * @return Block component associated with this block entity component.
     */
    public MultiBlockComponent getBlockComponent()
    {
        // must be bound to a block component holder
        // with multi block component registered
        var block = toGameObject().getBlockState().getBlock();
        Validate.isInstanceOf(BlockComponentHolder.class, block);
        return ((BlockComponentHolder) block).getComponentOrThrow(BlockComponentTypes.MULTI_BLOCK);
    }

    /**
     * @return Multi block pattern associated with this component.
     */
    public MultiBlockPattern getPattern()
    {
        return getBlockComponent().getPattern();
    }

    @Nullable
    @Override
    protected Tag serialize(boolean forNetwork)
    {
        var tag = new CompoundTag();
        if(masterPos != null) tag.put(NBT_MASTER, NbtUtils.writeBlockPos(masterPos));

        if(!children.isEmpty())
        {
            var childrenTag = new ListTag();
            children.stream().map(NbtUtils::writeBlockPos).forEach(childrenTag::add);
            tag.put(NBT_CHILDREN, childrenTag);
        }

        if(tag.isEmpty()) return null;
        return tag;
    }

    @Override
    protected void deserialize(Tag tag, boolean fromNetwork)
    {
        children.clear();
        init = true;

        if(!(tag instanceof CompoundTag compound)) return;

        if(compound.contains(NBT_MASTER, Tag.TAG_COMPOUND))
            masterPos = NbtUtils.readBlockPos(compound.getCompound(NBT_MASTER));

        if(compound.contains(NBT_CHILDREN, Tag.TAG_LIST))
        {
            var childrenTag = compound.getList(NBT_CHILDREN, Tag.TAG_COMPOUND);

            for(var i = 0; i < childrenTag.size(); i++)
            {
                children.add(NbtUtils.readBlockPos(childrenTag.getCompound(i)));
            }
        }
    }

    void onPlace(Consumer<BlockPos> childPosConsumer)
    {
        if(init) return;

        masterPos = null;
        children.clear();

        var pattern = getPattern();
        var level = getLevel();
        var originWS = getPos();
        var blockState = getBlockState();

        for(var localSpace : pattern.getPositions())
        {
            var worldSpace = originWS.offset(localSpace);
            if(worldSpace.equals(originWS)) continue;
            childPosConsumer.accept(worldSpace);
            level.setBlock(worldSpace, blockState, Block.UPDATE_ALL);

            executeAs(level, worldSpace, child -> {
                child.masterPos = originWS;
                child.init = true;
                children.add(worldSpace);
            }, () -> ApexCore.LOGGER.warn("Failed to find connected MultiBlock Entity for origin: '{}' at position '{}' (local: '{}')", originWS, worldSpace, localSpace));
        }

        init = true;
    }

    void onRemove(BlockPos breakPos)
    {
        if(!init) return;

        var level = getLevel();

        if(masterPos != null)
        {
            var tmp = masterPos;
            masterPos = null;
            executeAs(level, tmp, master -> master.onRemove(breakPos));
        }

        children.forEach(child -> destroyIfNotOrigin(level, child, breakPos));
        children.clear();
        destroyIfNotOrigin(level, getPos(), breakPos);
        init = false;
    }

    private static void destroyIfNotOrigin(Level level, BlockPos pos, BlockPos breakPos)
    {
        if(pos.equals(breakPos)) return;
        level.destroyBlock(pos, false);
    }

    private static void executeAs(BlockGetter level, BlockPos pos, Consumer<MultiBlockEntityComponent> consumer)
    {
        var blockEntity = level.getBlockEntity(pos);
        if(!(blockEntity instanceof BlockEntityComponentHolder holder)) return;
        holder.getOptionalComponent(COMPONENT_TYPE).ifPresent(consumer);
    }

    private static void executeAs(BlockGetter level, BlockPos pos, Consumer<MultiBlockEntityComponent> consumer, Runnable emptyAction)
    {
        var blockEntity = level.getBlockEntity(pos);
        if(!(blockEntity instanceof BlockEntityComponentHolder holder)) return;
        holder.getOptionalComponent(COMPONENT_TYPE).ifPresentOrElse(consumer, emptyAction);
    }
}
