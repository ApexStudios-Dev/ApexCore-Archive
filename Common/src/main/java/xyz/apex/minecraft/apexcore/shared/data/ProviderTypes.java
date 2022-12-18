package xyz.apex.minecraft.apexcore.shared.data;

import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.data.providers.LanguageProvider;
import xyz.apex.minecraft.apexcore.shared.data.providers.RecipeProvider;
import xyz.apex.minecraft.apexcore.shared.data.providers.model.BlockModelProvider;
import xyz.apex.minecraft.apexcore.shared.data.providers.model.ItemModelProvider;

public interface ProviderTypes
{
    // Client
    ProviderType<LanguageProvider> LANGUAGE = new ProviderType<>("language");
    ProviderType<ItemModelProvider> ITEM_MODELS = new ProviderType<>("item_models");
    ProviderType<BlockModelProvider> BLOCK_MODELS = new ProviderType<>("block_models");

    // Server
    ProviderType<RecipeProvider> RECIPES = new ProviderType<>("recipes");

    ProviderType<ItemTagsProvider> ITEM_TAGS = new ProviderType<>("item_tags");
    ProviderType<TagsProvider<Block>> BLOCK_TAGS = new ProviderType<>("block_tags");
}
