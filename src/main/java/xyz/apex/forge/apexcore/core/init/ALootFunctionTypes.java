package xyz.apex.forge.apexcore.core.init;

import net.minecraft.loot.LootFunctionType;
import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.lib.loot.ApplyRandomColor;
import xyz.apex.forge.apexcore.lib.util.RegistryHelper;

public final class ALootFunctionTypes
{
	public static final LootFunctionType APPLY_RANDOM_COLOR = RegistryHelper.registerLootFunction(ApexCore.ID, "apply_random_color", new ApplyRandomColor.Serializer());

	public static void register() { }
}
