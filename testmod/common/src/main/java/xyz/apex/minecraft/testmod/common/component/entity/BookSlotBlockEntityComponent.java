package xyz.apex.minecraft.testmod.common.component.entity;

import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BaseContainerBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentType;
import xyz.apex.minecraft.testmod.common.TestMod;

public final class BookSlotBlockEntityComponent extends BaseContainerBlockEntityComponent<BookSlotBlockEntityComponent>
{
    public static final BlockEntityComponentType<BookSlotBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(TestMod.ID, "book_slot", BookSlotBlockEntityComponent::new);

    private BookSlotBlockEntityComponent(BlockEntityComponentHolder componentHolder)
    {
        super(componentHolder);

        super.withSlotCount(1);
    }

    @Deprecated
    @Override
    public BookSlotBlockEntityComponent withSlotCount(int slotCount)
    {
        return this;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack)
    {
        return stack.is(ItemTags.BOOKSHELF_BOOKS);
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction side)
    {
        return stack.is(ItemTags.BOOKSHELF_BOOKS);
    }
}
