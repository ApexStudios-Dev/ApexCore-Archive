package xyz.apex.minecraft.apexcore.common.component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ComponentBlock
{
    void registerComponents();

    <T extends Component> T registerComponent(ComponentType<T> componentType, Object... constructorArgs);

    @Nullable
    <T extends Component> T getComponent(ComponentType<T> componentType);

    <T extends Component> Optional<T> getOptionalComponent(ComponentType<T> componentType);

    <T extends Component> T getRequiredComponent(ComponentType<T> componentType);

    Set<ResourceLocation> getComponentTypes();

    Collection<Component> getComponents();

    <T extends Component> boolean hasComponent(ComponentType<T> componentType);

    default Block toBlock()
    {
        return (Block) this;
    }
}
