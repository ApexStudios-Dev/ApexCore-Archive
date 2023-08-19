package xyz.apex.minecraft.testmod.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;
import xyz.apex.minecraft.testmod.common.TestMod;
import xyz.apex.minecraft.testmod.common.menu.TestMenu;

public class TestBlockEntity extends BlockEntity implements Nameable, MenuConstructor
{
    @Nullable
    private Component customName = null;

    public TestBlockEntity(BlockEntityType<? extends TestBlockEntity> blockEntityType, BlockPos pos, BlockState blockState)
    {
        super(blockEntityType, pos, blockState);
    }

    public void setCustomName(Component customName)
    {
        this.customName = customName;
        setChanged();
    }

    public MenuProvider createMenuProvider()
    {
        return MenuHooks.get().createMenuProvider(getDisplayName(), this, buffer -> buffer.writeBlockPos(worldPosition));
    }

    @Override
    public void load(CompoundTag tag)
    {
        customName = null;

        if(tag.contains("CustomName", Tag.TAG_STRING))
            customName = Component.Serializer.fromJson(tag.getString("CustomName"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        if(customName != null)
            tag.putString("CustomName", Component.Serializer.toJson(customName));
    }

    @Override
    public boolean hasCustomName()
    {
        return customName != null;
    }

    @Nullable
    @Override
    public Component getCustomName()
    {
        return customName;
    }

    @Override
    public Component getName()
    {
        return getBlockState().getBlock().getName();
    }

    @Override
    public Component getDisplayName()
    {
        return customName == null ? getName() : customName;
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player)
    {
        return new TestMenu(TestMod.TEST_MENU.value(), syncId, inventory);
    }
}
