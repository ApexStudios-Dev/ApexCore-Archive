package xyz.apex.minecraft.apexcore.common.lib.component.block;

import java.util.function.Consumer;

@FunctionalInterface
public interface BlockComponentRegistrar
{
    <C extends BlockComponent> void register(BlockComponentType<C> componentType, Consumer<C> onRegister);

    default void register(BlockComponentType<?> componentType)
    {
        register(componentType, component -> { });
    }
}
