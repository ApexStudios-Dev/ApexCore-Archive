package xyz.apex.minecraft.testmod.shared;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class TestBlock extends Block
{
    public TestBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(player instanceof ServerPlayer serverPlayer) TestMod.TEST_MENU.open(serverPlayer, Component.literal("Test Menu"), data -> data.writeBlockPos(pos).writeResourceKey(level.dimension()));
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
    {
        return TestMod.TEST_MENU.asProvider(Component.literal("Test Menu"), data -> data.writeBlockPos(pos).writeResourceKey(level.dimension()));
    }
}
