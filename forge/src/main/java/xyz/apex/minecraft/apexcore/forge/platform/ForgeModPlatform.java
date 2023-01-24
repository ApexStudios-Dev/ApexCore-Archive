package xyz.apex.minecraft.apexcore.forge.platform;

import dev.architectury.platform.forge.EventBuses;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import xyz.apex.minecraft.apexcore.common.platform.AbstractModPlatform;
import xyz.apex.minecraft.apexcore.common.registry.Registrar;

public abstract class ForgeModPlatform extends AbstractModPlatform
{
    protected final IEventBus modBus;
    protected final String modId;
    @Nullable protected final Registrar registrar;

    // manually instantiated
    public ForgeModPlatform(String modId, @Nullable Registrar registrar)
    {
        super();

        this.modId = modId;
        this.registrar = registrar;
        modBus = setup();
    }

    // we are subclass of main mod entry point
    // should be able to access @Mod annotation
    protected ForgeModPlatform(@Nullable Registrar registrar)
    {
        super();

        modId = getClass().getAnnotation(net.minecraftforge.fml.common.Mod.class).value();
        this.registrar = registrar;
        modBus = setup();
    }

    private IEventBus setup()
    {
        if(registrar != null) registrar.setMod(this);
        // should be called from mod entry point, thus this should never fail
        // this *HAS* to be true, in order to obtain the correct mod bus instance
        Validate.isTrue(ModLoadingContext.get().getActiveNamespace().equals(modId));
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(modId, modBus);
        modBus.addListener(EventPriority.HIGHEST, this::onRegister);
        modBus.addListener(EventPriority.LOWEST, this::onLateRegister);
        initialize();
        return modBus;
    }

    @Override
    public final String getModId()
    {
        return modId;
    }

    private void onRegister(RegisterEvent event)
    {
        if(registrar == null) return;

        var vanillaRegistry = event.getVanillaRegistry();
        var forgeRegistry = event.getForgeRegistry();

        if(forgeRegistry != null) registrar.register(forgeRegistry.getRegistryKey());
        else if(vanillaRegistry != null) registrar.register(vanillaRegistry.key());
    }

    private void onLateRegister(RegisterEvent event)
    {
        if(registrar == null) return;

        var vanillaRegistry = event.getVanillaRegistry();
        var forgeRegistry = event.getForgeRegistry();

        if(forgeRegistry != null) registrar.lateRegister(forgeRegistry.getRegistryKey());
        else if(vanillaRegistry != null) registrar.lateRegister(vanillaRegistry.key());
    }

    @Nullable
    @Override
    public final Registrar getRegistrar()
    {
        return registrar;
    }
}
