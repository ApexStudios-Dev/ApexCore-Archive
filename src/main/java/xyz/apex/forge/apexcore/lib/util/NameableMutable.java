package xyz.apex.forge.apexcore.lib.util;

import org.jetbrains.annotations.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;

public interface NameableMutable extends Nameable
{
	void setCustomName(@Nullable Component customName);
}