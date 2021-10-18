package xyz.apex.forge.apexcore.lib.constants;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.versions.forge.ForgeVersion;
import xyz.apex.forge.apexcore.core.ApexCore;

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
}
