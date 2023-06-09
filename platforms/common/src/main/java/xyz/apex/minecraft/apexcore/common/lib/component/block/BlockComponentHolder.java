package xyz.apex.minecraft.apexcore.common.lib.component.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public sealed interface BlockComponentHolder extends SimpleWaterloggedBlock permits BaseBlockComponentHolder
{
    @Nullable
    <C extends BlockComponent> C getComponent(BlockComponentType<C> componentType);

    <C extends BlockComponent> Optional<C> findComponent(BlockComponentType<C> componentType);

    <C extends BlockComponent> C getRequiredComponent(BlockComponentType<C> componentType);

    boolean hasComponent(BlockComponentType<?> componentType);

    Set<BlockComponentType<?>> getComponentTypes();

    Collection<BlockComponent> getComponents();

    Block getGameObject();
}
