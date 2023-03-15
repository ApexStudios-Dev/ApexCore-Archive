package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import org.jetbrains.annotations.Nullable;

public interface NameableMutable extends Nameable
{
	void setCustomName(@Nullable Component customName);
}
