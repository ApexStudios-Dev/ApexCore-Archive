package xyz.apex.minecraft.apexcore.shared.multiblock;

public interface MultiBlockTypes
{
    MultiBlockType MB_1x2x1 = simple(1, 2, 1);
    MultiBlockType MB_2x1x1 = simple(2, 1, 1);
    MultiBlockType MB_2x1x2 = simple(2, 1, 2);
    MultiBlockType MB_2x2x1 = simple(2, 2, 1);
    MultiBlockType MB_1x1x2 = simple(1, 1, 2); //2x1x1 rotated 90 degrees CCW

    static MultiBlockType.Builder builder(int width, int height, int depth)
    {
        return MultiBlockType.builder(width, height, depth);
    }

    static MultiBlockType simple(int width, int height, int depth)
    {
        return builder(width, height, depth).build();
    }
}
