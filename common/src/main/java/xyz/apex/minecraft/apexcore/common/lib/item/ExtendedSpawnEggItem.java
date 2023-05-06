package xyz.apex.minecraft.apexcore.common.lib.item;

import com.google.common.base.Suppliers;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.hook.RegisterColorHandlerHooks;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistryEntry;
import xyz.apex.minecraft.apexcore.common.lib.registry.builders.EntityBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Copy of ForgeSpawnEggItem to be used on any platform.
 */
public class ExtendedSpawnEggItem extends SpawnEggItem
{
    private static final Map<EntityType<? extends Mob>, SpawnEggItem> BY_ID = Maps.newHashMap();
    private static final Collection<SpawnEggItem> MOD_EGGS = Collections.unmodifiableCollection(BY_ID.values());
    private static final Supplier<DispenseItemBehavior> DISPENSE_SPAWN_EGG_BEHAVIOR = DispenseSpawnEggBehavior::new;
    private static final Supplier<Class<? extends SpawnEggItem>> forgeSpawnEggClass = Suppliers.memoize(ExtendedSpawnEggItem::findForgeSpawnEggClass);
    private static final Supplier<Iterable<SpawnEggItem>> forgeSpawnEggs = Suppliers.memoize(() -> findForgeSpawnEggField("MOD_EGGS", Collections::emptyList));
    private static final Supplier<Map<EntityType<? extends Mob>, ? extends SpawnEggItem>> forgeSpawnEggTypeMap = Suppliers.memoize(() -> findForgeSpawnEggField("TYPE_MAP", Collections::emptyMap));

    private final Supplier<? extends EntityType<? extends Mob>> entityType;

