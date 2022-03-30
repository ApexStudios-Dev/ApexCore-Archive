package xyz.apex.forge.apexcore.lib.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public final class VectorHelper
{
	public static Vector3d rotate(Vector3d vec, double deg, Direction.Axis axis)
	{
		if(deg == 0D)
			return vec;
		if(vec == Vector3d.ZERO)
			return vec;

		float angle = (float) (deg / 180F * Math.PI);
		double sin = MathHelper.sin(angle);
		double cos = MathHelper.cos(angle);
		double x = vec.x;
		double y = vec.y;
		double z = vec.z;

		if (axis == Direction.Axis.X)
			return new Vector3d(x, y * cos - z * sin, z * cos + y * sin);
		if (axis == Direction.Axis.Y)
			return new Vector3d(x * cos + z * sin, y, z * cos - x * sin);
		if (axis == Direction.Axis.Z)
			return new Vector3d(x * cos - y * sin, y * cos + x * sin, z);
		return vec;
	}
}
