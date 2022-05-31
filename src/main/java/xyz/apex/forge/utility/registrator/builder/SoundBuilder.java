package xyz.apex.forge.utility.registrator.builder;

import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonnullType;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.SoundDefinition;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.entry.SoundEntry;
import xyz.apex.java.utility.nullness.NonnullUnaryOperator;

@SuppressWarnings("unused")
public final class SoundBuilder<OWNER extends AbstractRegistrator<OWNER>, PARENT> extends RegistratorBuilder<OWNER, SoundEvent, SoundEvent, PARENT, SoundBuilder<OWNER, PARENT>, SoundEntry>
{
	private NonnullUnaryOperator<SoundDefinition> definitionModifier = NonnullUnaryOperator.identity();

	public SoundBuilder(OWNER owner, PARENT parent, String registryName, BuilderCallback callback)
	{
		super(owner, parent, registryName, callback, SoundEvent.class, SoundEntry::new, SoundEntry::cast);

		owner.addSoundGenerator(provider -> provider.add(getRegistryNameFull(), definitionModifier.apply(SoundDefinition.definition())));
	}

	public SoundBuilder<OWNER, PARENT> properties(NonnullUnaryOperator<SoundDefinition> definitionModifier)
	{
		this.definitionModifier = this.definitionModifier.andThen(definitionModifier);
		return this;
	}

	public SoundBuilder<OWNER, PARENT> replace(boolean replace)
	{
		return properties(properties -> properties.replace(replace));
	}

	public SoundBuilder<OWNER, PARENT> subtitle(String subtitle)
	{
		return properties(properties -> properties.subtitle(subtitle));
	}

	public SoundBuilder<OWNER, PARENT> withSound(SoundDefinition.Sound sound)
	{
		return properties(properties -> properties.with(sound));
	}

	public SoundBuilder<OWNER, PARENT> withSound(SoundDefinition.Sound... sounds)
	{
		return properties(properties -> properties.with(sounds));
	}

	public SoundBuilder<OWNER, PARENT> withSound(ResourceLocation soundName, SoundDefinition.SoundType soundType, NonnullUnaryOperator<SoundDefinition.Sound> soundModifier)
	{
		return withSound(soundModifier.apply(SoundDefinition.Sound.sound(soundName, soundType)));
	}

	public SoundBuilder<OWNER, PARENT> withSound(ResourceLocation soundName, NonnullUnaryOperator<SoundDefinition.Sound> soundModifier)
	{
		return withSound(soundName, SoundDefinition.SoundType.SOUND, soundModifier);
	}

	public SoundBuilder<OWNER, PARENT> withSimpleSound(ResourceLocation soundName, SoundDefinition.SoundType soundType)
	{
		return withSound(soundName, soundType, sound -> sound.volume(1F).pitch(1F).weight(1).stream(false).preload(false).attenuationDistance(16));
	}

	public SoundBuilder<OWNER, PARENT> withSimpleSound(ResourceLocation soundName)
	{
		return withSimpleSound(soundName, SoundDefinition.SoundType.SOUND);
	}

	public SoundBuilder<OWNER, PARENT> withSound(String soundName, SoundDefinition.SoundType soundType, NonnullUnaryOperator<SoundDefinition.Sound> soundModifier)
	{
		return withSound(owner.id(soundName), soundType, soundModifier);
	}

	public SoundBuilder<OWNER, PARENT> withSound(String soundName, NonnullUnaryOperator<SoundDefinition.Sound> soundModifier)
	{
		return withSound(owner.id(soundName), soundModifier);
	}

	public SoundBuilder<OWNER, PARENT> withSimpleSound(String soundName, SoundDefinition.SoundType soundType)
	{
		return withSimpleSound(owner.id(soundName), soundType);
	}

	public SoundBuilder<OWNER, PARENT> withSimpleSound(String soundName)
	{
		return withSimpleSound(owner.id(soundName));
	}

	@Override
	protected @NonnullType SoundEvent createEntry()
	{
		return new SoundEvent(owner.id(getRegistryName()));
	}
}
