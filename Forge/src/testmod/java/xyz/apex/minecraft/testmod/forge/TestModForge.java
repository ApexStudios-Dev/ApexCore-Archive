package xyz.apex.minecraft.testmod.forge;

import net.minecraftforge.fml.common.Mod;

import xyz.apex.minecraft.testmod.shared.TestMod;

@Mod(TestMod.ID)
public final class TestModForge
{
    public TestModForge()
    {
        TestMod.bootstrap();
    }
}
