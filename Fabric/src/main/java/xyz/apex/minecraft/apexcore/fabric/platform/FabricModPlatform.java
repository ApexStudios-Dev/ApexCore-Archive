package xyz.apex.minecraft.apexcore.fabric.platform;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.Nullable;

import xyz.apex.minecraft.apexcore.shared.platform.AbstractModPlatform;
import xyz.apex.minecraft.apexcore.shared.registry.Registrar;

public class FabricModPlatform extends AbstractModPlatform implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer
{
    protected final String modId;
    @Nullable protected final Registrar registrar;

    public FabricModPlatform(String modId, @Nullable Registrar registrar)
    {
        super();

        this.modId = modId;

        this.registrar = registrar;
        if(registrar != null) registrar.setMod(this);
    }

    @Override
    public final String getModId()
    {
        return modId;
    }

    @Override
    public final void onInitializeClient()
    {
        if(registrar != null) registrar.lateRegister();
    }

    @Override
    public final void onInitializeServer()
    {
        if(registrar != null) registrar.lateRegister();
    }

    @Override
    public final void onInitialize()
    {
        initialize();
        if(registrar != null) registrar.register();
    }

    @Nullable
    @Override
    public final Registrar getRegistrar()
    {
        return registrar;
    }
}
