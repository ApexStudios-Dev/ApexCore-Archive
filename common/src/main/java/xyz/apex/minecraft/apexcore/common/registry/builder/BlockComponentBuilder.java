package xyz.apex.minecraft.apexcore.common.registry.builder;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponent;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentHolderFactory;
import xyz.apex.minecraft.apexcore.common.component.block.BlockComponentType;

import java.util.function.Consumer;

public final class BlockComponentBuilder<T extends Block & BlockComponentHolder> extends AbstractBlockBuilder<T, BlockComponentBuilder<T>>
{
    private Consumer<BlockComponentHolder.Registrar> registrarConsumer = registrar -> {};
    private final BlockComponentHolderFactory<T> blockFactory;

    private BlockComponentBuilder(String ownerId, String registrationName, BlockComponentHolderFactory<T> blockFactory)
    {
        super(ownerId, registrationName);

        this.blockFactory = blockFactory;
    }

    public BlockComponentBuilder<T> onRegisterComponents(Consumer<BlockComponentHolder.Registrar> registrarConsumer)
    {
        this.registrarConsumer = this.registrarConsumer.andThen(registrarConsumer);
        return this;
    }

    public <C extends BlockComponent> BlockComponentBuilder<T> registerComponent(BlockComponentType<C> componentType, Consumer<C> consumer)
    {
        return onRegisterComponents(registrar -> consumer.accept(registrar.register(componentType)));
    }

    public <C extends BlockComponent> BlockComponentBuilder<T> registerComponent(BlockComponentType<C> componentType)
    {
        return registerComponent(componentType, component -> {});
    }

    @Override
    protected T createBlock(BlockBehaviour.Properties properties)
    {
        return blockFactory.create(registrarConsumer, properties);
    }

    public static <T extends Block & BlockComponentHolder> BlockComponentBuilder<T> builder(String ownerId, String registrationName, BlockComponentHolderFactory<T> blockFactory)
    {
        return new BlockComponentBuilder<>(ownerId, registrationName, blockFactory);
    }
}
