package xyz.apex.minecraft.apexcore.common.lib.component.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import xyz.apex.minecraft.apexcore.common.lib.component.BaseComponent;

/**
 * Base implementation for item components.
 */
public class ItemComponent extends BaseComponent<Item, ItemComponentHolder>
{
    protected ItemComponent(ItemComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    protected void verifyTagAfterLoad(CompoundTag tag)
    {
    }

    protected InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    protected boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player)
    {
        return false;
    }

    protected boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess slotAccess)
    {
        return false;
    }

    protected boolean canFitInsideContainerItems()
    {
        return true;
    }
}
