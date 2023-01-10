package xyz.apex.minecraft.apexcore.quilt.platform;

import dev.architectury.utils.Env;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.base.api.entrypoint.server.DedicatedServerModInitializer;

import xyz.apex.minecraft.apexcore.shared.platform.AbstractModPlatform;
import xyz.apex.minecraft.apexcore.shared.registry.Registrar;

public class QuiltModPlatform extends AbstractModPlatform implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer
{
    protected final String modId;
    @Nullable protected final Registrar registrar;

    public QuiltModPlatform(String modId, @Nullable Registrar registrar)
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
    public final void onInitializeClient(ModContainer mod)
    {
        if(registrar != null) registrar.lateRegister();
        initializeSided(Env.CLIENT);
    }

    @Override
    public final void onInitializeServer(ModContainer mod)
    {
        if(registrar != null) registrar.lateRegister();
        initializeSided(Env.SERVER);
    }

    @Override
    public final void onInitialize(ModContainer mod)
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
