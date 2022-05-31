package xyz.apex.forge.utility.registrator.helper;

import org.apache.logging.log4j.LogManager;

import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import xyz.apex.java.utility.nullness.NonnullSupplier;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
public class ForgeSpawnEggItem<ENTITY extends Entity> extends SpawnEggItem
{
	private static final Field EGGS = ObfuscationReflectionHelper.findField(SpawnEggItem.class, "BY_ID");
	private static final DispenseItemBehavior DEFAULT_DISPENSE_BEHAVIOR = (source, stack) -> {
		var direction = source.getBlockState().getValue(DispenserBlock.FACING);
		var entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());

		try
		{
			entityType.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
		}
		catch(Exception e)
		{
			LogManager.getLogger().error("Error while dispensing spawn egg from dispenser at {}", source.getPos(), e);
			return ItemStack.EMPTY;
		}

		stack.shrink(1);
		source.getLevel().gameEvent(GameEvent.ENTITY_PLACE, source.getPos());
		return stack;
	};

	private boolean registered = false;
	private final NonnullSupplier<EntityType<ENTITY>> entityTypeSupplier;

	public ForgeSpawnEggItem(NonnullSupplier<EntityType<ENTITY>> entityTypeSupplier, int backgroundColor, int highlightColor, Properties properties)
	{
		super(null, backgroundColor, highlightColor, properties);

		this.entityTypeSupplier = entityTypeSupplier;
	}

	@Override
	public EntityType<?> getType(@Nullable CompoundTag tag)
	{
		var entityType = super.getType(tag);
		return entityType == null ? entityTypeSupplier.get() : entityType;
	}

	@Nullable
	protected DispenseItemBehavior getDispenserBehavior()
	{
		return DEFAULT_DISPENSE_BEHAVIOR;
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public final void registerSpawnEggProperties()
	{
		if(registered)
			return;

		try
		{
			// down cast to allow spawn eggs of none mob based entities
			var eggs = (Map<EntityType<?>, SpawnEggItem>) EGGS.get(this);
			eggs.put(entityTypeSupplier.get(), this);
			registered = true;

			var dispenserBehavior = getDispenserBehavior();

			if(dispenserBehavior != null)
				DispenserBlock.registerBehavior(this, dispenserBehavior);
		}
		catch(IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static int getSpawnEggColor(ItemStack stack, int tintIndex)
	{
		var item = stack.getItem();
		return item instanceof SpawnEggItem spawnegg ? spawnegg.getColor(tintIndex) : 0;
	}
}
