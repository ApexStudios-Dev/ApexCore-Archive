package xyz.apex.minecraft.apexcore.shared.item;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;

public class CustomHoeItem extends HoeItem
{
    public CustomHoeItem(Tier tier, int baseAttackDamage, float attackSpeed, Properties properties)
    {
        super(tier, baseAttackDamage, attackSpeed, properties);
    }
}
