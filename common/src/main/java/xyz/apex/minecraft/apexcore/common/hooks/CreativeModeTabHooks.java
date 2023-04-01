package xyz.apex.minecraft.apexcore.common.hooks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import xyz.apex.minecraft.apexcore.common.platform.PlatformHolder;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public interface CreativeModeTabHooks extends PlatformHolder
{
    void register(ResourceLocation registryName, UnaryOperator<CreativeModeTab.Builder> consumer);

    void modify(CreativeModeTab creativeModeTab, Consumer<CreativeModeTab.Output> consumer);

    void modify(ResourceLocation registryName, Consumer<CreativeModeTab.Output> consumer);

    static CreativeModeTabHooks getInstance()
    {
        return Hooks.getInstance().creativeModeTab();
    }
}
