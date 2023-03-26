package xyz.apex.minecraft.apexcore.common.component.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Consumer;

@FunctionalInterface
public interface BlockComponentHolderFactory<T extends Block & BlockComponentHolder>
{
    T create(Consumer<BlockComponentHolder.Registrar> registrar, BlockBehaviour.Properties properties);
}
