package xyz.apex.minecraft.apexcore.common.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.platform.Platform;

public interface TagHelper
{
    String FABRIC_TAG_ID = "c"; // fabric uses 'c' for 'common'

    static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryType, String ownerId, String tagPath)
    {
        return TagKey.create(registryType, new ResourceLocation(ownerId, tagPath));
    }

    static <T> TagKey<T> forgeTag(ResourceKey<? extends Registry<T>> registryType, String tagPath)
    {
        return tag(registryType, Platform.FORGE, tagPath);
    }

    static <T> TagKey<T> fabricTag(ResourceKey<? extends Registry<T>> registryType, String tagPath)
    {
        return tag(registryType, FABRIC_TAG_ID, tagPath);
    }

    static <T> TagKey<T> apexTag(ResourceKey<? extends Registry<T>> registryType, String tagPath)
    {
        return tag(registryType, ApexCore.ID, tagPath);
    }

    // region: Block
    static TagKey<Block> blockTag(String ownerId, String tagPath)
    {
        return tag(Registries.BLOCK, ownerId, tagPath);
    }

    static TagKey<Block> forgeBlockTag(String tagPath)
    {
        return blockTag(Platform.FORGE, tagPath);
    }

    static TagKey<Block> fabricBlockTag(String tagPath)
    {
        return blockTag(FABRIC_TAG_ID, tagPath);
    }

    static TagKey<Block> apexBlockTag(String tagPath)
    {
        return blockTag(ApexCore.ID, tagPath);
    }
    // endregion

    // region: Item
    static TagKey<Item> itemTag(String ownerId, String tagPath)
    {
        return tag(Registries.ITEM, ownerId, tagPath);
    }

    static TagKey<Item> forgeItemTag(String tagPath)
    {
        return itemTag(Platform.FORGE, tagPath);
    }

    static TagKey<Item> fabricItemTag(String tagPath)
    {
        return itemTag(FABRIC_TAG_ID, tagPath);
    }

    static TagKey<Item> apexItemTag(String tagPath)
    {
        return itemTag(ApexCore.ID, tagPath);
    }
    // endregion

    // region: EntityType
    static TagKey<EntityType<?>> entityTag(String ownerId, String tagPath)
    {
        return tag(Registries.ENTITY_TYPE, ownerId, tagPath);
    }

    static TagKey<EntityType<?>> forgeEntityTag(String tagPath)
    {
        return entityTag(Platform.FORGE, tagPath);
    }

    static TagKey<EntityType<?>> fabricEntityTag(String tagPath)
    {
        return entityTag(FABRIC_TAG_ID, tagPath);
    }

    static TagKey<EntityType<?>> apexEntityTag(String tagPath)
    {
        return entityTag(ApexCore.ID, tagPath);
    }
    // endregion
}
