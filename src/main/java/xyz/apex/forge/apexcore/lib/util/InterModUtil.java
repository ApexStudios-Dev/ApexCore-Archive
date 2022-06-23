package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.InterModComms;

import xyz.apex.forge.commonality.Mods;

public final class InterModUtil
{
	public static final String FURNITURE_STATION_METHOD = "method:furniture_station";

	public static void sendFurnitureStationResult(String senderModId, ItemStack stack)
	{
		InterModComms.sendTo(senderModId, Mods.FANTASY_FURNITURE, FURNITURE_STATION_METHOD, () -> stack);
	}
}
