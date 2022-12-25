package xyz.apex.minecraft.apexcore.shared.mixin.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

@Mixin(Block.class)
public interface BlockAccessor
{
    @Accessor("stateDefinition") @Mutable void ApexCore$setStateDefinition(StateDefinition<Block, BlockState> stateDefinition);
}
