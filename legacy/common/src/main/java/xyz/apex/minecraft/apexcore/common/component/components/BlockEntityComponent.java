package xyz.apex.minecraft.apexcore.common.component.components;

import dev.architectury.registry.menu.MenuRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.ComponentBlock;
import xyz.apex.minecraft.apexcore.common.component.ComponentType;
import xyz.apex.minecraft.apexcore.common.component.ComponentTypes;
import xyz.apex.minecraft.apexcore.common.component.SimpleComponent;

import java.util.Optional;
import java.util.function.Supplier;

public final class BlockEntityComponent extends SimpleComponent
{
    public static final ComponentType<BlockEntityComponent> COMPONENT_TYPE = ComponentType.register(
            new ResourceLocation(ApexCore.ID, "block_entity"),
            BlockEntityComponent.class,
            /* Supplier<BlockEntityType<?>> */ Supplier.class
    );

    private final Supplier<BlockEntityType<?>> blockEntityType;

    @ApiStatus.Internal // public cause reflection
    public BlockEntityComponent(ComponentBlock block, Supplier<BlockEntityType<?>> blockEntityType)
    {
        super(block);

        this.blockEntityType = blockEntityType;
    }

    public BlockEntityType<?> getBlockEntityType()
    {
        return blockEntityType.get();
    }

    @Nullable
    public BlockEntity getBlockEntity(BlockGetter level, BlockPos pos)
    {
        return lookupBlockEntity(level, pos).orElse(null);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        return lookupBlockEntity(level, pos)
                .map(blockEntity -> {
                    // TODO: add interface to allow writing custom data to buffer
                    if(player instanceof ServerPlayer serverPlayer) MenuRegistry.openExtendedMenu(serverPlayer, getMenuProvider(blockState, level, pos), data -> data.writeBlockPos(pos));
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }).orElse(InteractionResult.PASS);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos)
    {
        return lookupBlockEntity(level, pos).filter(MenuProvider.class::isInstance).map(MenuProvider.class::cast).orElse(null);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState)
    {
        var multiBlockComponent = getComponent(ComponentTypes.MULTI_BLOCK);
        if(multiBlockComponent != null && !multiBlockComponent.getMultiBlockType().isOrigin(blockState)) return null;
        return blockEntityType.get().create(pos, blockState);
    }

    @Nullable
    public static BlockEntity findBlockEntity(BlockGetter level, BlockPos pos)
    {
        var blockEntity = level.getBlockEntity(pos);
        if(blockEntity != null) return blockEntity;

        var blockState = level.getBlockState(pos);

        if(blockState.getBlock() instanceof ComponentBlock block)
        {
            var multiBlockComponent = block.getComponent(ComponentTypes.MULTI_BLOCK);

            if(multiBlockComponent != null)
            {
                var multiBlockType = multiBlockComponent.getMultiBlockType();

                if(!multiBlockType.isOrigin(blockState))
                {
                    var originPos = multiBlockType.getOriginPos(blockState, pos);
                    return findBlockEntity(level, originPos);
                }
            }
        }

        return null;
    }

    public static Optional<BlockEntity> lookupBlockEntity(BlockGetter level, BlockPos pos)
    {
        return Optional.ofNullable(findBlockEntity(level, pos));
    }
}
