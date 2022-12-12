package xyz.apex.minecraft.apexcore.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public abstract class ExtendedLanguageProvider extends LanguageProvider implements xyz.apex.minecraft.apexcore.shared.data.providers.LanguageProvider
{
    public ExtendedLanguageProvider(DataGenerator generator, String modId, String locale)
    {
        super(generator, modId, locale);
    }
}
