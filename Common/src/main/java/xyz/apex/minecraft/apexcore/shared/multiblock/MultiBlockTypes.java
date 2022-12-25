package xyz.apex.minecraft.apexcore.shared.multiblock;

public interface MultiBlockTypes
{
    MultiBlockType MB_1x2x1 = builder(1, 2, 1)
            .build();

    static MultiBlockType.Builder builder(int width, int height, int depth)
    {
        return MultiBlockType.builder(width, height, depth);
    }
}
