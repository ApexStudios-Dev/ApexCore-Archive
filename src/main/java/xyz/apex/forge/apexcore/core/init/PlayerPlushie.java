package xyz.apex.forge.apexcore.core.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

import xyz.apex.forge.apexcore.core.block.PlayerPlushieBlock;
import xyz.apex.forge.apexcore.core.block.entity.PlayerPlushieBlockEntity;
import xyz.apex.forge.apexcore.core.client.renderer.PlayerPlushieBlockEntityRenderer;
import xyz.apex.forge.apexcore.core.item.PlayerPlushieBlockItem;
import xyz.apex.forge.apexcore.lib.block.BlockHelper;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;
import xyz.apex.forge.utility.registrator.entry.BlockEntityEntry;
import xyz.apex.forge.utility.registrator.entry.BlockEntry;
import xyz.apex.forge.utility.registrator.entry.ItemEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

import static xyz.apex.forge.utility.registrator.provider.RegistrateLangExtProvider.EN_GB;

public final class PlayerPlushie
{
	private static final ACRegistry REGISTRY = ACRegistry.getRegistry();

	public static final BlockEntry<PlayerPlushieBlock> PLAYER_PLUSHIE_BLOCK = playerPlushie();
	public static final ItemEntry<BlockItem> PLAYER_PLUSHIE_BLOCK_ITEM = ItemEntry.cast(PLAYER_PLUSHIE_BLOCK.getSibling(Item.class));
	public static final BlockEntityEntry<PlayerPlushieBlockEntity> PLAYER_PLUSHIE_BLOCK_ENTITY = BlockEntityEntry.cast(PLAYER_PLUSHIE_BLOCK.getSibling(BlockEntityType.class));

	public static final String NBT_SUPPORTER_DATA = "SupporterData";
	private static final String NBT_PLAYER_ID = "UUID";
	private static final String NBT_PLAYER_ALIASES = "Aliases";
	private static final String NBT_SUPPORTER_LEVEL = "Level";
	private static final String NBT_USERNAME = "Username";

	static void bootstrap()
	{
	}

	private static BlockEntry<PlayerPlushieBlock> playerPlushie()
	{
		return REGISTRY
				.block("player_plushie", PlayerPlushieBlock::new)

				.lang("Player Plushie")
				.lang(EN_GB, "Player Plushie")

				.initialProperties(Material.WOOL)
				.strength(.8F)
				.sound(SoundType.WOOL)
				.noOcclusion()

				.blockState((ctx, provider) -> provider
						.getVariantBuilder(ctx.get())
						.forAllStates(blockState -> ConfiguredModel
								.builder()
								.modelFile(provider
										.models()
										.getBuilder(ctx.getName())
										.texture("particle", "minecraft:block/white_wool")
								)
								.build()
						)
				)

				.loot((lootTables, block) -> lootTables
						.add(block, LootTable
								.lootTable()
								.withPool(BlockLoot
										.applyExplosionCondition(block, LootPool
												.lootPool()
												.setRolls(ConstantValue.exactly(1))
												.add(LootItem.lootTableItem(block))
												.apply(CopyNbtFunction
														.copyData(ContextNbtProvider.BLOCK_ENTITY)
														.copy(NBT_SUPPORTER_DATA, NBT_SUPPORTER_DATA)
												)
										)
								)
						)
				)

				.isValidSpawn(BlockHelper::never)
				.isRedstoneConductor(BlockHelper::never)
				.isSuffocating(BlockHelper::never)
				.isViewBlocking(BlockHelper::never)

				.addRenderType(() -> RenderType::cutout)

				.item(PlayerPlushieBlockItem::new)
					.model((ctx, provider) -> {
						ModelFile.UncheckedModelFile builtInEntity = new ModelFile.UncheckedModelFile("minecraft:builtin/entity");

						provider.getBuilder(ctx.getName())
						        .parent(builtInEntity)
								.transforms()
									.transform(ModelBuilder.Perspective.THIRDPERSON_RIGHT)
										.rotation(75F, 45F, 0F)
										.translation(0F, 2.5F, 0F)
										.scale(.375F, .375F, .375F)
									.end()
									.transform(ModelBuilder.Perspective.THIRDPERSON_LEFT)
										.rotation(75F, 45F, 0F)
										.translation(0F, 2.5F, 0F)
										.scale(.375F, .375F, .375F)
									.end()
									.transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
										.rotation(0F, 135F, 0F)
										.translation(0F, 4F, 0F)
										.scale(.4F, .4F, .4F)
									.end()
									.transform(ModelBuilder.Perspective.FIRSTPERSON_LEFT)
										.rotation(0F, 135F, 0F)
										.translation(0F, 4F, 0F)
										.scale(.4F, .4F, .4F)
									.end()
									.transform(ModelBuilder.Perspective.HEAD)
										.rotation(0F, 0F, 0F)
										.translation(0F, 14.5F, 0F)
										.scale(1F, 1F, 1F)
									.end()
									.transform(ModelBuilder.Perspective.GROUND)
										.rotation(0F, 0F, 0F)
										.translation(0F, 3F, 0F)
										.scale(.25F, .25F, .25F)
									.end()
									.transform(ModelBuilder.Perspective.FIXED)
										.rotation(-90F, 0F, 0F)
										.translation(0F, 0F, -8F)
										.scale(1F, 1F, 1F)
									.end()
									.transform(ModelBuilder.Perspective.GUI)
										.rotation(30F, -135F, 0F)
										.translation(0F, 0F, 0F)
										.scale(.625F, .625F, .625F)
									.end()
								.end();
					})
					.tab(() -> CreativeModeTab.TAB_MISC)
				.build()

				.blockEntity(PlayerPlushieBlockEntity::new)
					.renderer(() -> PlayerPlushieBlockEntityRenderer::new)
				.build()

		.register();
	}

