package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Nameable;

import javax.annotation.Nullable;

public interface NameableMutable extends Nameable
{
	void setCustomName(@Nullable TextComponent customName);
}
