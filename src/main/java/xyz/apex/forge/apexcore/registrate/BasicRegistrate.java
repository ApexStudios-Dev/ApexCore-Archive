package xyz.apex.forge.apexcore.registrate;

import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;

import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("unused")
public final class BasicRegistrate extends AbstractRegistrate<BasicRegistrate>
{
	private BasicRegistrate(String modId)
	{
		super(modId);
	}

	public static BasicRegistrate create(String modId, NonNullUnaryOperator<BasicRegistrate> modifier)
	{
		return modifier.apply(new BasicRegistrate(modId)).registerEventListeners(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static BasicRegistrate create(String modId)
	{
		return create(modId, NonNullUnaryOperator.identity());
	}

	public static Lazy<BasicRegistrate> lazy(String modId, NonNullUnaryOperator<BasicRegistrate> modifier)
	{
		return Lazy.of(() -> create(modId, modifier));
	}

	public static Lazy<BasicRegistrate> lazy(String modId)
	{
		return lazy(modId, NonNullUnaryOperator.identity());
	}
}