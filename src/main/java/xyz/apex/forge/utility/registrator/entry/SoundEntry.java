package xyz.apex.forge.utility.registrator.entry;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.RegistryObject;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.java.utility.nullness.NonnullSupplier;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public final class SoundEntry extends RegistryEntry<SoundEvent> implements NonnullSupplier<SoundEvent>
{
	public SoundEntry(AbstractRegistrator<?> owner, RegistryObject<SoundEvent> delegate)
	{
		super(owner, delegate);
	}

	public void play(Level level, @Nullable Player player, BlockPos pos, SoundSource category, float pitch, float volume)
	{
		level.playSound(player, pos, asSoundEvent(), category, pitch, volume);
	}

	public void play(Level level, @Nullable Player player, BlockPos pos, SoundSource category, float pitch)
	{
		play(level, player, pos, category, pitch, 1F);
	}

	public void play(Level level, @Nullable Player player, BlockPos pos, SoundSource category)
	{
		play(level, player, pos, category, 1F);
	}

	public void play(Level level, BlockPos pos, SoundSource category, float pitch, float volume)
	{
		play(level, null, pos, category, pitch, volume);
	}

	public void play(Level level, BlockPos pos, SoundSource category, float pitch)
	{
		play(level, pos, category, pitch, 1F);
	}

	public void play(Level level, BlockPos pos, SoundSource category)
	{
		play(level, pos, category, 1F);
	}

	public void play(Level level, @Nullable Player player, double x, double y, double z, SoundSource category, float pitch, float volume)
	{
		level.playSound(player, x, y, z, asSoundEvent(), category, pitch, volume);
	}

	public void play(Level level, @Nullable Player player, double x, double y, double z, SoundSource category, float pitch)
	{
		play(level, player, x, y, z, category, pitch, 1F);
	}

	public void play(Level level, @Nullable Player player, double x, double y, double z, SoundSource category)
	{
		play(level, player, x, y, z, category, 1F);
	}

	public void play(Level level, double x, double y, double z, SoundSource category, float pitch, float volume)
	{
		play(level, null, x, y, z, category, pitch, volume);
	}

	public void play(Level level, double x, double y, double z, SoundSource category, float pitch)
	{
		play(level, x, y, z, category, pitch, 1F);
	}

	public void play(Level level, double x, double y, double z, SoundSource category)
	{
		play(level, x, y, z, category, 1F);
	}

	public void play(Level level, @Nullable Player player, Entity entity, SoundSource category, float pitch, float volume)
	{
		level.playSound(player, entity, asSoundEvent(), category, pitch, volume);
	}

	public void play(Level level, @Nullable Player player, Entity entity, SoundSource category, float pitch)
	{
		play(level, player, entity, category, pitch, 1F);
	}

	public void play(Level level, @Nullable Player player, Entity entity, SoundSource category)
	{
		play(level, player, entity, category, 1F);
	}

	public void play(Level level, Entity entity, SoundSource category, float pitch, float volume)
	{
		play(level, null, entity, category, pitch, volume);
	}

	public void play(Level level, Entity entity, SoundSource category, float pitch)
	{
		play(level, entity, category, pitch, 1F);
	}

	public void play(Level level, Entity entity, SoundSource category)
	{
		play(level, entity, category, 1F);
	}

	public void play(Level level, @Nullable Player player, Entity entity, float pitch, float volume)
	{
		play(level, player, entity, entity.getSoundSource(), pitch, volume);
	}

	public void play(Level level, @Nullable Player player, Entity entity, float pitch)
	{
		play(level, player, entity, pitch, 1F);
	}

	public void play(Level level, @Nullable Player player, Entity entity)
	{
		play(level, player, entity, 1F);
	}

	public void play(Level level, Entity entity, float pitch, float volume)
	{
		play(level, null, entity, pitch, volume);
	}

	public void play(Level level, Entity entity, float pitch)
	{
		play(level, entity, pitch, 1F);
	}

	public void play(Level level, Entity entity)
	{
		play(level, entity, 1F);
	}

	public void play(Entity entity, float pitch, float volume)
	{
		entity.playSound(asSoundEvent(), pitch, volume);
	}

	public void play(Entity entity, float pitch)
	{
		play(entity, pitch, 1F);
	}

	public void play(Entity entity)
	{
		play(entity, 1F);
	}

	public SoundEvent asSoundEvent()
	{
		return get();
	}

	public ResourceLocation getSoundLocation()
	{
		return asSoundEvent().getLocation();
	}

	public static SoundEntry cast(RegistryEntry<SoundEvent> registryEntry)
	{
		return cast(SoundEntry.class, registryEntry);
	}

	public static SoundEntry cast(com.tterrag.registrate.util.entry.RegistryEntry<SoundEvent> registryEntry)
	{
		return cast(SoundEntry.class, registryEntry);
	}
}
