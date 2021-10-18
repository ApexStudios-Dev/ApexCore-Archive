package xyz.apex.forge.apexcore.lib.registrate.providers;

import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.LogicalSide;
import xyz.apex.forge.apexcore.lib.constants.Mods;
import xyz.apex.forge.apexcore.lib.data.template_pool.TemplatePoolProvider;

public class RegistrateTemplatePoolProvider extends TemplatePoolProvider implements RegistrateProvider
{
	public static final ProviderType<RegistrateTemplatePoolProvider> TEMPLATE_POOL = ProviderType.register(Mods.APEX_CORE.modId + ":tempate_pool", (p, e) -> new RegistrateTemplatePoolProvider(p, e.getGenerator()));

	protected final AbstractRegistrate<?> registrate;

	private RegistrateTemplatePoolProvider(AbstractRegistrate<?> registrate, DataGenerator generator)
	{
		super(generator);

		this.registrate = registrate;
	}

	@Override
	protected void registerPools()
	{
		registrate.genData(TEMPLATE_POOL, this);
	}

	@Override
	public LogicalSide getSide()
	{
		return LogicalSide.SERVER;
	}
}
