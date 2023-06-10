package xyz.apex.minecraft.apexcore.common.lib.component.block.entity;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public sealed interface BlockEntityComponentType<T extends BlockEntityComponent> permits BlockEntityComponentTypeImpl
{
    ResourceLocation registryName();

    @DoNotCall
    @ApiStatus.Internal
    T newInstance(BlockEntityComponentHolder componentHolder);

    static <T extends BlockEntityComponent> BlockEntityComponentType<T> register(String ownerId, String componentName, BlockComponentFactory<T> componentFactory)
    {
        return BlockEntityComponentTypeImpl.register(ownerId, componentName, componentFactory);
    }

    @Nullable
    static BlockEntityComponentType<?> byName(ResourceLocation registryName)
    {
        return BlockEntityComponentTypeImpl.byName(registryName);
    }

    @FunctionalInterface
    interface BlockComponentFactory<T extends BlockEntityComponent>
    {
        T create(BlockEntityComponentHolder componentHolder);
    }
}
