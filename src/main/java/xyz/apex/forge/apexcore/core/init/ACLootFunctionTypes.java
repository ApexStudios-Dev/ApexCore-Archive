package xyz.apex.forge.apexcore.core.init;

import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

import xyz.apex.forge.apexcore.lib.loot.ApplyRandomColor;
import xyz.apex.forge.apexcore.lib.util.RegistryHelper;
import xyz.apex.forge.commonality.init.Mods;

public final class ACLootFunctionTypes
{
	public static final LootItemFunctionType APPLY_RANDOM_COLOR = RegistryHelper.registerLootFunction(Mods.APEX_CORE, "apply_random_color", new ApplyRandomColor.Serializer());

	static void bootstrap()
	{
	}
}
