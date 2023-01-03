package xyz.apex.minecraft.testmod.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import xyz.apex.minecraft.testmod.shared.TestMod;

public final class LanguageGenerator extends FabricLanguageProvider
{
    LanguageGenerator(FabricDataOutput dataOutput)
    {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder)
    {
        builder.add(TestMod.TEST_ITEM.get(), "Test Item");
        builder.add(TestMod.TEST_BLOCK.get(), "Test Block");
    }
}
