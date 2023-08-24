package xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BaseBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentType;

public final class LootTableBlockEntityComponent extends BaseBlockEntityComponent
{
    public static final BlockEntityComponentType<LootTableBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(ApexCore.ID, "loot_table", LootTableBlockEntityComponent::new);

    private static final String NBT_LOOT_TABLE = RandomizableContainerBlockEntity.LOOT_TABLE_TAG;
    private static final String NBT_LOOT_TABLE_SEED = RandomizableContainerBlockEntity.LOOT_TABLE_SEED_TAG;

    @Nullable private ResourceLocation lootTableId = null;
    private long lootTableSeed = -1L;

    private LootTableBlockEntityComponent(BlockEntityComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    public void setLootTable(ResourceLocation lootTableId, long lootTableSeed)
    {
        this.lootTableId = lootTableId;
        this.lootTableSeed = lootTableSeed;
    }

    public void setLootTable(ResourceLocation lootTableId)
    {
        setLootTable(lootTableId, -1L);
    }

    public void clearLootTable()
    {
        lootTableId = null;
        lootTableSeed = -1L;
    }

    public void unpackLootTable(@Nullable Player player)
    {
        if(lootTableId == null)
            return;

        var blockEntity = getGameObject();

        if(!(blockEntity.getLevel() instanceof ServerLevel level))
            return;

        var lootTable = level.getServer().getLootData().getLootTable(lootTableId);

        if(player instanceof ServerPlayer sPlayer)
            CriteriaTriggers.GENERATE_LOOT.trigger(sPlayer, lootTableId);

        // loot table system references the container
        // if we dont clear the id here, we could cause stack overflow
        // calling this method over and over again
        lootTableId = null;

        var builder = new LootParams.Builder(level)
                .withParameter(LootContextParams.BLOCK_ENTITY, blockEntity)
                .withParameter(LootContextParams.BLOCK_STATE, blockEntity.getBlockState())
                .withParameter(LootContextParams.ORIGIN, blockEntity.getBlockPos().getCenter());

        if(player != null)
            builder = builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);

        componentHolder.clearContent();
        lootTable.fill(componentHolder, builder.create(LootContextParamSets.CHEST), lootTableSeed);

        clearLootTable();
    }

    @Override
    public void serializeInto(CompoundTag tag, boolean forNetwork)
    {
        if(lootTableId == null)
            return;

        tag.putString(NBT_LOOT_TABLE, lootTableId.toString());

        if(lootTableSeed != -1)
            tag.putLong(NBT_LOOT_TABLE_SEED, lootTableSeed);
    }

    @Override
    public void deserializeFrom(CompoundTag tag, boolean fromNetwork)
    {
        clearLootTable();

        if(tag.contains(NBT_LOOT_TABLE, Tag.TAG_STRING))
        {
            lootTableId = new ResourceLocation(tag.getString(NBT_LOOT_TABLE));
            lootTableSeed = tag.contains(NBT_LOOT_TABLE_SEED, Tag.TAG_ANY_NUMERIC) ? tag.getLong(NBT_LOOT_TABLE_SEED) : -1L;
        }
    }

    public static void unpackLootTable(BlockEntity blockEntity, @Nullable Player player)
    {
        if(blockEntity instanceof RandomizableContainerBlockEntity container)
            container.unpackLootTable(player);
        else if(blockEntity instanceof BlockEntityComponentHolder componentHolder)
        {
            var component = componentHolder.getComponent(COMPONENT_TYPE);

            if(component != null)
                component.unpackLootTable(player);
        }
    }
}
