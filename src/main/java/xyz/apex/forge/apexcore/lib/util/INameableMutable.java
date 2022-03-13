package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public interface INameableMutable extends INameable
{
	void setCustomName(@Nullable ITextComponent customName);
}
