package xyz.apex.minecraft.apexcore.shared.hooks;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.ItemLike;

import xyz.apex.minecraft.apexcore.shared.hooks.acessors.ItemHook;

public interface ItemHooks
{
    static Rarity getRarity(ItemLike itemLike)
    {
        return getRarity(itemLike.asItem());
    }

    static Rarity getRarity(Item item)
    {
        return Hooks.get(item, ItemHook.class, ItemHook::ApexCore$getRarity);
    }

    @SuppressWarnings("DataFlowIssue")
    static Item.Properties copyProperties(Item item)
    {
        var category = item.getItemCategory();
        var rarity = ItemHooks.getRarity(item);
        var maxDamage = item.getMaxDamage();
        var foodProperties = item.getFoodProperties();

        var properties = new Item.Properties().rarity(rarity);

        if(maxDamage > 0) properties = properties.durability(maxDamage);
        else properties = properties.stacksTo(item.getMaxStackSize());

        if(foodProperties != null) properties = properties.food(foodProperties);
        if(item.hasCraftingRemainingItem()) properties = properties.craftRemainder(item.getCraftingRemainingItem());
        if(category != null) properties = properties.tab(category);
        if(item.isFireResistant()) properties = properties.fireResistant();

        return properties;
    }
}
