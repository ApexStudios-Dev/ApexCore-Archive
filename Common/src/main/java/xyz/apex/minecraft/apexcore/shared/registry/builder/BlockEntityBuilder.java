package xyz.apex.minecraft.apexcore.shared.registry.builder;

import com.google.common.collect.Sets;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import org.jetbrains.annotations.Nullable;

import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import xyz.apex.minecraft.apexcore.shared.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.shared.registry.entry.BlockEntityEntry;
import xyz.apex.minecraft.apexcore.shared.util.function.Lazy;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public final class BlockEntityBuilder<R extends BlockEntity, O extends AbstractRegistrar<O>, P> extends AbstractBuilder<BlockEntityType<?>, BlockEntityType<R>, O, P, BlockEntityBuilder<R, O, P>>
{
    private final Factory<R> factory;
    private final Set<Supplier<? extends Block>> validBlocks = Sets.newHashSet();
    private Supplier<Supplier<BlockEntityRendererProvider<R>>> rendererSupplier = () -> () -> null;
    @Nullable private MenuBuilder<?, ?, O, BlockEntityBuilder<R, O, P>> menuBuilder = null;

    public BlockEntityBuilder(O owner, P parent, String registrationName, Factory<R> factory)
    {
        super(owner, parent, Registries.BLOCK_ENTITY_TYPE, registrationName);

        this.factory = factory;

        onRegister(blockEntityType -> EnvExecutor.runInEnv(Env.CLIENT, () -> () -> {
            var renderer = rendererSupplier.get().get();
            if(renderer != null) BlockEntityRendererRegistry.register(blockEntityType, renderer);
        }));
    }

    public BlockEntityBuilder<R, O, P> renderer(Supplier<Supplier<BlockEntityRendererProvider<R>>> rendererSupplier)
    {
        this.rendererSupplier = rendererSupplier;
        return this;
    }

    public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> BlockEntityBuilder<R, O, P> menu(MenuBuilder.MenuFactory<M> menuFactory, Supplier<MenuBuilder.ScreenFactory<M, S>> screenFactorySupplier)
    {
        menuBuilder = new MenuBuilder<>(owner, this, getRegistrationName(), menuFactory, screenFactorySupplier);
        return this;
    }

    public BlockEntityBuilder<R, O, P> noMenu()
    {
        menuBuilder = null;
        return this;
    }

    @Deprecated
    public BlockEntityBuilder<R, O, P> validBlock(Block block)
    {
        return validBlock(Lazy.of(block));
    }

    @Deprecated
    public BlockEntityBuilder<R, O, P> validBlock(Block... blocks)
    {
        Arrays.stream(blocks).map(Lazy::of).forEach(this::validBlock);
        return this;
    }

    public BlockEntityBuilder<R, O, P> validBlock(Supplier<? extends Block> block)
    {
        validBlocks.add(block);
        return this;
    }

    public BlockEntityBuilder<R, O, P> validBlock(Supplier<? extends Block>... block)
    {
        Arrays.stream(block).forEach(this::validBlock);
        return this;
    }

    @Override
    protected BlockEntityType<R> createEntry()
    {
        var validBlocks = this.validBlocks.stream().map(Supplier::get).toArray(Block[]::new);
        var dataType = Objects.requireNonNull(Util.fetchChoiceType(References.BLOCK_ENTITY, getRegistryName().toString()));
        return BlockEntityType.Builder.of((pos, blockState) -> factory.create(safeSupplier.get(), pos, blockState), validBlocks).build(dataType);
    }

    @Override
    protected BlockEntityEntry<R> createRegistryEntry(RegistrySupplier<BlockEntityType<R>> delegate)
    {
        return new BlockEntityEntry<>(owner, delegate, registryKey);
    }

    @Override
    public BlockEntityEntry<R> register()
    {
        if(menuBuilder != null) menuBuilder.register();
        return (BlockEntityEntry<R>) super.register();
    }

    @FunctionalInterface
    public interface Factory<T extends BlockEntity>
    {
        T create(BlockEntityType<T> blockEntityType, BlockPos pos, BlockState blockState);
    }
}
