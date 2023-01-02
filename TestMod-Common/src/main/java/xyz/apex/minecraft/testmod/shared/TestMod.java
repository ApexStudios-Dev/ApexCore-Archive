package xyz.apex.minecraft.testmod.shared;

import xyz.apex.minecraft.apexcore.shared.platform.ModPlatform;
import xyz.apex.minecraft.apexcore.shared.registry.Registrar;
import xyz.apex.minecraft.testmod.shared.init.AllItems;

public interface TestMod extends ModPlatform
{
    String ID = "testmod";
    Registrar REGISTRAR = Registrar.create(ID);

    @Override
    default void initialize()
    {
        AllItems.bootstrap();
        ModPlatform.super.initialize();
    }
}
