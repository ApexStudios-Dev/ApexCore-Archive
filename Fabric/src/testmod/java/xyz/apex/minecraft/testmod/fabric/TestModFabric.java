package xyz.apex.minecraft.testmod.fabric;

import net.fabricmc.api.ModInitializer;

import xyz.apex.minecraft.testmod.shared.TestMod;

public final class TestModFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        TestMod.bootstrap();
    }
}
