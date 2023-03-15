package xyz.apex.forge.apexcore.registrate;

import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import xyz.apex.forge.apexcore.registrate.builder.*;
import xyz.apex.forge.apexcore.registrate.builder.factory.*;
import xyz.apex.forge.apexcore.registrate.entry.MenuEntry;
import xyz.apex.forge.apexcore.registrate.holder.*;

public class AbstractRegistrate<OWNER extends AbstractRegistrate<OWNER>> extends CoreRegistrate<OWNER> implements
		ItemHolder<OWNER>,
		BlockHolder<OWNER>,
		MenuHolder<OWNER>,
		BlockEntityHolder<OWNER>,
		EnchantmentHolder<OWNER>,
		EntityHolder<OWNER>,
		FluidHolder<OWNER>
{
	protected AbstractRegistrate(String modId)
	{
		super(modId);
	}

	// region: Block
	@Override
	public final BlockBuilder<OWNER, Block, OWNER> block()
	{
		return BlockEntityHolder.super.block();
	}

	@Override
	public final BlockBuilder<OWNER, Block, OWNER> block(String name)
	{
		return BlockEntityHolder.super.block(name);
	}

	@Override
	public final <PARENT> BlockBuilder<OWNER, Block, PARENT> block(PARENT parent)
	{
		return BlockEntityHolder.super.block(parent);
	}

	@Override
	public final <PARENT> BlockBuilder<OWNER, Block, PARENT> block(PARENT parent, String name)
	{
		return BlockEntityHolder.super.block(parent, name);
	}

	@Override
	public final <BLOCK extends Block> BlockBuilder<OWNER, BLOCK, OWNER> block(BlockFactory<BLOCK> factory)
	{
		return BlockEntityHolder.super.block(factory);
	}

	@Override
	public final <BLOCK extends Block> BlockBuilder<OWNER, BLOCK, OWNER> block(String name, BlockFactory<BLOCK> factory)
	{
		return BlockEntityHolder.super.block(name, factory);
	}

	@Override
	public final <BLOCK extends Block, PARENT> BlockBuilder<OWNER, BLOCK, PARENT> block(PARENT parent, BlockFactory<BLOCK> factory)
	{
		return BlockEntityHolder.super.block(parent, factory);
	}

	@Override
	public final <BLOCK extends Block, PARENT> BlockBuilder<OWNER, BLOCK, PARENT> block(PARENT parent, String name, BlockFactory<BLOCK> factory)
	{
		return BlockEntityHolder.super.block(parent, name, factory);
	}
	// endregion

	// region: Item
	@Override
	public final ItemBuilder<OWNER, Item, OWNER> item()
	{
		return BlockEntityHolder.super.item();
	}

	@Override
	public final ItemBuilder<OWNER, Item, OWNER> item(String name)
	{
		return BlockEntityHolder.super.item(name);
	}

	@Override
	public final <PARENT> ItemBuilder<OWNER, Item, PARENT> item(PARENT parent)
	{
		return BlockEntityHolder.super.item(parent);
	}

	@Override
	public final <PARENT> ItemBuilder<OWNER, Item, PARENT> item(PARENT parent, String name)
	{
		return BlockEntityHolder.super.item(parent, name);
	}

	@Override
	public final <ITEM extends Item> ItemBuilder<OWNER, ITEM, OWNER> item(ItemFactory<ITEM> itemFactory)
	{
		return BlockEntityHolder.super.item(itemFactory);
	}

	@Override
	public final <ITEM extends Item> ItemBuilder<OWNER, ITEM, OWNER> item(String name, ItemFactory<ITEM> itemFactory)
	{
		return BlockEntityHolder.super.item(name, itemFactory);
	}

	@Override
	public final <ITEM extends Item, PARENT> ItemBuilder<OWNER, ITEM, PARENT> item(PARENT parent, ItemFactory<ITEM> itemFactory)
	{
		return BlockEntityHolder.super.item(parent, itemFactory);
	}

	@Override
	public final <ITEM extends Item, PARENT> ItemBuilder<OWNER, ITEM, PARENT> item(PARENT parent, String name, ItemFactory<ITEM> itemFactory)
	{
		return BlockEntityHolder.super.item(parent, name, itemFactory);
	}
	// endregion

	// region: Menu
	@Override
	public final <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>> MenuEntry<MENU> menu(MenuFactory<MENU> menuFactory, NonNullSupplier<MenuScreenFactory<MENU, SCREEN>> screenFactory)
	{
		return MenuHolder.super.menu(menuFactory, screenFactory);
	}

	@Override
	public final <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>> MenuEntry<MENU> menu(String name, MenuFactory<MENU> menuFactory, NonNullSupplier<MenuScreenFactory<MENU, SCREEN>> screenFactory)
	{
		return MenuHolder.super.menu(name, menuFactory, screenFactory);
	}

	@Override
	public final <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>> MenuEntry<MENU> menu(ForgeMenuFactory<MENU> menuFactory, NonNullSupplier<MenuScreenFactory<MENU, SCREEN>> screenFactory)
	{
		return MenuHolder.super.menu(menuFactory, screenFactory);
	}

	@Override
	public final <MENU extends AbstractContainerMenu, SCREEN extends Screen & MenuAccess<MENU>> MenuEntry<MENU> menu(String name, ForgeMenuFactory<MENU> menuFactory, NonNullSupplier<MenuScreenFactory<MENU, SCREEN>> screenFactory)
	{
		return MenuHolder.super.menu(name, menuFactory, screenFactory);
	}
	// endregion

	// region: BlockEntity
	@Override
	public final <BLOCK_ENTITY extends BlockEntity> BlockEntityBuilder<OWNER, BLOCK_ENTITY, OWNER> blockEntity(BlockEntityFactory<BLOCK_ENTITY> factory)
	{
		return BlockEntityHolder.super.blockEntity(factory);
	}

	@Override
	public final <BLOCK_ENTITY extends BlockEntity> BlockEntityBuilder<OWNER, BLOCK_ENTITY, OWNER> blockEntity(String name, BlockEntityFactory<BLOCK_ENTITY> factory)
	{
		return BlockEntityHolder.super.blockEntity(name, factory);
	}

	@Override
	public final <BLOCK_ENTITY extends BlockEntity, PARENT> BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> blockEntity(PARENT parent, BlockEntityFactory<BLOCK_ENTITY> factory)
	{
		return BlockEntityHolder.super.blockEntity(parent, factory);
	}

	@Override
	public final <BLOCK_ENTITY extends BlockEntity, PARENT> BlockEntityBuilder<OWNER, BLOCK_ENTITY, PARENT> blockEntity(PARENT parent, String name, BlockEntityFactory<BLOCK_ENTITY> factory)
	{
		return BlockEntityHolder.super.blockEntity(parent, name, factory);
	}
	// endregion

	// region: Enchantment
	@Override
	public final <ENCHANTMENT extends Enchantment> EnchantmentBuilder<OWNER, ENCHANTMENT, OWNER> enchantment(EnchantmentFactory<ENCHANTMENT> factory)
	{
		return EnchantmentHolder.super.enchantment(factory);
	}

	@Override
	public final <ENCHANTMENT extends Enchantment> EnchantmentBuilder<OWNER, ENCHANTMENT, OWNER> enchantment(String name, EnchantmentFactory<ENCHANTMENT> factory)
	{
		return EnchantmentHolder.super.enchantment(name, factory);
	}

	@Override
	public final <ENCHANTMENT extends Enchantment, PARENT> EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> enchantment(PARENT parent, EnchantmentFactory<ENCHANTMENT> factory)
	{
		return EnchantmentHolder.super.enchantment(parent, factory);
	}

	@Override
	public final <ENCHANTMENT extends Enchantment, PARENT> EnchantmentBuilder<OWNER, ENCHANTMENT, PARENT> enchantment(PARENT parent, String name, EnchantmentFactory<ENCHANTMENT> factory)
	{
		return EnchantmentHolder.super.enchantment(parent, name, factory);
	}
	// endregion

	// region: Entity
	@Override
	public final <ENTITY extends Entity> EntityBuilder<OWNER, ENTITY, OWNER> entity(EntityFactory<ENTITY> factory)
	{
		return EntityHolder.super.entity(factory);
	}

	@Override
	public final <ENTITY extends Entity> EntityBuilder<OWNER, ENTITY, OWNER> entity(String name, EntityFactory<ENTITY> factory)
	{
		return EntityHolder.super.entity(name, factory);
	}

	@Override
	public final <ENTITY extends Entity, PARENT> EntityBuilder<OWNER, ENTITY, PARENT> entity(PARENT parent, EntityFactory<ENTITY> factory)
	{
		return EntityHolder.super.entity(parent, factory);
	}

	@Override
	public final <ENTITY extends Entity, PARENT> EntityBuilder<OWNER, ENTITY, PARENT> entity(PARENT parent, String name, EntityFactory<ENTITY> factory)
	{
		return EntityHolder.super.entity(parent, name, factory);
	}
	// endregion

	// region: Fluid
	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid()
	{
		return FluidHolder.super.fluid();
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(FluidBuilder.FluidTypeFactory typeFactory)
	{
		return FluidHolder.super.fluid(typeFactory);
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(NonNullSupplier<FluidType> fluidType)
	{
		return FluidHolder.super.fluid(fluidType);
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture)
	{
		return FluidHolder.super.fluid(stillTexture, flowingTexture);
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return FluidHolder.super.fluid(stillTexture, flowingTexture, typeFactory);
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType)
	{
		return FluidHolder.super.fluid(stillTexture, flowingTexture, fluidType);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(stillTexture, flowingTexture, fluidFactory);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(stillTexture, flowingTexture, typeFactory, fluidFactory);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(stillTexture, flowingTexture, fluidType, fluidFactory);
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name)
	{
		return FluidHolder.super.fluid(name);
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return FluidHolder.super.fluid(name, typeFactory);
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name, NonNullSupplier<FluidType> fluidType)
	{
		return FluidHolder.super.fluid(name, fluidType);
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture)
	{
		return FluidHolder.super.fluid(name, stillTexture, flowingTexture);
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return FluidHolder.super.fluid(name, stillTexture, flowingTexture, typeFactory);
	}

	@Override
	public final FluidBuilder<ForgeFlowingFluid.Flowing, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType)
	{
		return FluidHolder.super.fluid(name, stillTexture, flowingTexture, fluidType);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(name, stillTexture, flowingTexture, fluidFactory);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(name, stillTexture, flowingTexture, typeFactory, fluidFactory);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid> FluidBuilder<FLUID, OWNER> fluid(String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(name, stillTexture, flowingTexture, fluidType, fluidFactory);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent)
	{
		return FluidHolder.super.fluid(parent);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return FluidHolder.super.fluid(parent, typeFactory);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, NonNullSupplier<FluidType> fluidType)
	{
		return FluidHolder.super.fluid(parent, fluidType);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture)
	{
		return FluidHolder.super.fluid(parent, stillTexture, flowingTexture);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return FluidHolder.super.fluid(parent, stillTexture, flowingTexture, typeFactory);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType)
	{
		return FluidHolder.super.fluid(parent, stillTexture, flowingTexture, fluidType);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(parent, stillTexture, flowingTexture, fluidFactory);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(parent, stillTexture, flowingTexture, typeFactory, fluidFactory);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(parent, stillTexture, flowingTexture, fluidType, fluidFactory);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name)
	{
		return FluidHolder.super.fluid(parent, name);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return FluidHolder.super.fluid(parent, name, typeFactory);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name, NonNullSupplier<FluidType> fluidType)
	{
		return FluidHolder.super.fluid(parent, name, fluidType);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture)
	{
		return FluidHolder.super.fluid(parent, name, stillTexture, flowingTexture);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory)
	{
		return FluidHolder.super.fluid(parent, name, stillTexture, flowingTexture, typeFactory);
	}

	@Override
	public final <PARENT> FluidBuilder<ForgeFlowingFluid.Flowing, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType)
	{
		return FluidHolder.super.fluid(parent, name, stillTexture, flowingTexture, fluidType);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(parent, name, stillTexture, flowingTexture, fluidFactory);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, FluidBuilder.FluidTypeFactory typeFactory, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(parent, name, stillTexture, flowingTexture, typeFactory, fluidFactory);
	}

	@Override
	public final <FLUID extends ForgeFlowingFluid, PARENT> FluidBuilder<FLUID, PARENT> fluid(PARENT parent, String name, ResourceLocation stillTexture, ResourceLocation flowingTexture, NonNullSupplier<FluidType> fluidType, NonNullFunction<ForgeFlowingFluid.Properties, FLUID> fluidFactory)
	{
		return FluidHolder.super.fluid(parent, name, stillTexture, flowingTexture, fluidType, fluidFactory);
	}
	// endregion
}
