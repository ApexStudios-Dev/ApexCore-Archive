package xyz.apex.minecraft.apexcore.forge.platform.data;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@ApiStatus.Internal
public abstract class DelegatedProvider<P extends DataProvider> implements DataProvider
{
    protected final P provider;
    protected final ExistingFileHelper existingFileHelper;
    protected final String modId;

    protected DelegatedProvider(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper)
    {
        this.modId = modId;
        this.existingFileHelper = existingFileHelper;

        provider = createProvider(packOutputSupplier, lookupProviderSupplier, modId, existingFileHelper);
    }

    protected abstract P createProvider(Supplier<PackOutput> packOutputSupplier, Supplier<CompletableFuture<HolderLookup.Provider>> lookupProviderSupplier, String modId, ExistingFileHelper existingFileHelper);

    @Override
    public final CompletableFuture<?> run(CachedOutput pOutput)
    {
        return provider.run(pOutput);
    }

    @Override
    public final String getName()
    {
        return "%s (%s)".formatted(provider.getName(), modId);
    }
}
