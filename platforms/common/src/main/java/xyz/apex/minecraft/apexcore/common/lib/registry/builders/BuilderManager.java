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

/**
 * Builder Manager used to construct various builders.
 *
 * @param <M> Type of builder manager.
 */
public sealed interface BuilderManager<M extends BuilderManager<M>> permits SimpleBuilderManager
{
    /**
     * @return Registrar manager this builder manager is bound to.
     */
    RegistrarManager getRegistrarManager();

    /**
     * @return Owning mod id this builder manager is bound to.
     */
    String getOwnerId();

    /**
     * @return This builder manager.
     */
    M self();

    // region: Item

    /**
     * Returns new builder used to build a new item instance.
     *
     * @param parent           Parent element of the builder.
     * @param registrationName Registration name of the builder.
     * @param itemFactory      Item factory used to construct finalized item instance.
     * @param <P>              Type of parent element.
     * @param <T>              Type of item.
     * @return New builder used to build a new item instance.
     */
    <P, T extends Item> ItemBuilder<P, T, M> item(P parent, String registrationName, ItemFactory<T> itemFactory);

    /**
     * Returns new builder used to build a new item instance.
     *
     * @param parent           Parent element of the builder.
     * @param registrationName Registration name of the builder.
     * @param <P>              Type of parent element.
     * @return New builder used to build a new item instance.
     */
    <P> ItemBuilder<P, Item, M> item(P parent, String registrationName);

    /**
     * Returns new builder used to build a new item instance.
     *
     * @param registrationName Registration name of the builder.
     * @param itemFactory      Item factory used to construct finalized item instance.
     * @param <T>              Type of item.
     * @return New builder used to build a new item instance.
     */
    <T extends Item> ItemBuilder<M, T, M> item(String registrationName, ItemFactory<T> itemFactory);

    /**
     * Returns new builder used to build a new item instance.
     *
     * @param registrationName Registration name of the builder.
     * @return New builder used to build a new item instance.
     */
    ItemBuilder<M, Item, M> item(String registrationName);
    // endregion

    // region: Block

    /**
     * Returns new builder used to build a new block instance.
     *
     * @param parent           Parent element of the builder.
     * @param registrationName Registration name of the builder.
     * @param blockFactory     Block factory used to construct finalized block instance.
     * @param <P>              Type of parent element.
     * @param <T>              Type of block.
     * @return New builder used to build a new block instance.
     */
    <P, T extends Block> BlockBuilder<P, T, M> block(P parent, String registrationName, BlockFactory<T> blockFactory);

    /**
     * Returns new builder used to build a new block instance.
     *
     * @param parent           Parent element of the builder.
     * @param registrationName Registration name of the builder.
     * @param <P>              Type of parent element.
     * @return New builder used to build a new block instance.
     */
    <P> BlockBuilder<P, Block, M> block(P parent, String registrationName);

    /**
     * Returns new builder used to build a new block instance.
     *
     * @param registrationName Registration name of the builder.
     * @param blockFactory     Block factory used to construct finalized block instance.
     * @param <T>              Type of block.
     * @return New builder used to build a new block instance.
     */
    <T extends Block> BlockBuilder<M, T, M> block(String registrationName, BlockFactory<T> blockFactory);

    /**
     * Returns new builder used to build a new block instance.
     *
     * @param registrationName Registration name of the builder.
     * @return New builder used to build a new block instance.
     */
    BlockBuilder<M, Block, M> block(String registrationName);
    // endregion

    // region: Entity

    /**
     * Returns new builder used to build a new entity type instance.
     *
     * @param parent           Parent element of the builder.
     * @param registrationName Registration name of the builder.
     * @param entityFactory    Entity factory used to construct finalized entity type instance.
     * @param <P>              Type of parent element.
     * @param <T>              Type of entity type.
     * @return New builder used to build a new entity type instance.
     */
    <P, T extends Entity> EntityBuilder<P, T, M> entity(P parent, String registrationName, EntityType.EntityFactory<T> entityFactory);

    /**
     * Returns new builder used to build a new entity type instance.
     *
     * @param registrationName Registration name of the builder.
     * @param entityFactory    Entity factory used to construct finalized entity type instance.
     * @param <T>              Type of entity type.
     * @return New builder used to build a new entity type instance.
     */
    <T extends Entity> EntityBuilder<M, T, M> entity(String registrationName, EntityType.EntityFactory<T> entityFactory);
    // endregion

    // region: BlockEntity

