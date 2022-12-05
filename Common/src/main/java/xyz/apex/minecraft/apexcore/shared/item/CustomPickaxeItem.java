package xyz.apex.minecraft.apexcore.shared.item;

import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class CustomPickaxeItem extends PickaxeItem
{
    public CustomPickaxeItem(Tier tier, int baseAttackDamage, float attackSpeed, Properties properties)
    {
        super(tier, baseAttackDamage, attackSpeed, properties);
    }
}
