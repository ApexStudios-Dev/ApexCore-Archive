package dev.apexstudios.testmod.common.block.entity;

import dev.apexstudios.apexcore.common.inventory.ItemHandler;
import dev.apexstudios.apexcore.common.inventory.ItemHandlerProvider;
import dev.apexstudios.apexcore.common.inventory.SimpleItemHandler;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import dev.apexstudios.testmod.common.menu.TestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class TestBlockEntity extends BlockEntity implements ItemHandlerProvider.Directional, MenuProvider, Consumer<FriendlyByteBuf>, Nameable
{
    private static final String NBT_OAK = "Oak";
    private static final String NBT_BIRCH = "Birch";
    private static final String NBT_CUSTOM_NAME = "CustomName";

    public ItemHandler oakInventory = new SimpleItemHandler(1);
    public ItemHandler birchInventory = new SimpleItemHandler(4);
    @Nullable private Component customName;

    public TestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
    {
        super(type, pos, blockState);
    }

    @Override
    public void load(CompoundTag tag)
    {
        if(tag.contains(NBT_OAK, Tag.TAG_COMPOUND))
        {
            var oakTag = tag.getCompound(NBT_OAK);
            oakInventory = new SimpleItemHandler(1, oakTag);
        }

        if(tag.contains(NBT_BIRCH, Tag.TAG_COMPOUND))
        {
            var oakTag = tag.getCompound(NBT_BIRCH);
            birchInventory = new SimpleItemHandler(4, oakTag);
        }

        if(tag.contains(NBT_CUSTOM_NAME, Tag.TAG_STRING))
            customName = Component.Serializer.fromJsonLenient(tag.getString(NBT_CUSTOM_NAME));
    }

    @Override
    protected void saveAdditional(CompoundTag tag)
    {
        tag.put(NBT_OAK, oakInventory.serialize());
        tag.put(NBT_BIRCH, birchInventory.serialize());

        if(customName != null)
            tag.putString(NBT_CUSTOM_NAME, Component.Serializer.toJson(customName));
    }

    @Override
    public OptionalLike<ItemHandler> getItemHandler(@Nullable Direction side)
    {
        return side == null || side == Direction.UP ? OptionalLike.of(oakInventory) : OptionalLike.of(birchInventory);
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player)
    {
        return new TestMenu(windowId, inventory, oakInventory, birchInventory);
    }

    @Override
    public void accept(FriendlyByteBuf buffer)
    {
        buffer.writeGlobalPos(GlobalPos.of(level.dimension(), worldPosition));
    }

    public void setCustomName(Component customName)
    {
        this.customName = customName;
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
}
