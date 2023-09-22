package xyz.apex.minecraft.apexcore.mcforge.core;

import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

@Mod(ApexCore.ID)
@ApiStatus.Internal
public final class ApexCoreMinecraftForgeEP
{
    public ApexCoreMinecraftForgeEP()
    {
        ApexCore.INSTANCE.bootstrap();
    }
}
