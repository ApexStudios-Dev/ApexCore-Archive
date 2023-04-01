package xyz.apex.minecraft.apexcore.common.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class VoxelShapeCacher
{
    private final Cache<BlockState, VoxelShape> shapeCache = CacheBuilder.newBuilder().expireAfterWrite(30L , TimeUnit.MINUTES).build();

    private final ShapeGetter getter;

    public VoxelShapeCacher(ShapeGetter getter)
    {
        this.getter = getter;
    }

    public VoxelShape get(BlockState blockState) throws ExecutionException
    {
        return shapeCache.get(blockState, () -> getter.getShape(blockState));
    }

    public VoxelShape getSafe(BlockState blockState)
    {
        try
        {
            return get(blockState);
        }
        catch(ExecutionException e)
        {
            return Shapes.block();
        }
    }

    @FunctionalInterface
    public interface ShapeGetter
    {
        VoxelShape getShape(BlockState blockState);
    }
}
