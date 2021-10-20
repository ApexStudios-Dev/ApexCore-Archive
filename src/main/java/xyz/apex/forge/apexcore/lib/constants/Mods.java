package xyz.apex.forge.apexcore.lib.constants;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModContainer;
import xyz.apex.forge.apexcore.lib.util.IMod;

import java.util.Optional;

/**
 * @deprecated use {@link IMod}
 */
@Deprecated
public enum Mods implements IMod
{
	APEX_CORE(IMod.APEX_CORE),

	FANTASY_COTTAGE(IMod.FANTASY_COTTAGE),

	FORGE(IMod.FORGE),
	MINECRAFT(IMod.MINECRAFT),

	JEI(IMod.JEI),
	TOP(IMod.TOP),

	TERRAFORGED(IMod.TERRAFORGED),
	;

	public final String modId;
	public final IMod mod;

	Mods(IMod mod)
	{
		this.mod = mod;
		modId = mod.getModId();
	}

	@Override
	public String getModId()
	{
		return mod.getModId();
	}

	@Override
	public boolean isLoaded()
	{
		return mod.isLoaded();
	}

	@Override
	public Optional<? extends ModContainer> getModContainer()
	{
		return mod.getModContainer();
	}

	@Override
	public String getDisplayName()
	{
		return mod.getDisplayName();
	}

	@Override
	public ResourceLocation id(String path)
	{
		return mod.id(path);
	}

	@Override
	public String prefix(String path)
	{
		return mod.prefix(path);
	}
}
