package xyz.apex.minecraft.apexcore.common.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface AccessorBlock
{
    @Accessor("stateDefinition") @Mutable
    void ApexCore$setStateDefinition(StateDefinition<Block, BlockState> stateDefinition);
}
