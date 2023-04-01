package xyz.apex.minecraft.apexcore.common.component.block;

import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public sealed interface BlockComponentHolder permits BaseBlockComponentHolder
{
    void registerComponents(Registrar registrar);

    @Nullable
    <T extends BlockComponent> T getComponent(BlockComponentType<T> componentType);

    <T extends BlockComponent> Optional<T> getOptionalComponent(BlockComponentType<T> componentType);

    <T extends BlockComponent> T getRequiredComponent(BlockComponentType<T> componentType);

    Set<BlockComponentType<?>> getComponentTypes();

    Stream<BlockComponent> components();

    <T extends BlockComponent> boolean hasComponent(BlockComponentType<T> componentType);

    Block toBlock();

    @FunctionalInterface
    interface Registrar
    {
        <T extends BlockComponent> T register(BlockComponentType<T> componentType);
    }
}
