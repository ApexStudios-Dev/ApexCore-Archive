package xyz.apex.minecraft.apexcore.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

public abstract class BlockLootTableProvider extends FabricBlockLootTableProvider
{
    protected BlockLootTableProvider(FabricDataOutput dataOutput)
    {
        super(dataOutput);
    }

    @Override public abstract void generate();

    @Override
    public final void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer)
    {
        generate(consumer);
    }
}
