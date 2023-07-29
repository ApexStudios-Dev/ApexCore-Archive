package xyz.apex.minecraft.apexcore.common.lib.resgen;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class AdvancementProvider implements DataProvider
{
    public static final ProviderType<AdvancementProvider> PROVIDER_TYPE = ProviderType.register(new ResourceLocation(ApexCore.ID, "advancements"), AdvancementProvider::new);

    private final ProviderType.ProviderContext context;
    private final List<Advancement> advancements = Lists.newArrayList();
    private AdvancementProvider(ProviderType.ProviderContext context)
    {
        this.context = context;
    }

    public AdvancementProvider add(Advancement advancement)
    {
        advancements.add(advancement);
        return this;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache)
    {
        var pathProvider = context.packOutput().createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        var futures = Lists.<CompletableFuture<?>>newArrayList();
        var seen = Sets.<ResourceLocation>newHashSet();

        advancements.forEach(advancement -> {
            var advancementId = advancement.getId();

            if(!seen.add(advancementId))
                throw new IllegalStateException("Duplicate advancement: %s".formatted(advancementId));

            var path = pathProvider.json(advancementId);
            futures.add(DataProvider.saveStable(cache, advancement.deconstruct().serializeToJson(), path));
        });

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName()
    {
        return "Advancements";
    }
}
