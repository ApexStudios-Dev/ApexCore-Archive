package dev.apexstudios.apexcore.common.registry.generic;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class DeferredSpawnEggItem extends SpawnEggItem
{
    public static final DispenseItemBehavior DEFAULT_DISPENSE_BEHAVIOR = (src, stack) -> {
        var face = src.state().getValue(DispenserBlock.FACING);
        var entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());

        try
        {
            entityType.spawn(src.level(), stack, null, src.pos().relative(face), MobSpawnType.DISPENSER, face != Direction.UP, false);
        }
        catch(Exception e)
        {
            DispenseItemBehavior.LOGGER.error("Error while dispensing spawn egg from dispenser at {}", src.pos(), e);
            return ItemStack.EMPTY;
        }

        stack.shrink(1);
        src.level().gameEvent(GameEvent.ENTITY_PLACE, src.pos(), GameEvent.Context.of(src.state()));
        return stack;
    };

    private static final Map<EntityType<?>, DeferredSpawnEggItem> TYPE_MAP = Maps.newIdentityHashMap();

    private final Holder<EntityType<?>> deferredEntityType;

    public DeferredSpawnEggItem(Holder<EntityType<?>> deferredEntityType, int backgroundColor, int highlightColor, Properties properties)
    {
        super(null, backgroundColor, highlightColor, properties);

        this.deferredEntityType = deferredEntityType;
    }

    @Override
    public EntityType<?> getType(@Nullable CompoundTag tag)
    {
        var type = super.getType(tag);
        return type == null ? deferredEntityType.value() : type;
    }

    // (Neo)Forge override
    protected EntityType<?> getDefaultType()
    {
        return deferredEntityType.value();
    }

    @Override
    public FeatureFlagSet requiredFeatures()
    {
        return deferredEntityType.value().requiredFeatures();
    }

    @ApiStatus.Internal
    @DoNotCall
    public void injectSpawnEggEntry()
    {
        TYPE_MAP.put(deferredEntityType.value(), this);
    }

    @Nullable
    public static SpawnEggItem deferredOnlyById(@Nullable EntityType<?> entityType)
    {
        return entityType == null ? null : TYPE_MAP.get(entityType);
    }

    public static Iterable<SpawnEggItem> deferredOnly()
    {
        return Iterables.unmodifiableIterable(TYPE_MAP.values());
    }

    @FunctionalInterface
    public interface Factory<I extends DeferredSpawnEggItem>
    {
        I create(Holder<EntityType<?>> entityType, int backgroundColor, int highlightColor, Properties properties);
    }
}
