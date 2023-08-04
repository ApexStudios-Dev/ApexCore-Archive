package xyz.apex.minecraft.apexcore.common.lib.registry.entry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;

public final class MenuEntry<T extends AbstractContainerMenu> extends BaseRegistryEntry<MenuType<T>> implements FeaturedEntry<MenuType<T>>
{
    @ApiStatus.Internal
    public MenuEntry(AbstractRegistrar<?> registrar, ResourceKey<MenuType<T>> registryKey)
    {
        super(registrar, registryKey);
    }

    public static <T extends AbstractContainerMenu> MenuEntry<T> cast(RegistryEntry<?> registryEntry)
    {
        return RegistryEntry.cast(MenuEntry.class, registryEntry);
    }
}
