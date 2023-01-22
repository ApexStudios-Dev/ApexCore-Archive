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
public interface Tags
{
    interface Blocks
    {
        TagKey<Block> IMMOVABLE = tag(ApexCore.ID, "immovable");

        static TagKey<Block> tag(String namespace, String name)
        {
            return Tags.tag(Registries.BLOCK, namespace, name);
        }

        static TagKey<Block> forge(String name)
        {
            return Tags.forge(Registries.BLOCK, name);
        }

        static TagKey<Block> fabric(String name)
        {
            return Tags.fabric(Registries.BLOCK, name);
        }

        private static void bootstrap() {}
    }

    interface Items
    {
        static TagKey<Item> tag(String namespace, String name)
        {
            return Tags.tag(Registries.ITEM, namespace, name);
        }

        static TagKey<Item> forge(String name)
        {
            return Tags.forge(Registries.ITEM, name);
        }

        static TagKey<Item> fabric(String name)
        {
            return Tags.fabric(Registries.ITEM, name);
        }

        private static void bootstrap() {}
    }

    static <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryType, String namespace, String name)
    {
        return TagKey.create(registryType, new ResourceLocation(namespace, name));
    }

    static <T> TagKey<T> forge(ResourceKey<? extends Registry<T>> registryType, String name)
    {
        return tag(registryType, "forge", name);
    }

    static <T> TagKey<T> fabric(ResourceKey<? extends Registry<T>> registryType, String name)
    {
        return tag(registryType, "c", name);
    }

    static void bootstrap()
    {
        Blocks.bootstrap();
        Items.bootstrap();
    }
}
