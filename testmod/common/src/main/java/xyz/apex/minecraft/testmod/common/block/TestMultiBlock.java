package xyz.apex.minecraft.testmod.common.block;

import net.minecraft.world.level.block.entity.BlockEntityType;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BaseBlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.component.block.types.BlockComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.multiblock.MultiBlockType;
import xyz.apex.minecraft.testmod.common.TestMod;

public final class TestMultiBlock extends BaseBlockComponentHolder
{
    public TestMultiBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected void registerComponents(BlockComponentRegistrar registrar)
    {
        registrar.register(BlockComponentTypes.WATERLOGGED);
        registrar.register(BlockComponentTypes.HORIZONTAL_FACING);
        registrar.register(BlockComponentTypes.MULTI_BLOCK, component -> component.setMultiBlockType(MultiBlockType.builder().with("X", "X").build()));
        registrar.register(BlockComponentTypes.MENU_PROVIDER);
    }

    @Override
    protected BlockEntityType<?> getBlockEntityType()
    {
        return TestMod.TEST_MULTI_BLOCK_ENTITY.value();
    }
}
