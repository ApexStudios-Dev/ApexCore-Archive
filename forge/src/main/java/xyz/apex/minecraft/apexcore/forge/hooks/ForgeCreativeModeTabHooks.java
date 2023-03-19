package xyz.apex.minecraft.apexcore.forge.hooks;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.apex.minecraft.apexcore.common.hooks.CreativeModeTabHooks;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatform;
import xyz.apex.minecraft.apexcore.forge.platform.ForgePlatformHolder;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class ForgeCreativeModeTabHooks extends ForgePlatformHolder implements CreativeModeTabHooks
{
    private final Map<String, Mod> mods = Maps.newHashMap();

    ForgeCreativeModeTabHooks(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public void register(ResourceLocation registryName, UnaryOperator<CreativeModeTab.Builder> consumer)
    {
        compute(mod -> mod.registerListeners.add(event -> event.registerCreativeModeTab(registryName, consumer::apply)));
    }

    @Override
    public void modify(CreativeModeTab creativeModeTab, Consumer<CreativeModeTab.Output> consumer)
    {
        compute(mod -> mod.modifyListeners.add(event -> {
            if(event.getTab() != creativeModeTab) return;
            consumer.accept(event);
        }));
    }

    @Override
    public void modify(ResourceLocation registryName, Consumer<CreativeModeTab.Output> consumer)
    {
        compute(mod -> mod.modifyListeners.add(event -> {
            if(event.getTab() != CreativeModeTabRegistry.getTab(registryName)) return;
            consumer.accept(event);
        }));
    }

    private void compute(Consumer<Mod> consumer)
    {
        var modId = ModLoadingContext.get().getActiveNamespace();
        var mod = mods.computeIfAbsent(modId, $ -> new Mod());
        consumer.accept(mod);
    }

    private static final class Mod
    {
        private final List<Consumer<CreativeModeTabEvent.Register>> registerListeners = Lists.newLinkedList();
        private final List<Consumer<CreativeModeTabEvent.BuildContents>> modifyListeners = Lists.newLinkedList();

        private Mod()
        {
            var modBus = FMLJavaModLoadingContext.get().getModEventBus();
            modBus.addListener(EventPriority.HIGHEST, this::onRegisterCreativeModeTab);
            modBus.addListener(EventPriority.HIGHEST, this::onModifyCreativeModeTab);
        }

        private void onRegisterCreativeModeTab(CreativeModeTabEvent.Register event)
        {
            registerListeners.forEach(listener -> listener.accept(event));
            registerListeners.clear();
        }

        private void onModifyCreativeModeTab(CreativeModeTabEvent.BuildContents event)
        {
            modifyListeners.forEach(listener -> listener.accept(event));
        }
    }
}
