package xyz.apex.minecraft.apexcore.common.component.entity.types;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.entity.BaseBlockEntityComponent;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentType;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentTypes;

public final class LootableContainerBlockEntityComponent extends BaseBlockEntityComponent
{
    public static final BlockEntityComponentType<LootableContainerBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(
            new ResourceLocation(ApexCore.ID, "lootable"),
            LootableContainerBlockEntityComponent::new
    );

    private static final String LOOT_TABLE_TAG = RandomizableContainerBlockEntity.LOOT_TABLE_TAG;
    private static final String LOOT_TABLE_SEED_TAG = RandomizableContainerBlockEntity.LOOT_TABLE_SEED_TAG;

    @Nullable private ResourceLocation lootTable;
    private long lootTableSeed = 0L;

    private LootableContainerBlockEntityComponent(BlockEntityComponentHolder holder)
    {
        super(holder);
    }

    @Override
    public void onRegistered(BlockEntityComponentHolder.Registrar registrar)
    {
        registrar.register(BlockEntityComponentTypes.CONTAINER);
    }

    public LootableContainerBlockEntityComponent withLootTable(ResourceLocation lootTable, long lootTableSeed)
    {
        this.lootTable = lootTable;
        this.lootTableSeed = lootTableSeed;
        return this;
    }

    public LootableContainerBlockEntityComponent withLootTable(ResourceLocation lootTable, RandomSource rng)
    {
        return withLootTable(lootTable, rng.nextLong());
    }

    public LootableContainerBlockEntityComponent withLootTable(ResourceLocation lootTable)
    {
        return withLootTable(lootTable, 0L);
    }

    public LootableContainerBlockEntityComponent clearLootTable()
    {
        lootTable = null;
        lootTableSeed = 0L;
        return this;
    }

    public long getLootTableSeed()
    {
        return lootTableSeed;
    }

    @Nullable
    public ResourceLocation getLootTable()
    {
        return lootTable;
    }

    public void unpackLootTable(@Nullable Player player)
    {
        var container = getRequiredComponent(BlockEntityComponentTypes.CONTAINER);
        // stack overflow
        // if(!container.isEmpty()) return; // only unpack if container is empty
        var items = container.getItems();
        if(items.isEmpty() || items.stream().allMatch(ItemStack::isEmpty)) return;

        if(!(getLevel() instanceof ServerLevel serverLevel)) return;
        if(lootTable == null) return;
        if(player instanceof ServerPlayer serverPlayer) CriteriaTriggers.GENERATE_LOOT.trigger(serverPlayer, lootTable);

        var lootTable = serverLevel.getServer().getLootTables().get(this.lootTable);

        var builder = new LootContext.Builder(serverLevel)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(toBlockPos()))
                .withOptionalRandomSeed(lootTableSeed);

        if(player != null) builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
        lootTable.fill(container, builder.create(LootContextParamSets.CHEST));

        clearLootTable();
    }

    @Override
    public Tag writeNbt()
    {
        // TODO: add way to lookup vanilla nbt location, and read that instead, if it exists
        if(lootTable == null) return null;
        var compoundTag = new CompoundTag();
        compoundTag.putString(LOOT_TABLE_TAG, lootTable.toString());
        if(lootTableSeed != 0L) compoundTag.putLong(LOOT_TABLE_SEED_TAG, lootTableSeed);
        return compoundTag;
    }

    @Override
    public void readNbt(Tag nbt)
    {
        if(!(nbt instanceof CompoundTag tagCompound)) return;

        if(!tagCompound.contains(LOOT_TABLE_TAG, Tag.TAG_STRING)) return;
        lootTable = new ResourceLocation(tagCompound.getString(LOOT_TABLE_TAG));

        if(tagCompound.contains(LOOT_TABLE_SEED_TAG, Tag.TAG_LONG)) lootTableSeed = tagCompound.getLong(LOOT_TABLE_SEED_TAG);
        else runForLevel(level -> lootTableSeed = level.random.nextLong());
    }
}
