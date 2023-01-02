package xyz.apex.minecraft.testmod.fabric;

import xyz.apex.minecraft.apexcore.fabric.platform.FabricModPlatform;
import xyz.apex.minecraft.testmod.shared.TestMod;

public final class TestModFabric extends FabricModPlatform implements TestMod
{
    public static final FabricModPlatform INSTANCE = new TestModFabric();

    private TestModFabric()
    {
        super(ID, REGISTRAR);
    }
}
