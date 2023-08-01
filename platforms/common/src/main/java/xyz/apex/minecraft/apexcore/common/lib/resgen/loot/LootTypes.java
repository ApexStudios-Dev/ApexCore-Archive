package xyz.apex.minecraft.apexcore.common.lib.resgen.loot;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

@ApiStatus.NonExtendable
public interface LootTypes
{
    LootType<BlockLootProvider> BLOCKS = register(LootContextParamSets.BLOCK, BlockLootProvider::new);
    LootType<EntityLootProvider> ENTITIES = register(LootContextParamSets.ENTITY, EntityLootProvider::new);

    static <T extends LootTableSubProvider> LootType<T> register(LootContextParamSet paramSet, Function<LootTableProvider, T> subProviderFactory)
    {
        return LootType.register(paramSet, subProviderFactory);
    }

    @ApiStatus.Internal
    @DoNotCall
    static void bootstrap()
    {
    }
}
