package xyz.apex.minecraft.apexcore.forge.platform.data;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;

import xyz.apex.minecraft.apexcore.shared.data.Generators;
import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;
import xyz.apex.minecraft.apexcore.shared.data.providers.Language;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@ApiStatus.Internal
public final class EnglishGenerator extends DelegatedProvider<LanguageProvider> implements Language
{
    EnglishGenerator(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
    {
        super(packOutputSupplier, lookupProviderSupplier, modId, existingFileHelper);
    }

    @Override
    protected LanguageProvider createProvider(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
    {
        return new LanguageProvider(packOutputSupplier.get(), modId, "en_us") {
            @Override
            protected void addTranslations()
            {
                Generators.processDataGenerator(modId, ProviderTypes.LANGUAGE, EnglishGenerator.this);
            }
        };
    }

    @Override
    public Language add(String key, String name)
    {
        provider.add(key, name);
        return this;
    }
}
