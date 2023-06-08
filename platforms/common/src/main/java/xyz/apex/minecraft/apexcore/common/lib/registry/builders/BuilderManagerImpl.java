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

public non-sealed class BuilderManagerImpl implements BuilderManager
{
    private final RegistrarManager registrarManager;

    protected BuilderManagerImpl(RegistrarManager registrarManager)
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

    // region: Item
    @Override
    public final <P, T extends Item> ItemBuilder<P, T> item(P parent, String registrationName, ItemFactory<T> itemFactory)
    {
        return new ItemBuilder<>(parent, this, registrationName, itemFactory);
    }

    @Override
    public final <P> ItemBuilder<P, Item> item(P parent, String registrationName)
    {
        return item(parent, registrationName, Item::new);
    }

    @Override
    public final <T extends Item> ItemBuilder<BuilderManager, T> item(String registrationName, ItemFactory<T> itemFactory)
    {
        return item(this, registrationName, itemFactory);
    }

    @Override
    public final ItemBuilder<BuilderManager, Item> item(String registrationName)
    {
        return item(this, registrationName, Item::new);
    }
    // endregion

    // region: Block
    @Override
    public final <P, T extends Block> BlockBuilder<P, T> block(P parent, String registrationName, BlockFactory<T> blockFactory)
    {
        return new BlockBuilder<>(parent, this, registrationName, blockFactory);
    }

    @Override
    public final <P> BlockBuilder<P, Block> block(P parent, String registrationName)
    {
        return block(parent, registrationName, Block::new);
    }

    @Override
    public final <T extends Block> BlockBuilder<BuilderManager, T> block(String registrationName, BlockFactory<T> blockFactory)
    {
        return block(this, registrationName, blockFactory);
    }

    @Override
    public final BlockBuilder<BuilderManager, Block> block(String registrationName)
    {
        return block(this, registrationName, Block::new);
    }
    // endregion

    // region: EntityType
    @Override
    public final <P, T extends Entity> EntityBuilder<P, T> entity(P parent, String registrationName, EntityType.EntityFactory<T> entityFactory)
    {
        return new EntityBuilder<>(parent, this, registrationName, entityFactory);
    }

    @Override
    public final <T extends Entity> EntityBuilder<BuilderManager, T> entity(String registrationName, EntityType.EntityFactory<T> entityFactory)
    {
        return entity(this, registrationName, entityFactory);
    }
    // endregion

    // region: BlockEntityType
    @Override
    public final <P, T extends BlockEntity> BlockEntityBuilder<P, T> blockEntity(P parent, String registrationName, BlockEntityFactory<T> entityFactory)
    {
        return new BlockEntityBuilder<>(parent, this, registrationName, entityFactory);
    }

    @Override
    public final <T extends BlockEntity> BlockEntityBuilder<BuilderManager, T> blockEntity(String registrationName, BlockEntityFactory<T> entityFactory)
    {
        return blockEntity(this, registrationName, entityFactory);
    }
    // endregion

    // region: Enchantment
    @Override
    public final <P, T extends Enchantment> EnchantmentBuilder<P, T> enchantment(P parent, String registrationName, EnchantmentCategory enchantmentCategory, EnchantmentFactory<T> enchantmentFactory)
    {
        return new EnchantmentBuilder<>(parent, this, registrationName, enchantmentCategory, enchantmentFactory);
    }

    @Override
    public <P> EnchantmentBuilder<P, SimpleEnchantment> enchantment(P parent, String registrationName, EnchantmentCategory enchantmentCategory)
    {
        return enchantment(parent, registrationName, enchantmentCategory, SimpleEnchantment::new);
    }

    @Override
    public final <T extends Enchantment> EnchantmentBuilder<BuilderManager, T> enchantment(String registrationName, EnchantmentCategory enchantmentCategory, EnchantmentFactory<T> enchantmentFactory)
    {
        return enchantment(this, registrationName, enchantmentCategory, enchantmentFactory);
    }

    @Override
    public EnchantmentBuilder<BuilderManager, SimpleEnchantment> enchantment(String registrationName, EnchantmentCategory enchantmentCategory)
    {
        return enchantment(this, registrationName, enchantmentCategory, SimpleEnchantment::new);
    }
    // endregion
}
