package dev.apexstudios.apexcore.fabric.platform;

import dev.apexstudios.apexcore.common.platform.PlatformTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.impl.tag.convention.TagRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

final class PlatformTagsImpl implements PlatformTags
{
    @Override
    public TagKey<Block> blockRelocationNotSupported()
    {
        return ConventionalBlockTags.MOVEMENT_RESTRICTED;
    }

    @Override
    public TagKey<Block> block(String path)
    {
        return TagRegistration.BLOCK_TAG_REGISTRATION.registerCommon(path);
    }

    @Override
    public TagKey<Item> item(String path)
    {
        return TagRegistration.ITEM_TAG_REGISTRATION.registerCommon(path);
    }

    @Override
    public ResourceLocation id(String path)
    {
        return new ResourceLocation("c", path);
    }
}
