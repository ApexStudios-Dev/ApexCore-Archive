package xyz.apex.minecraft.apexcore.common.lib.registry.builders;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.BlockEntityFactory;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.BlockFactory;
import xyz.apex.minecraft.apexcore.common.lib.registry.factories.ItemFactory;

/**
 * Builder Manager used to construct various builders.
 */
public sealed interface BuilderManager permits BuilderManagerImpl
{
    /**
     * @return Registrar manager this builder manager is bound to.
     */
    RegistrarManager getRegistrarManager();

    /**
     * @return Owning mod id this builder manager is bound to.
     */
    String getOwnerId();

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
    <P, T extends Item> ItemBuilder<P, T> item(P parent, String registrationName, ItemFactory<T> itemFactory);

    /**
     * Returns new builder used to build a new item instance.
     *
     * @param parent           Parent element of the builder.
     * @param registrationName Registration name of the builder.
     * @param <P>              Type of parent element.
     * @return New builder used to build a new item instance.
     */
    <P> ItemBuilder<P, Item> item(P parent, String registrationName);

    /**
     * Returns new builder used to build a new item instance.
     *
     * @param registrationName Registration name of the builder.
     * @param itemFactory      Item factory used to construct finalized item instance.
     * @param <T>              Type of item.
     * @return New builder used to build a new item instance.
     */
    <T extends Item> ItemBuilder<BuilderManager, T> item(String registrationName, ItemFactory<T> itemFactory);

    /**
     * Returns new builder used to build a new item instance.
     *
     * @param registrationName Registration name of the builder.
     * @return New builder used to build a new item instance.
     */
    ItemBuilder<BuilderManager, Item> item(String registrationName);
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
    <P, T extends Block> BlockBuilder<P, T> block(P parent, String registrationName, BlockFactory<T> blockFactory);

    /**
     * Returns new builder used to build a new block instance.
     *
     * @param parent           Parent element of the builder.
     * @param registrationName Registration name of the builder.
     * @param <P>              Type of parent element.
     * @return New builder used to build a new block instance.
     */
    <P> BlockBuilder<P, Block> block(P parent, String registrationName);

    /**
     * Returns new builder used to build a new block instance.
     *
     * @param registrationName Registration name of the builder.
     * @param blockFactory     Block factory used to construct finalized block instance.
     * @param <T>              Type of block.
     * @return New builder used to build a new block instance.
     */
    <T extends Block> BlockBuilder<BuilderManager, T> block(String registrationName, BlockFactory<T> blockFactory);

    /**
     * Returns new builder used to build a new block instance.
     *
     * @param registrationName Registration name of the builder.
     * @return New builder used to build a new block instance.
     */
    BlockBuilder<BuilderManager, Block> block(String registrationName);
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
    <P, T extends Entity> EntityBuilder<P, T> entity(P parent, String registrationName, EntityType.EntityFactory<T> entityFactory);

    /**
     * Returns new builder used to build a new entity type instance.
     *
     * @param registrationName Registration name of the builder.
     * @param entityFactory    Entity factory used to construct finalized entity type instance.
     * @param <T>              Type of entity type.
     * @return New builder used to build a new entity type instance.
     */
    <T extends Entity> EntityBuilder<BuilderManager, T> entity(String registrationName, EntityType.EntityFactory<T> entityFactory);
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
    <P, T extends BlockEntity> BlockEntityBuilder<P, T> blockEntity(P parent, String registrationName, BlockEntityFactory<T> blockEntityFactory);

    /**
     * Returns new builder used to build a new block entity type instance.
     *
     * @param registrationName   Registration name of the builder.
     * @param blockEntityFactory Block entity factory used to construct finalized block entity type instance.
     * @param <T>                Type of block entity.
     * @return New builder used to build a new block entity type instance.
     */
    <T extends BlockEntity> BlockEntityBuilder<BuilderManager, T> blockEntity(String registrationName, BlockEntityFactory<T> blockEntityFactory);
    // endregion

    /**
     * Returns new builder manager bound to the given registrar manager.
     *
     * @param registrarManager Registrar manager to bind the builder manager to.
     * @return New builder manager bound to the given registrar manager.
     */
    static BuilderManager create(RegistrarManager registrarManager)
    {
        return new BuilderManagerImpl(registrarManager);
    }

    /**
     * Returns new builder manager bound to the given mod id.
     *
     * @param ownerId Mod id to bind the builder manager to.
     * @return New builder manager bound to the given mod id.
     */
    static BuilderManager create(String ownerId)
    {
        return create(RegistrarManager.get(ownerId));
    }
}
