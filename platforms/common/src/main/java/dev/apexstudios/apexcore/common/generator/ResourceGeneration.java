package dev.apexstudios.apexcore.common.generator;

import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public final class ResourceGeneration
{
    private static final String KEY_PLATFORM_DIR = "apexcore.resgen.platform-dir";
    static final OptionalLike<PackOutput> PLATFORM_OUTPUT = parsePlatformDir();

    public static void generate(String ownerId, boolean validationEnabled, Consumer<BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, DataProvider>> consumer)
    {
        // TODO: resource validation similar to [neo]forge

        for(var providerType : ProviderType.providerTypes())
        {
            if(!providerType.hasListeners(ownerId))
                continue;

            consumer.accept((output, registries) -> new DataProviderWrapper<>(ownerId, output, registries, providerType));
        }
    }

    private static OptionalLike<PackOutput> parsePlatformDir()
    {
        var str = System.getProperty(KEY_PLATFORM_DIR);

        if(str == null || str.isEmpty() || str.isBlank())
            return OptionalLike.empty();

        try
        {
            var platformPath = Paths.get(str);
            return OptionalLike.of(new PackOutput(platformPath));
        }
        catch(Throwable t)
        {
            return OptionalLike.empty();
        }
    }
}
