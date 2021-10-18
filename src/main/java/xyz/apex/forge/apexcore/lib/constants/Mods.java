package xyz.apex.forge.apexcore.lib.constants;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.versions.forge.ForgeVersion;
import xyz.apex.forge.apexcore.core.ApexCore;

import java.util.concurrent.ExecutionException;

public enum Mods
{
	APEX_CORE(ApexCore.ID),

	FANTASY_COTTAGE("fantasycottage"),

	FORGE(ForgeVersion.MOD_ID),
	MINECRAFT("minecraft"),

	JEI("jei"),
	TOP("theoneprobe"),

	TERRAFORGED("terraforged"),
	;

	public final String modId;
	private final Cache<String, ResourceLocation> resourceLocationCache = CacheBuilder.newBuilder().weakValues().build();

	Mods(String modId)
	{
		this.modId = modId;
	}

	public String getModId()
	{
		return modId;
	}

	public String getDisplayName()
	{
		return ModList.get().getModContainerById(modId).map(mc -> mc.getModInfo().getDisplayName()).orElse(modId);
	}

	public boolean isLoaded()
	{
		return ModList.get().isLoaded(modId);
	}

	public ResourceLocation id(String name)
	{
		try
		{
			return resourceLocationCache.get(name, () -> new ResourceLocation(modId, name));
		}
		catch(ExecutionException e)
		{
			return new ResourceLocation(modId, name);
		}
	}
}
