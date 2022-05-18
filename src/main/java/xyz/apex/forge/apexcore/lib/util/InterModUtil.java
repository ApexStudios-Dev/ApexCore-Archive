package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.InterModComms;

public final class InterModUtil
{
	public static final String FANTASY_FURNITURE_ID = "fantasyfurniture";
	public static final String FURNITURE_STATION_METHOD = "method:furniture_station";

	public static void sendFurnitureStationResult(String senderModId, ItemStack stack)
	{
		InterModComms.sendTo(senderModId, FANTASY_FURNITURE_ID, FURNITURE_STATION_METHOD, () -> stack);
	}
}