    /**
     * Returns new builder used to build a new block entity type instance.
     *
     * @param parent             Parent element of the builder.
     * @param registrationName   Registration name of the builder.
     * @param blockEntityFactory Block entity factory used to construct finalized block entity type instance.
     * @param <P>                Type of parent element.
     * @param <T>                Type of block entity.
     * @return New builder used to build a new block entity type instance.
     */
    <P, T extends BlockEntity> BlockEntityBuilder<P, T, M> blockEntity(P parent, String registrationName, BlockEntityFactory<T> blockEntityFactory);

    /**
     * Returns new builder used to build a new block entity type instance.
     *
     * @param registrationName   Registration name of the builder.
     * @param blockEntityFactory Block entity factory used to construct finalized block entity type instance.
     * @param <T>                Type of block entity.
     * @return New builder used to build a new block entity type instance.
     */
    <T extends BlockEntity> BlockEntityBuilder<M, T, M> blockEntity(String registrationName, BlockEntityFactory<T> blockEntityFactory);
    // endregion

    // region: Enchantment

    /**
     * Returns new builder used to build a new enchantment instance.
     *
     * @param parent              Parent element of the builder.
     * @param registrationName    Registration name of the builder.
     * @param enchantmentCategory Enchantment category.
     * @param enchantmentFactory  Enchantment factory used to construct finalized enchantment instance.
     * @param <P>                 Type of parent element.
     * @param <T>                 Type of enchantment.
     * @return New builder used to build a new block entity type instance.
     */
    <P, T extends Enchantment> EnchantmentBuilder<P, T, M> enchantment(P parent, String registrationName, EnchantmentCategory enchantmentCategory, EnchantmentFactory<T> enchantmentFactory);

    /**
     * Returns new builder used to build a new enchantment instance.
     *
     * @param parent              Parent element of the builder.
     * @param registrationName    Registration name of the builder.
     * @param enchantmentCategory Enchantment category.
     * @param <P>                 Type of parent element.
     * @return New builder used to build a new block entity type instance.
     */
    <P> EnchantmentBuilder<P, SimpleEnchantment, M> enchantment(P parent, String registrationName, EnchantmentCategory enchantmentCategory);

    /**
     * Returns new builder used to build a new enchantment instance.
     *
     * @param registrationName    Registration name of the builder.
     * @param enchantmentCategory Enchantment category.
     * @param enchantmentFactory  Enchantment factory used to construct finalized enchantment instance.
     * @param <T>                 Type of enchantment.
     * @return New builder used to build a new block entity type instance.
     */
    <T extends Enchantment> EnchantmentBuilder<M, T, M> enchantment(String registrationName, EnchantmentCategory enchantmentCategory, EnchantmentFactory<T> enchantmentFactory);

    /**
     * Returns new builder used to build a new enchantment instance.
     *
     * @param registrationName    Registration name of the builder.
     * @param enchantmentCategory Enchantment category.
     * @return New builder used to build a new block entity type instance.
     */
    EnchantmentBuilder<M, SimpleEnchantment, M> enchantment(String registrationName, EnchantmentCategory enchantmentCategory);
    // endregion

    // region: CreativeModeTab

    /**
     * Returns new builder used to build a new creative mode tab instance.
     *
     * @param parent           Parent element of the builder.
     * @param registrationName Registration name of the builder.
     * @param <P>              Type of parent element.
     * @return New builder used to build a new creative mode tab instance.
     */
    <P> CreativeModeTabBuilder<P, M> creativeModeTab(P parent, String registrationName);

    /**
     * Returns new builder used to build a new creative mode tab instance.
     *
     * @param registrationName Registration name of the builder.
     * @return New builder used to build a new creative mode tab instance.
     */
    CreativeModeTabBuilder<M, M> creativeModeTab(String registrationName);
    // endregion

    // TODO:
    //  DamageType
    //  Fluid
    //  MenuType
    //  MobEffect
    //  ParticleType
    //  PoiType
    //  SoundEvent
    //  TrimMaterial
    //  TrimPattern

    /**
     * Returns new builder manager bound to the given registrar manager.
     *
     * @param registrarManager Registrar manager to bind the builder manager to.
     * @return New builder manager bound to the given registrar manager.
     */
    static BuilderManager<Impl> create(RegistrarManager registrarManager)
    {
        return new Impl(registrarManager);
    }

    /**
     * Returns new builder manager bound to the given mod id.
     *
     * @param ownerId Mod id to bind the builder manager to.
     * @return New builder manager bound to the given mod id.
     */
    static BuilderManager<Impl> create(String ownerId)
    {
        return create(RegistrarManager.get(ownerId));
    }

    final class Impl extends SimpleBuilderManager<Impl>
    {
        private Impl(RegistrarManager registrarManager)
        {
            super(registrarManager);
        }
    }
}
