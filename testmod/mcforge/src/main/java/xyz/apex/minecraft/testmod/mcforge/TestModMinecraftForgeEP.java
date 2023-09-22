package xyz.apex.minecraft.testmod.mcforge;

import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.testmod.common.TestMod;

@ApiStatus.Internal
@Mod(TestMod.ID)
public final class TestModMinecraftForgeEP
{
    public TestModMinecraftForgeEP()
    {
        TestMod.INSTANCE.bootstrap();
    }
}
