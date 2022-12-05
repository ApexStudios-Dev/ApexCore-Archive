package xyz.apex.minecraft.apexcore.shared.item;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;

public class CustomAxeItem extends AxeItem
{
    public CustomAxeItem(Tier tier, float baseAttackDamage, float attackSpeed, Properties properties)
    {
        super(tier, baseAttackDamage, attackSpeed, properties);
    }
}
