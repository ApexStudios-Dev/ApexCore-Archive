package xyz.apex.minecraft.apexcore.fabric.lib.hooks;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.Hooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegisterColorHandlerHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegisterRendererHooks;

import java.util.function.Supplier;

@ApiStatus.Internal
public final class HooksImpl implements Hooks
{
    private final RegisterRendererHooks registerRendererHooks = new RegisterRendererHooksImpl();
    private final RegisterColorHandlerHooks colorHandlerHooks = new RegisterColorHandlerHooksImpl();

    @Override
    public RegisterRendererHooks registerRenderer()
    {
        return registerRendererHooks;
    }

    @Override
    public RegisterColorHandlerHooks registerColorHandler()
    {
        return colorHandlerHooks;
    }

    @Override
    public void registerEntityDefaultAttribute(Supplier<EntityType<? extends LivingEntity>> entityType, Supplier<AttributeSupplier.Builder> defaultAttributes)
    {
        FabricDefaultAttributeRegistry.register(entityType.get(), defaultAttributes.get());
    }
}
