package xyz.apex.forge.apexcore.lib.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.mutable.MutableObject;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import xyz.apex.forge.apexcore.lib.util.VectorHelper;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public final class VoxelShaper
{
	public static final Function<Direction, Vec3> DEFAULT_ROTATION_VALUES = dir ->
			new Vec3(
					dir == Direction.UP ? 0D : (Direction.Plane.VERTICAL.test(dir) ? 180D : 90D),
					-horizontalAngleFromDirection(dir),
					0D
			);

	public static final Function<Direction, Vec3> HORIZONTAL_ROTATION_VALUES = dir ->
			new Vec3(
					0D,
					-horizontalAngleFromDirection(dir),
					0D
			);

	private final Map<Direction, VoxelShape> shapes = Maps.newEnumMap(Direction.class);

	public VoxelShape get(Direction facing)
	{
		return shapes.containsKey(facing) ? shapes.get(facing) : Shapes.block();
	}

	public VoxelShape get(Direction.Axis axis)
	{
		return get(axisAsFace(axis));
	}

	public VoxelShaper withVerticalShapes(VoxelShape source)
	{
		return with(source, Direction.UP).with( rotatedCopy(source, new Vec3(180D, 0D, 0D)), Direction.DOWN);
	}

	public VoxelShaper with(VoxelShape source, Direction facing)
	{
		shapes.put(facing, source);
		return this;
	}

	public static Direction axisAsFace(Direction.Axis axis)
	{
		return Direction.get(Direction.AxisDirection.POSITIVE, axis);
	}

	public static float horizontalAngleFromDirection(Direction direction)
	{
		return (float) ((Math.max(direction.get2DDataValue(), 0) & 3) * 90);
	}

	public static VoxelShaper forHorizontal(VoxelShape source, Direction facing)
	{
		return forDirectionsWithRotation(source, facing, Direction.Plane.HORIZONTAL, HORIZONTAL_ROTATION_VALUES);
	}

	public static VoxelShaper forHorizontalAxis(VoxelShape source, Direction.Axis along)
	{
		return forDirectionsWithRotation(source, axisAsFace(along), Arrays.asList(Direction.SOUTH, Direction.EAST), HORIZONTAL_ROTATION_VALUES);
	}

	public static VoxelShaper forDirectional(VoxelShape source, Direction facing)
	{
		return forDirectionsWithRotation(source, facing, Lists.newArrayList(Direction.values()), DEFAULT_ROTATION_VALUES);
	}

	public static VoxelShaper forAxis(VoxelShape source, Direction.Axis axis)
	{
		return forDirectionsWithRotation(source, axisAsFace(axis), Arrays.asList(Direction.SOUTH, Direction.EAST, Direction.UP), DEFAULT_ROTATION_VALUES);
	}

	public static VoxelShaper forDirectionsWithRotation(VoxelShape source, Direction facing, Iterable<Direction> directions, Function<Direction, Vec3> usingValues)
	{
		var shaper = new VoxelShaper();

		for(var direction : directions)
		{
			shaper.with(rotate(source, facing, direction, usingValues), direction);
		}

		return shaper;
	}

	public static VoxelShape rotate(VoxelShape source, Direction from, Direction to, Function<Direction, Vec3> usingValues)
	{
		if(from == to)
			return source;

		return rotatedCopy(source, usingValues.apply(from).reverse().add(usingValues.apply(to)));
	}

	public static VoxelShape rotatedCopy(VoxelShape source, Vec3 rotation)
	{
		if(rotation.equals(Vec3.ZERO))
			return source;

		var result = new MutableObject<>(Shapes.empty());
		var center = new Vec3(8, 8, 8);

		source.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
			var v1 = new Vec3(x1, y1, z1).scale(16D).subtract(center);
			var v2 = new Vec3(x2, y2, z2).scale(16D).subtract(center);

			v1 = VectorHelper.rotate(v1, (float) rotation.x, Direction.Axis.X);
			v1 = VectorHelper.rotate(v1, (float) rotation.y, Direction.Axis.Y);
			v1 = VectorHelper.rotate(v1, (float) rotation.z, Direction.Axis.Z).add(center);

			v2 = VectorHelper.rotate(v2, (float) rotation.x, Direction.Axis.X);
			v2 = VectorHelper.rotate(v2, (float) rotation.y, Direction.Axis.Y);
			v2 = VectorHelper.rotate(v2, (float) rotation.z, Direction.Axis.Z).add(center);

			var rotated = box(v1, v2);
			result.setValue(Shapes.or(result.getValue(), rotated));
		});

		return result.getValue();
	}

	public static VoxelShape box(Vec3 v1, Vec3 v2)
	{
		return Block.box(
				Math.min(v1.x, v2.x),
				Math.min(v1.y, v2.y),
				Math.min(v1.z, v2.z),

				Math.max(v1.x, v2.x),
				Math.max(v1.y, v2.y),
				Math.max(v1.z, v2.z)
		);
	}

	public static VoxelShape join(BooleanOp function, VoxelShape... shapes)
	{
		var result = Shapes.empty();

		for(var shape : shapes)
		{
			result = Shapes.join(result, shape, function);
		}

		return result.optimize();
	}

	public static VoxelShape or(VoxelShape... shapes)
	{
		return join(BooleanOp.OR, shapes);
	}
}
