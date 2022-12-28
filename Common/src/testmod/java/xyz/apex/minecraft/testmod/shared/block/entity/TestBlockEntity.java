package xyz.apex.minecraft.testmod.shared.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public final class TestBlockEntity extends BlockEntity
{
    public static final String NBT_CLICK_COUNT = "ClickCount";

    private int clickCount = 0;

    public TestBlockEntity(BlockEntityType<TestBlockEntity> blockEntityType, BlockPos pos, BlockState blockState)
    {
        super(blockEntityType, pos, blockState);
    }

    public void click(Player player)
    {
        clickCount++;
        player.displayClientMessage(Component.literal("Clicks: %d".formatted(clickCount)), true);
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        tag.putInt(NBT_CLICK_COUNT, clickCount);
    }

    @Override
    public void load(CompoundTag tag)
    {
        if(tag.contains(NBT_CLICK_COUNT, Tag.TAG_ANY_NUMERIC)) clickCount = tag.getInt(NBT_CLICK_COUNT);
        else clickCount = 0;
    }
}
