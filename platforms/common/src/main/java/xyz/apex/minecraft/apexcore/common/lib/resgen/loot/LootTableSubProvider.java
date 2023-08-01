package xyz.apex.minecraft.apexcore.common.lib.resgen.loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface LootTableSubProvider
{
    void generate(String ownerId, BiConsumer<ResourceLocation, LootTable.Builder> consumer);

    static LootTable.Builder noDrop()
    {
        return LootTable.lootTable();
    }
}
