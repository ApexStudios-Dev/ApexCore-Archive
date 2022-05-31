package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class VectorHelper
{
	public static Vec3 rotate(Vec3 vec, double deg, Direction.Axis axis)
	{
		if(deg == 0D)
			return vec;
		if(vec == Vec3.ZERO)
			return vec;

		var angle = (float) (deg / 180F * Math.PI);
		var sin = Mth.sin(angle);
		var cos = Mth.cos(angle);
		var x = vec.x;
		var y = vec.y;
		var z = vec.z;

		if (axis == Direction.Axis.X)
			return new Vec3(x, y * cos - z * sin, z * cos + y * sin);
		if (axis == Direction.Axis.Y)
			return new Vec3(x * cos + z * sin, y, z * cos - x * sin);
		if (axis == Direction.Axis.Z)
			return new Vec3(x * cos - y * sin, y * cos + x * sin, z);
		return vec;
	}
}
