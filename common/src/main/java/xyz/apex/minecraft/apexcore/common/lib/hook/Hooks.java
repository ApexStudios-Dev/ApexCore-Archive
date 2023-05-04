package xyz.apex.minecraft.apexcore.common.lib.hook;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

import java.util.function.Supplier;

/**
 * Interface holding all known hooks
 */
@ApiStatus.NonExtendable
public interface Hooks extends Services.Service
{
    /**
     * Hooks for registering various renderer elements.
     * <p>
     * May throw exceptions if used server side, wrap any calls in a client {@link PhysicalSide#runWhenOn(PhysicalSide, Supplier)}.
     */
    @SideOnly(PhysicalSide.CLIENT)
    RegisterRendererHooks registerRenderer();

    /**
     * Hooks for registering block and item color handlers.
     * <p>
     * May throw exceptions if used server side, wrap any calls in a client {@link PhysicalSide#runWhenOn(PhysicalSide, Supplier)}.
     */
    @SideOnly(PhysicalSide.CLIENT)
    RegisterColorHandlerHooks registerColorHandler();

    /**
     * Registers default attributes for given entity type.
     *
     * @param entityType        Entity type to register default attributes for.
     * @param defaultAttributes Default attributes to be registered.
     */
    void registerEntityDefaultAttribute(Supplier<EntityType<? extends LivingEntity>> entityType, Supplier<AttributeSupplier.Builder> defaultAttributes);
}