    @SuppressWarnings("DataFlowIssue")
    public ExtendedSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Properties properties)
    {
        super(null, backgroundColor, highlightColor, properties);

        this.entityType = entityType;
    }

    /**
     * Returns the RGB color to tint the spawn egg item model with.
     * <p>
     * This method by default is only invoked my spawn eggs run through our a {@link #registerSpawnEgg(ExtendedSpawnEggItem)} method.
     *
     * @param stack     Spawn egg item stack.
     * @param tintIndex Tinting index.
     * @return RGB color to tint spawn egg item model with.
     */
    protected int getSpawnEggColor(ItemStack stack, int tintIndex)
    {
        return getColor(tintIndex);
    }

    /**
     * @return Returns dispense behavior instance used when this spawn egg is dispensed from a dispenser.
     */
    protected DispenseItemBehavior createDispenseItemBehavior()
    {
        return DISPENSE_SPAWN_EGG_BEHAVIOR.get();
    }

    /**
     * Returns default entity type for this spawn egg.
     * <p>
     * While this is not called anywhere in vanilla or fabric, forge patches this method into the vanilla spawn egg class,
     * so we are soft overriding it here to return the correct type.
     *
     * @return Default entity type for this spawn egg.
     */
    public EntityType<?> getDefaultType()
    {
        return entityType.get();
    }

    @SuppressWarnings("ConstantValue")
    @Override
    public EntityType<?> getType(@Nullable CompoundTag tag)
    {
        var entityType = super.getType(tag);
        return entityType == null ? this.entityType.get() : getDefaultType();
    }

    @Override
    public FeatureFlagSet requiredFeatures()
    {
        return getDefaultType().requiredFeatures();
    }

    /**
     * Returns spawn egg bound to the given entity type.
     * <p>
     * This method will not work with vanilla spawn eggs or any custom spawn eggs not run through our {@link #registerSpawnEgg(ExtendedSpawnEggItem)} method.
     * <p>
     * Highly recommended to make use of {@link #fromEntityType(EntityType)} rather than this method.
     * <p>
     * This method exists purely for the mixin required for it to function correctly and hook into vanilla as seamlessly as possible.
     *
     * @param entityType Entity type to look up spawn egg for.
     * @return Spawn egg bound to the given entity type.
     * @see #fromEntityType(EntityType)
     */
    @Nullable
    @ApiStatus.Internal
    public static SpawnEggItem Mixin$fromEntityType(@Nullable EntityType<?> entityType)
    {
        return BY_ID.get(entityType);
    }

    /**
     * Returns spawn egg bound to the given entity type.
     * <p>
     * Unlike {@link #Mixin$fromEntityType(EntityType)} this method should work for all types of spawn eggs,
     * as this method calls back both the vanilla & forge look up methods when looking up the spawn egg item.
     * <p>
     * Highly recommended to use this method over {@link #Mixin$fromEntityType(EntityType)}
     *
     * @param entityType Entity type to look up spawn egg for.
     * @return Spawn egg bound to the given entity type.
     */
    public static SpawnEggItem fromEntityType(@Nullable EntityType<?> entityType)
    {
        // vanilla
        var spawnEggItem = SpawnEggItem.byId(entityType);
        if(spawnEggItem != null) return spawnEggItem;

        // forge
        spawnEggItem = forgeSpawnEggTypeMap.get().get(entityType);
        if(spawnEggItem != null) return spawnEggItem;

        // modded
        return BY_ID.get(entityType);
    }

    /**
     * Returns iterable of all modded spawn egg items.
     * <p>
     * This method will not work with vanilla spawn eggs or any custom spawn eggs not run through our {@link #registerSpawnEgg(ExtendedSpawnEggItem)} method.
     * <p>
     * Highly recommended to make use of {@link #eggs()} rather than this method.
     * <p>
     * This method exists purely for the mixin required for it to function correctly and hook into vanilla as seamlessly as possible.
     *
     * @return Iterable of all modded spawn egg items.
     */
    @ApiStatus.Internal
    public static Iterable<SpawnEggItem> Mixin$eggs()
    {
        return MOD_EGGS;
    }

    /**
     * Returns iterable of all known spawn eggs.
     *
     * <p>
     * Unlike {@link #Mixin$eggs()} this method should work for all types of spawn eggs,
     * as this method calls back both the vanilla & forge look up methods when looking up the spawn egg items.
     * <p>
     * Highly recommended to use this method over {@link #Mixin$eggs()}.
     *
     * @return Iterable of all known spawn eggs.
     */
    public static Iterable<SpawnEggItem> eggs()
    {
        // vanilla -> forge -> modded
        return Iterables.concat(SpawnEggItem.eggs(), forgeSpawnEggs.get(), Mixin$eggs());
    }

    /**
     * Binds the spawn egg to its entity type by performing necessary registrations.
     * <p>
     * If making use of our {@link EntityBuilder#spawnEgg(EntityBuilder.SpawnEggItemFactory, int, int)  builder system}, this should be done auto-magicly for you, meaning you do not have to call this method.
     * <p>
     * Otherwise, you must call this method yourself, use {@link RegistryEntry#addListener(Consumer)} to register listener to call this method at correct time.
     * <pre>{@code Registrar.get("my_mod_id", Registries.ITEM) // get item registrar, for your mod
     *                 .register("my_spawn_egg_item", () -> new ExtendedSpawnEggItem(MY_ENTITY_TYPE.get(), 0x0, 0x0, new Item.Properties())) // register spawn egg item
     *                 .addListener(ExtendedSpawnEggItem::registerSpawnEgg); // add listener to registry entry to finalize spawn egg registration
     * }</pre>
     *
     * @param spawnEgg Spawn egg to bind to entity type.
     */
    @ApiStatus.Internal
    public static void registerSpawnEgg(ExtendedSpawnEggItem spawnEgg)
    {
        // lookup the forge spawn egg fields
        // these are wrapped in a memorizing supplier
        // so should only happen first time they are invoked upon
        forgeSpawnEggClass.get();
        forgeSpawnEggTypeMap.get();
        forgeSpawnEggs.get();

        // register dispense behavior
        Validate.notNull(spawnEgg);
        DispenserBlock.registerBehavior(spawnEgg, Objects.requireNonNull(spawnEgg.createDispenseItemBehavior()));
        BY_ID.put(Objects.requireNonNull(spawnEgg.entityType.get()), spawnEgg);

        // register color handler
        PhysicalSide.CLIENT.runWhenOn(() -> () -> RegisterColorHandlerHooks.get().registerItemColor(() -> spawnEgg, () -> () -> spawnEgg::getSpawnEggColor));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static Class<? extends SpawnEggItem> findForgeSpawnEggClass()
    {
        try
        {
            return (Class<? extends SpawnEggItem>) Class.forName("net.minecraftforge.common.ForgeSpawnEggItem");
        }
        catch(Throwable t)
        {
            // TODO: add way to detect if running on forge and print throwable with warning
            ApexCore.LOGGER.warn("Failed to find ForgeSpawnEggItem class, Forge SpawnEgg support will be disabled!");
            ApexCore.LOGGER.warn("If we are currently running on Forge this is a bug, otherwise this warning can be safely ignored!");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T findForgeSpawnEggField(String fieldName, Supplier<T> defaultValue)
    {
        try
        {
            var forgeSpawnEggClass = ExtendedSpawnEggItem.forgeSpawnEggClass.get();
            if(forgeSpawnEggClass == null) return defaultValue.get();
            var field = forgeSpawnEggClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(null);
        }
        catch(Throwable t)
        {
            ApexCore.LOGGER.error("Could not find ForgeSpawnEgg.{} field! This is a bug, report this to ApexStudios!", fieldName, t);
            return defaultValue.get();
        }
    }

    private static final class DispenseSpawnEggBehavior implements DispenseItemBehavior
    {
        @Override
        public ItemStack dispense(BlockSource source, ItemStack stack)
        {
            var item = stack.getItem();
            EntityType<?> entityType = null;

            if(item instanceof SpawnEggItem spawnEgg) entityType = spawnEgg.getType(stack.getTag());
            if(entityType == null) return ItemStack.EMPTY;

            var blockState = source.getBlockState();
            var facing = blockState.getOptionalValue(DispenserBlock.FACING).orElse(Direction.NORTH);
            var level = source.getLevel();
            var pos = source.getPos();

            entityType.spawn(level, stack, null, pos.relative(facing), MobSpawnType.DISPENSER, facing != Direction.UP, false);
            stack.shrink(1);
            level.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(blockState));

            return stack;
        }
    }
}
