package xyz.apex.forge.utility.registrator.provider;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.fml.LogicalSide;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class RegistrateSoundProvider extends SoundDefinitionsProvider implements RegistrateProvider
{
	private final AbstractRegistrate<?> registrate;

	public RegistrateSoundProvider(AbstractRegistrate<?> registrate, DataGenerator generator, ExistingFileHelper helper)
	{
		super(generator, registrate.getModid(), helper);

		this.registrate = registrate;
	}

	@Override
	public void registerSounds()
	{
		registrate.genData(AbstractRegistrator.SOUNDS_PROVIDER, this);
	}

	@Override
	public LogicalSide getSide()
	{
		return LogicalSide.SERVER;
	}

	@Override
	public String getName()
	{
		return "SoundDefinitions";
	}

	@Override
	public void add(Supplier<SoundEvent> soundEvent, SoundDefinition definition)
	{
		super.add(soundEvent, definition);
	}

	@Override
	public void add(SoundEvent soundEvent, SoundDefinition definition)
	{
		super.add(soundEvent, definition);
	}

	@Override
	public void add(ResourceLocation soundEvent, SoundDefinition definition)
	{
		super.add(soundEvent, definition);
	}

	@Override
	public void add(String soundEvent, SoundDefinition definition)
	{
		super.add(soundEvent, definition);
	}

	public static SoundDefinition definition()
	{
		return SoundDefinitionsProvider.definition();
	}

	public static SoundDefinition.Sound sound(ResourceLocation name, SoundDefinition.SoundType type)
	{
		return SoundDefinitionsProvider.sound(name, type);
	}

	public static SoundDefinition.Sound sound(ResourceLocation name)
	{
		return SoundDefinitionsProvider.sound(name, SoundDefinition.SoundType.SOUND);
	}

	public static SoundDefinition.Sound sound(String name, SoundDefinition.SoundType type)
	{
		return SoundDefinitionsProvider.sound(new ResourceLocation(name), type);
	}

	public static SoundDefinition.Sound sound(String name)
	{
		return SoundDefinitionsProvider.sound(new ResourceLocation(name));
	}
}
