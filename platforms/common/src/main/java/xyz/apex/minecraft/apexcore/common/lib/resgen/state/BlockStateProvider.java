package xyz.apex.minecraft.apexcore.common.lib.resgen.state;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderType;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class BlockStateProvider implements DataProvider
{
    public static final ProviderType<BlockStateProvider> PROVIDER_TYPE = ProviderType.register(new ResourceLocation(ApexCore.ID, "block_states"), BlockStateProvider::new);

    private final ProviderType.ProviderContext context;
    private final List<BlockStateGenerator> generators = Lists.newLinkedList();
    private final Set<ResourceLocation> seenBlocks = Sets.newHashSet();

    private BlockStateProvider(ProviderType.ProviderContext context)
    {
        this.context = context;
    }

    public BlockStateProvider add(BlockStateGenerator generator)
    {
        generators.add(generator);
        return this;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output)
    {
        seenBlocks.clear();

        return CompletableFuture.allOf(generators
                .stream()
                .map(generator -> generate(output, generator))
                .toArray(CompletableFuture[]::new)
        );
    }

    private CompletableFuture<?> generate(CachedOutput output, BlockStateGenerator generator)
    {
        var blockName = BuiltInRegistries.BLOCK.getKey(generator.block());

        if(!seenBlocks.add(blockName))
            return CompletableFuture.completedFuture(null);

        var path = context
                .packOutput()
                .getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(blockName.getNamespace())
                .resolve("blockstates")
                .resolve("%s.json".formatted(blockName.getPath()));

        return DataProvider.saveStable(output, generator.toJson(), path);
    }

    @Override
    public String getName()
    {
        return "BlockStates";
    }
}
