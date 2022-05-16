package xyz.apex.forge.apexcore.core.init;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.core.block.PlayerPlushieBlock;
import xyz.apex.forge.apexcore.lib.block.BlockHelper;
import xyz.apex.forge.apexcore.lib.item.WearableBlockItem;
import xyz.apex.forge.apexcore.lib.util.EventBusHelper;
import xyz.apex.forge.utility.registrator.entry.BlockEntry;
import xyz.apex.forge.utility.registrator.entry.ItemEntry;

import static xyz.apex.forge.utility.registrator.provider.RegistrateLangExtProvider.EN_GB;

public final class PlayerPlushie
{
	private static final ACRegistry REGISTRY = ACRegistry.getRegistry();

	public static final BlockEntry<PlayerPlushieBlock> PLAYER_PLUSHIE_BLOCK = playerPlushie();
	public static final ItemEntry<WearableBlockItem> PLAYER_PLUSHIE_BLOCK_ITEM = ItemEntry.cast(PLAYER_PLUSHIE_BLOCK.getSibling(Item.class));

	public static final ResourceLocation PLAYER_PLUSHIE_ITEM_PROPERTY = REGISTRY.id("player_plushie");

	static void bootstrap()
	{
		EventBusHelper.addEnqueuedListener(FMLClientSetupEvent.class, event -> {
			ItemModelsProperties.register(PLAYER_PLUSHIE_BLOCK_ITEM.asItem(), PLAYER_PLUSHIE_ITEM_PROPERTY, (stack, level, holder) -> {
				CompoundNBT stackTag = stack.getTag();

				if(stackTag != null && stackTag.contains(PlayerPlushieBlock.NBT_PLAYER_INDEX, Constants.NBT.TAG_ANY_NUMERIC))
					return stackTag.getInt(PlayerPlushieBlock.NBT_PLAYER_INDEX);

				return PlayerPlushieBlock.Player.APEX.ordinal();
			});
		});
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

				.blockState((ctx, provider) -> {
					provider.horizontalBlock(ctx.get(), blockState -> {
						PlayerPlushieBlock.Player player = blockState.getValue(PlayerPlushieBlock.PLAYER);
						String serializedName = player.getSerializedName();

						return provider.models()
						               .withExistingParent(ctx.getName() + "/" + serializedName, new ResourceLocation(ApexCore.ID, "block/player_plushie"))
						               .texture("player_plushie", "models/player_plushie/" + serializedName)
								;

					}, 180);
				})

				.isValidSpawn(BlockHelper::never)
				.isRedstoneConductor(BlockHelper::never)
				.isSuffocating(BlockHelper::never)
				.isViewBlocking(BlockHelper::never)

				.addRenderType(() -> RenderType::cutout)

				.item((block, properties) -> new WearableBlockItem(block, properties, EquipmentSlotType.HEAD))
					.model((ctx, provider) -> {
						ItemModelBuilder builder = provider.withExistingParent(ctx.getName(), new ResourceLocation(ApexCore.ID, "block/player_plushie"));
						ResourceLocation id = ctx.getId();

						for(PlayerPlushieBlock.Player player : PlayerPlushieBlock.PLAYER.getPossibleValues())
						{
							builder.override()
							       .predicate(PLAYER_PLUSHIE_ITEM_PROPERTY, player.ordinal())
							       .model(provider.getExistingFile(new ResourceLocation(id.getNamespace(), id.getPath() + "/" + player.getSerializedName())))
					       .end();
						}
					})
					.itemGroup(() -> ItemGroup.TAB_MISC)
				.build()

		.register();
	}
}
