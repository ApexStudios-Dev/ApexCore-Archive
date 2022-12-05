package xyz.apex.minecraft.apexcore.shared.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

public class CustomDiggerItem extends DiggerItem
{
    public CustomDiggerItem(float baseAttackDamage, float attackSpeed, Tier tier, TagKey<Block> effectiveBlocks, Properties properties)
    {
        super(baseAttackDamage, attackSpeed, tier, effectiveBlocks, properties);
    }

    public CustomDiggerItem(Tier tier, float baseAttackDamage, float attackSpeed, TagKey<Block> effectiveBlocks, Properties properties)
    {
        this(baseAttackDamage, attackSpeed, tier, effectiveBlocks, properties);
    }
}
