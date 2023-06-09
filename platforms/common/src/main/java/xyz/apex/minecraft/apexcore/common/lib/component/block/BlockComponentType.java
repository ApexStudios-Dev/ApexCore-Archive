package xyz.apex.minecraft.apexcore.common.lib.component.block;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

public sealed interface BlockComponentType<T extends BlockComponent> permits BlockComponentTypeImpl
{
    ResourceLocation registryName();

    @DoNotCall
    @ApiStatus.Internal
    T newInstance(BlockComponentHolder componentHolder);

    static <T extends BlockComponent> BlockComponentType<T> register(String ownerId, String componentName, BlockComponentFactory<T> componentFactory)
    {
        return BlockComponentTypeImpl.register(ownerId, componentName, componentFactory);
    }

    @FunctionalInterface
    interface BlockComponentFactory<T extends BlockComponent>
    {
        T create(BlockComponentHolder componentHolder);
    }
}
