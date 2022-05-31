package xyz.apex.forge.utility.registrator.provider;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.RegistrateProvider;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.LogicalSide;

import xyz.apex.forge.utility.registrator.AbstractRegistrator;
import xyz.apex.forge.utility.registrator.data.ParticleProvider;

public final class RegistrateParticleProvider extends ParticleProvider implements RegistrateProvider
{
	private final AbstractRegistrate<?> registrate;

	public RegistrateParticleProvider(AbstractRegistrate<?> registrate, DataGenerator generator, ExistingFileHelper fileHelper)
	{
		super(generator, fileHelper);

		this.registrate = registrate;
	}

	@Override
	protected void registerParticleDefs()
	{
		registrate.genData(AbstractRegistrator.PARTICLE_PROVIDER, this);
	}

	@Override
	public LogicalSide getSide()
	{
		return LogicalSide.CLIENT;
	}
}
