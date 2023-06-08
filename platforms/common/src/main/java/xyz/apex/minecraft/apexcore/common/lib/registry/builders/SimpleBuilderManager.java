package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import xyz.apex.minecraft.apexcore.common.lib.enchantment.SimpleEnchantment;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.BlockEntityFactory;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.BlockFactory;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.EnchantmentFactory;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.ItemFactory;

public non-sealed class SimpleBuilderManager<M extends SimpleBuilderManager<M>> implements BuilderManager<M>
{
    private final RegistrarManager registrarManager;

    protected SimpleBuilderManager(RegistrarManager registrarManager)
    {
        this.registrarManager = registrarManager;
    }

    @Override
    public final RegistrarManager getRegistrarManager()
    {
        return registrarManager;
    }

    @Override
    public final String getOwnerId()
    {
        return registrarManager.getOwnerId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final M self()
    {
        return (M) this;
    }

    // region: Item
    @Override
    public final <P, T extends Item> ItemBuilder<P, T, M> item(P parent, String registrationName, ItemFactory<T> itemFactory)
    {
        return new ItemBuilder<>(parent, self(), registrationName, itemFactory);
    }

    @Override
    public final <P> ItemBuilder<P, Item, M> item(P parent, String registrationName)
    {
        return item(parent, registrationName, Item::new);
    }

    @Override
    public final <T extends Item> ItemBuilder<M, T, M> item(String registrationName, ItemFactory<T> itemFactory)
    {
        return item(self(), registrationName, itemFactory);
    }

    @Override
    public final ItemBuilder<M, Item, M> item(String registrationName)
    {
        return item(self(), registrationName, Item::new);
    }
    // endregion

    // region: Block
    @Override
    public final <P, T extends Block> BlockBuilder<P, T, M> block(P parent, String registrationName, BlockFactory<T> blockFactory)
    {
        return new BlockBuilder<>(parent, self(), registrationName, blockFactory);
    }

    @Override
    public final <P> BlockBuilder<P, Block, M> block(P parent, String registrationName)
    {
        return block(parent, registrationName, Block::new);
    }

    @Override
    public final <T extends Block> BlockBuilder<M, T, M> block(String registrationName, BlockFactory<T> blockFactory)
    {
        return block(self(), registrationName, blockFactory);
    }

    @Override
    public final BlockBuilder<M, Block, M> block(String registrationName)
    {
        return block(self(), registrationName, Block::new);
    }
    // endregion

    // region: EntityType
    @Override
    public final <P, T extends Entity> EntityBuilder<P, T, M> entity(P parent, String registrationName, EntityType.EntityFactory<T> entityFactory)
    {
        return new EntityBuilder<>(parent, self(), registrationName, entityFactory);
    }

    @Override
    public final <T extends Entity> EntityBuilder<M, T, M> entity(String registrationName, EntityType.EntityFactory<T> entityFactory)
    {
        return entity(self(), registrationName, entityFactory);
    }
    // endregion

    // region: BlockEntityType
    @Override
    public final <P, T extends BlockEntity> BlockEntityBuilder<P, T, M> blockEntity(P parent, String registrationName, BlockEntityFactory<T> entityFactory)
    {
        return new BlockEntityBuilder<>(parent, self(), registrationName, entityFactory);
    }

    @Override
    public final <T extends BlockEntity> BlockEntityBuilder<M, T, M> blockEntity(String registrationName, BlockEntityFactory<T> entityFactory)
    {
        return blockEntity(self(), registrationName, entityFactory);
    }
    // endregion

    // region: Enchantment
    @Override
    public final <P, T extends Enchantment> EnchantmentBuilder<P, T, M> enchantment(P parent, String registrationName, EnchantmentCategory enchantmentCategory, EnchantmentFactory<T> enchantmentFactory)
    {
        return new EnchantmentBuilder<>(parent, self(), registrationName, enchantmentCategory, enchantmentFactory);
    }

    @Override
    public <P> EnchantmentBuilder<P, SimpleEnchantment, M> enchantment(P parent, String registrationName, EnchantmentCategory enchantmentCategory)
    {
        return enchantment(parent, registrationName, enchantmentCategory, SimpleEnchantment::new);
    }

    @Override
    public final <T extends Enchantment> EnchantmentBuilder<M, T, M> enchantment(String registrationName, EnchantmentCategory enchantmentCategory, EnchantmentFactory<T> enchantmentFactory)
    {
        return enchantment(self(), registrationName, enchantmentCategory, enchantmentFactory);
    }

    @Override
    public EnchantmentBuilder<M, SimpleEnchantment, M> enchantment(String registrationName, EnchantmentCategory enchantmentCategory)
    {
        return enchantment(self(), registrationName, enchantmentCategory, SimpleEnchantment::new);
    }
    // endregion

    // region: CreativeModeTab
    @Override
    public <P> CreativeModeTabBuilder<P, M> creativeModeTab(P parent, String registrationName)
    {
        return new CreativeModeTabBuilder<>(parent, self(), registrationName);
    }

    @Override
    public CreativeModeTabBuilder<M, M> creativeModeTab(String registrationName)
    {
        return creativeModeTab(self(), registrationName);
    }
    // endregion
}
