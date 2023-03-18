package xyz.apex.minecraft.apexcore.common.component;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Consumer;

@FunctionalInterface
public interface ComponentBlockFactory<B extends Block & ComponentBlock>
{
    B create(Consumer<B> registerComponents, BlockBehaviour.Properties properties);
}
