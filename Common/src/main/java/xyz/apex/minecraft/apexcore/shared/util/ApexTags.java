package xyz.apex.minecraft.apexcore.shared.util;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.ApexCore;

@SuppressWarnings("unused")
public interface ApexTags
{
    interface Blocks
    {
        TagKey<Block> IMMOVABLE = tag(ApexCore.ID, "immovable");

        static TagKey<Block> tag(String namespace, String name)
        {
            return ApexTags.tag(Registries.BLOCK, namespace, name);
        }

        private static void bootstrap() {}
    }

    interface Items
    {
        static TagKey<Item> tag(String namespace, String name)
        {
            return ApexTags.tag(Registries.ITEM, namespace, name);
        }

        private static void bootstrap() {}
    }

    static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryType, String namespace, String name)
    {
        return TagKey.create(registryType, new ResourceLocation(namespace, name));
    }

    static void bootstrap()
    {
        Blocks.bootstrap();
        Items.bootstrap();
    }
}
