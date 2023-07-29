package xyz.apex.minecraft.apexcore.common.lib.resgen.lang;

import com.google.common.collect.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class LanguageProvider implements DataProvider
{
    public static final ProviderType<LanguageProvider> PROVIDER_TYPE = ProviderType.register(new ResourceLocation(ApexCore.ID, "languages"), LanguageProvider::new);

    private final ProviderType.ProviderContext context;
    private final Map<String, LanguageBuilder> regions = Maps.newHashMap();
    private final Multimap<String, String> copyMap = MultimapBuilder.hashKeys().hashSetValues().build();

    private LanguageProvider(ProviderType.ProviderContext context)
    {
        this.context = context;
    }

    public LanguageBuilder region(String region)
    {
        return regions.computeIfAbsent(region, r -> new LanguageBuilder(this, r));
    }

    public LanguageBuilder enUS()
    {
        return region("en_us").copyTo("en_gb");
    }

    public LanguageBuilder enGB()
    {
        return region("en_gb").copyFrom("en_us");
    }

    public LanguageProvider copy(String sourceRegion, String targetRegion)
    {
        copyMap.put(targetRegion, sourceRegion);
        return this;
    }

    private List<String> inGenerationOrder()
    {
        var regions = Sets.newHashSet(this.regions.keySet());
        regions.addAll(copyMap.keys());

        var toSort = Lists.newArrayList(regions);
        toSort.sort(Comparator.<String>comparingInt(region -> copyMap.get(region).size()).reversed());
        return toSort;
    }

    private JsonElement toJson(String region)
    {
        var json = new JsonObject();

        for(var key : getKeys(region, Sets.newHashSet()))
        {
            var value = getTranslation(region, key, Sets.newHashSet());

            if(value != null && !value.isBlank())
                json.addProperty(key, value);
        }

        return json.size() == 0 ? JsonNull.INSTANCE : json;
    }

    private Set<String> getKeys(String region, Set<String> visitedRegions)
    {
        var keys = Sets.<String>newHashSet();
        var builder = regions.get(region);
        visitedRegions.add(region);

        if(builder != null)
            keys.addAll(builder.keys());

        var sourceRegions = copyMap.get(region);

        if(!sourceRegions.isEmpty())
            sourceRegions.stream().filter(visitedRegions::add).map(sourceRegion -> getKeys(sourceRegion, visitedRegions)).forEach(keys::addAll);

        return keys;
    }

    @Nullable
    private String getTranslation(String region, String key, Set<String> visitedRegions)
    {
        var builder = regions.get(region);
        visitedRegions.add(region);

        if(builder != null)
        {
            var value = builder.get(key);

            if(value != null && !value.isBlank())
                return value;
        }

        var sourceRegions = copyMap.get(region);

        if(!sourceRegions.isEmpty())
        {
            for(var sourceRegion : sourceRegions)
            {
                if(visitedRegions.add(sourceRegion))
                {
                    var value = getTranslation(sourceRegion, key, visitedRegions);

                    if(value != null && !value.isBlank())
                        return value;
                }
            }
        }

        return null;
    }

    private CompletableFuture<?> generate(CachedOutput cache, String region)
    {
        return CompletableFuture.supplyAsync(() -> DataProvider.saveStable(
                cache,
                toJson(region),
                context.packOutput()
                       .getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                      .resolve(context.ownerId())
                      .resolve("lang")
                      .resolve("%s.json".formatted(region))
        ));
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
        return CompletableFuture.allOf(inGenerationOrder()
                .stream()
                .map(region -> generate(cache, region))
                .toArray(CompletableFuture[]::new)
        );
    }

    @Override
    public String getName()
    {
        return "language";
    }

    public static String toEnglishName(String registryName)
    {
        return Stream.of(registryName.split("_")).map(StringUtils::capitalize).collect(Collectors.joining(" "));
    }

    public static String toEnglishName(ResourceLocation registryName)
    {
        return toEnglishName(registryName.getPath());
    }
}
