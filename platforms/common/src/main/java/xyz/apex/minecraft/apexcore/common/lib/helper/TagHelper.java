package xyz.apex.minecraft.apexcore.common.lib.helper;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PlatformOnly;

/**
 * Various tag related utilities.
 */
@ApiStatus.NonExtendable
public interface TagHelper
{
    String FABRIC_TAG_ID = "c"; // fabric uses 'c' [meaning 'common'] for their common tags

    /**
     * Returns new tag key for given registry type.
     *
     * @param registryType Registry type for this tag.
     * @param ownerId      Owner ID for this tag.
     * @param tagPath      Path for this tag.
     * @param <T>          Type of registry.
     * @return Tag key for given registry type.
     */
    static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryType, String ownerId, String tagPath)
    {
        return TagKey.create(registryType, new ResourceLocation(ownerId, tagPath));
    }

    /**
     * Returns new Forge tag key for given registry type.
     *
     * @param registryType Registry type for this tag.
     * @param tagPath      Path for this tag.
     * @param <T>          Type of registry.
     * @return Tag key for given registry type.
     */
    static <T> TagKey<T> forgeTag(ResourceKey<? extends Registry<T>> registryType, String tagPath)
    {
        return tag(registryType, PlatformOnly.FORGE, tagPath);
    }

    /**
     * Returns new Fabric tag key for given registry type.
     *
     * @param registryType Registry type for this tag.
     * @param tagPath      Path for this tag.
     * @param <T>          Type of registry.
     * @return Tag key for given registry type.
     */
    static <T> TagKey<T> fabricTag(ResourceKey<? extends Registry<T>> registryType, String tagPath)
    {
        return tag(registryType, FABRIC_TAG_ID, tagPath);
    }

    /**
     * Returns new ApexCore tag key for given registry type.
     *
     * @param registryType Registry type for this tag.
     * @param tagPath      Path for this tag.
     * @param <T>          Type of registry.
     * @return Tag key for given registry type.
     */
    static <T> TagKey<T> apexTag(ResourceKey<? extends Registry<T>> registryType, String tagPath)
    {
        return tag(registryType, ApexCore.ID, tagPath);
    }

    // region: Block

    /**
     * Returns new Block tag.
     *
     * @param ownerId Owner ID for this tag.
     * @param tagPath Path for this tag.
     * @return New Block Tag.
     */
    static TagKey<Block> blockTag(String ownerId, String tagPath)
    {
        return tag(Registries.BLOCK, ownerId, tagPath);
    }

    /**
     * Returns new Forge Block tag.
     *
     * @param tagPath Path for this tag.
     * @return New Block Tag.
     */
    static TagKey<Block> forgeBlockTag(String tagPath)
    {
        return blockTag(PlatformOnly.FORGE, tagPath);
    }

    /**
     * Returns new Fabric Block tag.
     *
     * @param tagPath Path for this tag.
     * @return New Block Tag.
     */
    static TagKey<Block> fabricBlockTag(String tagPath)
    {
        return blockTag(FABRIC_TAG_ID, tagPath);
    }

    /**
     * Returns new ApexCore Block tag.
     *
     * @param tagPath Path for this tag.
     * @return New Block Tag.
     */
    static TagKey<Block> apexBlockTag(String tagPath)
    {
        return blockTag(ApexCore.ID, tagPath);
    }
    // endregion

    // region: Item

    /**
     * Returns new Item tag.
     *
     * @param ownerId Owner ID for this tag.
     * @param tagPath Path for this tag.
     * @return New Item Tag.
     */
    static TagKey<Item> itemTag(String ownerId, String tagPath)
    {
        return tag(Registries.ITEM, ownerId, tagPath);
    }

    /**
     * Returns new Forge Item tag.
     *
     * @param tagPath Path for this tag.
     * @return New Item Tag.
     */
    static TagKey<Item> forgeItemTag(String tagPath)
    {
        return itemTag(PlatformOnly.FORGE, tagPath);
    }

    /**
     * Returns new Fabric Item tag.
     *
     * @param tagPath Path for this tag.
     * @return New Item Tag.
     */
    static TagKey<Item> fabricItemTag(String tagPath)
    {
        return itemTag(FABRIC_TAG_ID, tagPath);
    }

    /**
     * Returns new ApexCore Item tag.
     *
     * @param tagPath Path for this tag.
     * @return New Item Tag.
     */
    static TagKey<Item> apexItemTag(String tagPath)
    {
        return itemTag(ApexCore.ID, tagPath);
    }
    // endregion

    // region: EntityType

    /**
     * Returns new Entity tag.
     *
     * @param ownerId Owner ID for this tag.
     * @param tagPath Path for this tag.
     * @return New Entity Tag.
     */
    static TagKey<EntityType<?>> entityTag(String ownerId, String tagPath)
    {
        return tag(Registries.ENTITY_TYPE, ownerId, tagPath);
    }

    /**
     * Returns new Forge Entity tag.
     *
     * @param tagPath Path for this tag.
     * @return New Entity Tag.
     */
    static TagKey<EntityType<?>> forgeEntityTag(String tagPath)
    {
        return entityTag(PlatformOnly.FORGE, tagPath);
    }

    /**
     * Returns new Fabric Entity tag.
     *
     * @param tagPath Path for this tag.
     * @return New Entity Tag.
     */
    static TagKey<EntityType<?>> fabricEntityTag(String tagPath)
    {
        return entityTag(FABRIC_TAG_ID, tagPath);
    }

    /**
     * Returns new ApexCore Entity tag.
     *
     * @param tagPath Path for this tag.
     * @return New Entity Tag.
     */
    static TagKey<EntityType<?>> apexEntityTag(String tagPath)
    {
        return entityTag(ApexCore.ID, tagPath);
    }
    // endregion
}
