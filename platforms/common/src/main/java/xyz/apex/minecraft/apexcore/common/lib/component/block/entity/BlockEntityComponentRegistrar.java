package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import java.util.function.Consumer;

@FunctionalInterface
public interface BlockEntityComponentRegistrar
{
    <C extends BlockEntityComponent> void register(BlockEntityComponentType<C> componentType, Consumer<C> onRegister);

    default void register(BlockEntityComponentType<?> componentType)
    {
        register(componentType, component -> { });
    }
}
