package xyz.apex.minecraft.apexcore.common.lib.resgen.loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.function.Function;

public sealed interface LootType<T extends LootTableSubProvider> permits LootTableProvider.LootTypeImpl
{
    ResourceLocation lootTypeName();

    LootContextParamSet paramSet();

    T createSubProvider(LootTableProvider provider);

    static <T extends LootTableSubProvider> LootType<T> register(LootContextParamSet paramSet, Function<LootTableProvider, T> subProviderFactory)
    {
        return LootTableProvider.registerLootType(paramSet, subProviderFactory);
    }
}