	public static List<ItemStack> getPlushieItems()
	{
		return SupporterManager.getSupporters().stream().map(PlayerPlushie::getPlushieItem).collect(Collectors.toList());
	}

	public static ItemStack getPlushieItem(SupporterManager.SupporterInfo info, int stackSize)
	{
		var stack = PLAYER_PLUSHIE_BLOCK.asItemStack(stackSize);

		var stackTag = stack.getOrCreateTag();
		var supporterTag = writeSupporterInfoTag(info);
		stackTag.put(NBT_SUPPORTER_DATA, supporterTag);

		return stack;
	}

	public static ItemStack getPlushieItem(SupporterManager.SupporterInfo info)
	{
		return getPlushieItem(info, 1);
	}

	@Nullable
	public static SupporterManager.SupporterInfo getSupporterInfo(ItemStack stack)
	{
		var stackTag = stack.getTag();

		if(stackTag != null && stackTag.contains(NBT_SUPPORTER_DATA, Tag.TAG_COMPOUND))
		{
			var supporterTag = stackTag.getCompound(NBT_SUPPORTER_DATA);
			return getSupporterInfo(supporterTag);
		}

		return null;
	}

	@Nullable
	public static SupporterManager.SupporterInfo getSupporterInfo(CompoundTag supporterTag)
	{
		var supporters = SupporterManager.getSupporters();

		if(supporterTag.contains(NBT_PLAYER_ID, Tag.TAG_INT_ARRAY))
		{
			var playerId = supporterTag.getUUID(NBT_PLAYER_ID);

			for(var info : supporters)
			{
				if(info.isFor(playerId))
					return info;
			}
		}

		if(supporterTag.contains(NBT_PLAYER_ALIASES, Tag.TAG_LIST))
		{
			var aliasesTag = supporterTag.getList(NBT_PLAYER_ALIASES, Tag.TAG_INT_ARRAY);

			for(var inbt : aliasesTag)
			{
				var playerId = NbtUtils.loadUUID(inbt);

				for(SupporterManager.SupporterInfo info : supporters)
				{
					if(info.isFor(playerId))
						return info;
				}
			}
		}

		return null;
	}

	public static CompoundTag writeSupporterInfoTag(SupporterManager.SupporterInfo info)
	{
		var aliases = info.getAliases();
		var level = info.getLevel();

		var supporterTag = new CompoundTag();
		supporterTag.putUUID(NBT_PLAYER_ID, info.getPlayerId());
		supporterTag.putString(NBT_SUPPORTER_LEVEL, level.getSerializedName());
		supporterTag.putString(NBT_USERNAME, info.getUsername());

		if(!aliases.isEmpty())
		{
			var aliasesTag = new ListTag();
			aliases.forEach(alias -> aliasesTag.add(NbtUtils.createUUID(alias)));
			supporterTag.put(NBT_PLAYER_ALIASES, aliasesTag);
		}

		return supporterTag;
	}
}
