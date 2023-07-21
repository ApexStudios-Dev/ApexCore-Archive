package xyz.apex.minecraft.apexcore.fabric.core;

import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ApexCoreFabricMod implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        ApexCoreImpl.bootstrap0();
    }
}
