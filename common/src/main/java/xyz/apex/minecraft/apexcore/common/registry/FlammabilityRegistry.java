package xyz.apex.minecraft.apexcore.common.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public interface FlammabilityRegistry
{
    static void register(Block block, int igniteOdds, int burnOdds)
    {
        ((FireBlock) Blocks.FIRE).setFlammable(block, igniteOdds, burnOdds);
    }
}
