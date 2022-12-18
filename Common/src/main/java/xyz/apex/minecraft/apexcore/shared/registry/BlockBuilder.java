package xyz.apex.minecraft.apexcore.shared.registry;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import xyz.apex.minecraft.apexcore.shared.data.ProviderTypes;
import xyz.apex.minecraft.apexcore.shared.platform.ForPlatform;
import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntry;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class BlockBuilder<R extends Block, P> extends AbstractBuilder<Block, R, P, BlockBuilder<R, P>, BlockEntry<R>>
{
    private final BlockFactory<R> blockFactory;
    private Supplier<BlockBehaviour.Properties> initialProperties = () -> BlockBehaviour.Properties.copy(Blocks.STONE);
    private Function<BlockBehaviour.Properties, BlockBehaviour.Properties> propertiesModifier = Function.identity();
    private final List<Supplier<Supplier<RenderType>>> renderTypes = Lists.newArrayList();

    private BlockBuilder(ResourceLocation registryName, @Nullable P parent, BlockFactory<R> blockFactory)
    {
        super(Registries.BLOCK, registryName, parent, BlockEntry::new);

        this.blockFactory = blockFactory;

        GamePlatform.events().onClientSetup(() -> {
            var renderTypes = this.renderTypes.stream().map(Supplier::get).map(Supplier::get).toArray(RenderType[]::new);
            GamePlatform.events().registerRenderTypes(asSupplier(), () -> () -> renderTypes);
        });

        defaultLang();

        // TODO: Change to blockstate
        setData(ProviderTypes.BLOCK_MODELS, (ctx, provider) -> provider.cubeAll(ctx));
    }

    @Override
    protected R construct()
    {
        var properties = propertiesModifier.apply(initialProperties.get());
        return blockFactory.create(properties);
    }

    public <T extends Item> ItemBuilder<T, BlockBuilder<R, P>> item(BlockItemFactory<T, R> itemFactory)
    {
        return ItemBuilder.builder(getModId(), getName(), this, properties -> itemFactory.create(getEntry(), properties))
                .clearData(ProviderTypes.LANGUAGE)
                .model((ctx, provider) -> provider.blockItem(getRegistryName()))
        ;
    }

    public BlockBuilder<R, P> defaultLang()
    {
        return lang(Block::getDescriptionId);
    }

    public BlockBuilder<R, P> lang(String name)
    {
        return lang(Block::getDescriptionId, name);
    }

    public ItemBuilder<BlockItem, BlockBuilder<R, P>> simpleItem()
    {
        return item(BlockItem::new);
    }

    public BlockBuilder<R, P> defaultItem()
    {
        return simpleItem().build();
    }

    public BlockBuilder<R, P> initialProperties(Supplier<BlockBehaviour.Properties> initialProperties)
    {
        this.initialProperties = initialProperties;
        return this;
    }

    public BlockBuilder<R, P> initialProperties(Material material)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material));
    }

    public BlockBuilder<R, P> initialProperties(Material material, DyeColor color)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, color));
    }

    public BlockBuilder<R, P> initialProperties(Material material, MaterialColor color)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, color));
    }

    public BlockBuilder<R, P> initialProperties(Material material, Function<BlockState, MaterialColor> colorFunction)
    {
        return initialProperties(() -> BlockBehaviour.Properties.of(material, colorFunction));
    }

    public BlockBuilder<R, P> copyPropertiesFrom(Supplier<Block> block)
    {
        return initialProperties(() -> BlockBehaviour.Properties.copy(block.get()));
    }

    public BlockBuilder<R, P> properties(UnaryOperator<BlockBehaviour.Properties> propertiesModifier)
    {
        this.propertiesModifier = this.propertiesModifier.andThen(propertiesModifier);
        return this;
    }

    // forge has this set directly in the model json files
    // other platforms do not allow this and need the render types registered manually
    @ForPlatform(Platform.Type.FABRIC)
    public BlockBuilder<R, P> renderType(Supplier<Supplier<RenderType>> renderType)
    {
        renderTypes.add(renderType);
        return this;
    }

    @ForPlatform(Platform.Type.FABRIC)
    public BlockBuilder<R, P> renderType(Supplier<Supplier<RenderType>> renderType, Supplier<Supplier<RenderType>>... renderTypes)
    {
        this.renderTypes.add(renderType);
        Collections.addAll(this.renderTypes, renderTypes);
        return this;
    }

    public BlockBuilder<R, P> color(Supplier<Supplier<BlockColor>> colorHandler)
    {
        GamePlatform.events().registerBlockColor(asSupplier(), colorHandler);
        return this;
    }

    public static <R extends Block, P> BlockBuilder<R, P> builder(String modId, String name, P parent, BlockFactory<R> blockFactory)
    {
        return new BlockBuilder<>(new ResourceLocation(modId, name), parent, blockFactory);
    }

    public static <R extends Block> BlockBuilder<R, Object> builder(String modId, String name, BlockFactory<R> blockFactory)
    {
        return new BlockBuilder<>(new ResourceLocation(modId, name), null, blockFactory);
    }

    public static <P> BlockBuilder<Block, P> block(String modId, String name, P parent)
    {
        return builder(modId, name, parent, Block::new);
    }

    public static BlockBuilder<Block, Object> block(String modId, String name)
    {
        return builder(modId, name, Block::new);
    }

    @FunctionalInterface
    public interface BlockFactory<T extends Block>
    {
        T create(BlockBehaviour.Properties properties);
    }

    @FunctionalInterface
    public interface BlockItemFactory<I extends Item, B extends Block>
    {
        I create(B block, Item.Properties properties);
    }
}
