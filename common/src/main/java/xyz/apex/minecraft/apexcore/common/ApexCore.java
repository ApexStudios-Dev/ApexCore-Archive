package xyz.apex.minecraft.apexcore.common;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import xyz.apex.minecraft.apexcore.common.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.common.util.ApexTags;

public interface ApexCore extends ModPlatform
{
    String ID = "apexcore";

    @Override
    default void initialize()
    {
        ApexTags.bootstrap();
    }

    @ApiStatus.Internal
    static <T extends Block> void replaceBlockStateContainer(T block)
    {
        var builder = new StateDefinition.Builder<Block, BlockState>(block);
        block.createBlockStateDefinition(builder);
        block.stateDefinition = builder.create(Block::defaultBlockState, BlockState::new);
        block.registerDefaultState(block.stateDefinition.any());
    }
}
