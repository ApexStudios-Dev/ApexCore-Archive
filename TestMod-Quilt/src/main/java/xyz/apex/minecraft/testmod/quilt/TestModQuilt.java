package xyz.apex.minecraft.testmod.quilt;

import xyz.apex.minecraft.apexcore.quilt.platform.QuiltModPlatform;
import xyz.apex.minecraft.testmod.shared.TestMod;

public final class TestModQuilt extends QuiltModPlatform implements TestMod
{
    public static final QuiltModPlatform INSTANCE = new TestModQuilt();

    private TestModQuilt()
    {
        super(ID, REGISTRAR);
    }
}
