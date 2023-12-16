package dev.apexstudios.testmod.common.block;

import com.mojang.serialization.MapCodec;
import dev.apexstudios.apexcore.common.util.MenuHelper;
import dev.apexstudios.testmod.common.ref.TestBlockEntities;
import dev.apexstudios.testmod.common.ref.TestPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public final class BlockWithEntity extends BaseEntityBlock
{
    private static final MapCodec<BlockWithEntity> CODEC = simpleCodec(BlockWithEntity::new);

    private int counter = 0;

    public BlockWithEntity(Properties properties)
    {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec()
    {
        return CODEC;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if(stack.hasCustomHoverName())
            TestBlockEntities.TEST_BLOCK_ENTITY.getOptional(level, pos).ifPresent(blockEntity -> blockEntity.setCustomName(stack.getHoverName()));
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean movedByPiston)
    {
        if(!blockState.is(newBlockState.getBlock()))
        {
            TestBlockEntities.TEST_BLOCK_ENTITY.getOptional(level, pos).ifPresent(blockEntity -> {
                blockEntity.oakInventory.forEach(stack -> Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack));
                blockEntity.birchInventory.forEach(stack -> Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack));
                level.updateNeighbourForOutputSignal(pos, this);
            });

            super.onRemove(blockState, level, pos, newBlockState, movedByPiston);
        }
    }

    // TODO
    /*@Override
    public boolean hasAnalogOutputSignal(BlockState state)
    {
        return super.hasAnalogOutputSignal(state);
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos)
    {
        return AbstractContainerMenu.getRedstoneSignalFromContainer();
    }*/

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState)
    {
        return TestBlockEntities.TEST_BLOCK_ENTITY.create(pos, blockState);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        if(player instanceof ServerPlayer server)
        {
            var count = counter;
            player.displayClientMessage(Component.literal("Server Counter: %s".formatted(count)), false);
            TestPackets.TEST_PACKET_S2C.sendTo(count, server);
            counter++;

            var blockEntity = TestBlockEntities.TEST_BLOCK_ENTITY.getOptional(level, pos).orElseThrow();
            MenuHelper.openMenu(server, blockEntity, blockEntity);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos)
    {
        return TestBlockEntities.TEST_BLOCK_ENTITY.getOptional(level, pos).map(blockEntity -> MenuHelper.wrapMenuProvider(blockEntity, blockEntity)).getRaw();
    }
}
