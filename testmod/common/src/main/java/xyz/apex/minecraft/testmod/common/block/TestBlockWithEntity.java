package xyz.apex.minecraft.testmod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.lib.helper.InteractionResultHelper;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;
import xyz.apex.minecraft.testmod.common.TestMod;
import xyz.apex.minecraft.testmod.common.block.entity.TestBlockEntity;

public final class TestBlockWithEntity extends BaseEntityBlock
{
    public TestBlockWithEntity(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(level.getBlockEntity(pos) instanceof TestBlockEntity blockEntity)
            return MenuHooks.get().openMenu(player, blockEntity.getDisplayName(), blockEntity, buffer -> buffer.writeBlockPos(pos));
        return InteractionResultHelper.BlockUse.noActionTaken();
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos)
    {
        var blockEntity = TestMod.TEST_BLOCK_ENTITY.value().getBlockEntity(level, pos);
        return blockEntity == null ? null : blockEntity.createMenuProvider();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(stack.hasCustomHoverName())
            TestMod.TEST_BLOCK_ENTITY.getBlockEntityOptional(level, pos).ifPresent(blockEntity -> blockEntity.setCustomName(stack.getHoverName()));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState)
    {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState)
    {
        return TestMod.TEST_BLOCK_ENTITY.get().create(pos, blockState);
    }
}
