package xyz.apex.minecraft.apexcore.shared.data;

import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.data.providers.Language;
import xyz.apex.minecraft.apexcore.shared.data.providers.Model;
import xyz.apex.minecraft.apexcore.shared.data.providers.Recipe;
import xyz.apex.minecraft.apexcore.shared.data.providers.Tag;

public interface ProviderTypes
{
    // Client
    ProviderType<Language> LANGUAGE = new ProviderType<>("language");
    ProviderType<Model.ItemModel<?>> ITEM_MODELS = new ProviderType<>("item_models");
    ProviderType<Model.BlockModel<?>> BLOCK_MODELS = new ProviderType<>("block_models");

    // Server
    ProviderType<Recipe> RECIPES = new ProviderType<>("recipes");

    ProviderType<Tag.ItemTag> ITEM_TAGS = new ProviderType<>("item_tags");
    ProviderType<Tag<Block>> BLOCK_TAGS = new ProviderType<>("block_tags");
}
