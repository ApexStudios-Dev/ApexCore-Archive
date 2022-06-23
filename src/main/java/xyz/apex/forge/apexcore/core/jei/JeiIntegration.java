package xyz.apex.forge.apexcore.core.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;

import net.minecraft.resources.ResourceLocation;

import xyz.apex.forge.apexcore.core.init.ACRegistry;
import xyz.apex.forge.apexcore.core.init.PlayerPlushie;

@JeiPlugin
public final class JeiIntegration implements IModPlugin
{
	private final ResourceLocation pluginName = ACRegistry.INSTANCE.id("jei_plugin");

	@Override
	public ResourceLocation getPluginUid()
	{
		return pluginName;
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration registration)
	{
		registration.useNbtForSubtypes(PlayerPlushie.PLAYER_PLUSHIE_BLOCK.asItem());
	}
}
