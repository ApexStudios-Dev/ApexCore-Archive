package xyz.apex.minecraft.apexcore.common.component;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class SimpleComponent implements Component
{
    protected final ComponentBlock block;

    protected SimpleComponent(ComponentBlock block)
    {
        this.block = block;
    }

    @Override
    public final <T extends Component> @Nullable T getComponent(ComponentType<T> componentType)
    {
        return Component.super.getComponent(componentType);
    }

    @Override
    public final <T extends Component> Optional<T> getOptionalComponent(ComponentType<T> componentType)
    {
        return Component.super.getOptionalComponent(componentType);
    }

    @Override
    public final <T extends Component> T getRequiredComponent(ComponentType<T> componentType)
    {
        return Component.super.getRequiredComponent(componentType);
    }

    @Override
    public final Set<ResourceLocation> getComponentTypes()
    {
        return Component.super.getComponentTypes();
    }

    @Override
    public final Collection<Component> getComponents()
    {
        return Component.super.getComponents();
    }

    @Override
    public final <T extends Component> boolean hasComponent(ComponentType<T> componentType)
    {
        return Component.super.hasComponent(componentType);
    }

    @Override
    public final ComponentBlock getBlock()
    {
        return block;
    }
}
