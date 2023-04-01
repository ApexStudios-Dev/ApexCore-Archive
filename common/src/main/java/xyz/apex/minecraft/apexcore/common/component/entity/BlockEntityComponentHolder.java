package xyz.apex.minecraft.apexcore.common.component.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public sealed interface BlockEntityComponentHolder permits BaseBlockEntityComponentHolder
{
    void registerComponents(Registrar registrar);

    @Nullable
    <T extends BlockEntityComponent> T getComponent(BlockEntityComponentType<T> componentType);

    <T extends BlockEntityComponent> Optional<T> getOptionalComponent(BlockEntityComponentType<T> componentType);

    <T extends BlockEntityComponent> T getRequiredComponent(BlockEntityComponentType<T> componentType);

    Set<BlockEntityComponentType<?>> getComponentTypes();

    Stream<BlockEntityComponent> components();

    <T extends BlockEntityComponent> boolean hasComponent(BlockEntityComponentType<T> componentType);

    BlockEntity toBlockEntity();

    BlockEntityType<? extends BlockEntity> toBlockEntityType();

    BlockPos toBlockPos();

    BlockState toBlockState();

    @Nullable
    Level getLevel();

    boolean hasLevel();

    void runForLevel(Consumer<Level> consumer);

    // see BlockEntityComponent
    void markDirty();

    @FunctionalInterface
    interface Registrar
    {
        <T extends BlockEntityComponent> T register(BlockEntityComponentType<T> componentType);
    }
}
