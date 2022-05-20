package xyz.apex.forge.apexcore.core.init;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.DatagenModLoader;

import xyz.apex.forge.apexcore.core.block.PlayerPlushieBlock;
import xyz.apex.forge.apexcore.core.block.entity.PlayerPlushieBlockEntity;
import xyz.apex.forge.apexcore.core.client.renderer.ApexCoreItemStackBlockEntityRenderer;
import xyz.apex.forge.apexcore.core.client.renderer.PlayerPlushieBlockEntityRenderer;
import xyz.apex.forge.apexcore.lib.block.BlockHelper;
import xyz.apex.forge.apexcore.lib.item.WearableBlockItem;
import xyz.apex.forge.apexcore.lib.support.SupporterManager;
import xyz.apex.forge.utility.registrator.entry.BlockEntityEntry;
import xyz.apex.forge.utility.registrator.entry.BlockEntry;
import xyz.apex.forge.utility.registrator.entry.ItemEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static xyz.apex.forge.utility.registrator.provider.RegistrateLangExtProvider.EN_GB;

public final class PlayerPlushie
{
	private static final ACRegistry REGISTRY = ACRegistry.getRegistry();

	public static final BlockEntry<PlayerPlushieBlock> PLAYER_PLUSHIE_BLOCK = playerPlushie();
	public static final ItemEntry<BlockItem> PLAYER_PLUSHIE_BLOCK_ITEM = ItemEntry.cast(PLAYER_PLUSHIE_BLOCK.getSibling(Item.class));
	public static final BlockEntityEntry<PlayerPlushieBlockEntity> PLAYER_PLUSHIE_BLOCK_ENTITY = BlockEntityEntry.cast(PLAYER_PLUSHIE_BLOCK.getSibling(TileEntityType.class));

	private static final String NBT_SUPPORTER_DATA = "SupporterData";
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

				.isValidSpawn(BlockHelper::never)
				.isRedstoneConductor(BlockHelper::never)
				.isSuffocating(BlockHelper::never)
				.isViewBlocking(BlockHelper::never)

				.addRenderType(() -> RenderType::cutout)

				.item((block, properties) -> new WearableBlockItem(block, properties, EquipmentSlotType.HEAD))
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
					.itemGroup(() -> ItemGroup.TAB_MISC)
					.properties(properties -> properties.setISTER(() -> DatagenModLoader.isRunningDataGen() ? (() -> null) : ApexCoreItemStackBlockEntityRenderer::new))
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
		ItemStack stack = PLAYER_PLUSHIE_BLOCK.asItemStack(stackSize);

		CompoundNBT stackTag = stack.getOrCreateTag();

		Set<UUID> aliases = info.getAliases();
		SupporterManager.SupporterLevel level = info.getLevel();

		CompoundNBT supporterTag = new CompoundNBT();
		supporterTag.putUUID(NBT_PLAYER_ID, info.getPlayerId());
		supporterTag.putString(NBT_SUPPORTER_LEVEL, level.getSerializedName());
		supporterTag.putString(NBT_USERNAME, info.getUsername());

		if(!aliases.isEmpty())
		{
			ListNBT aliasesTag = new ListNBT();
			aliases.forEach(alias -> aliasesTag.add(NBTUtil.createUUID(alias)));
			supporterTag.put(NBT_PLAYER_ALIASES, aliasesTag);
		}

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
		CompoundNBT stackTag = stack.getTag();

		if(stackTag != null && stackTag.contains(NBT_SUPPORTER_DATA, Constants.NBT.TAG_COMPOUND))
		{
			CompoundNBT supporterTag = stackTag.getCompound(NBT_SUPPORTER_DATA);
			Set<SupporterManager.SupporterInfo> supporters = SupporterManager.getSupporters();

			if(supporterTag.contains(NBT_PLAYER_ID, Constants.NBT.TAG_INT_ARRAY))
			{
				UUID playerId = supporterTag.getUUID(NBT_PLAYER_ID);

				for(SupporterManager.SupporterInfo info : supporters)
				{
					if(info.isFor(playerId))
						return info;
				}
			}

			if(supporterTag.contains(NBT_PLAYER_ALIASES, Constants.NBT.TAG_LIST))
			{
				ListNBT aliasesTag = supporterTag.getList(NBT_PLAYER_ALIASES, Constants.NBT.TAG_INT_ARRAY);

				for(INBT inbt : aliasesTag)
				{
					UUID playerId = NBTUtil.loadUUID(inbt);

					for(SupporterManager.SupporterInfo info : supporters)
					{
						if(info.isFor(playerId))
							return info;
					}
				}
			}
		}

		return null;
	}
}
