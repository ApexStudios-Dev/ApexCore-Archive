package xyz.apex.minecraft.apexcore.common.lib.component.block.types;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BaseBlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentHolder;
import xyz.apex.minecraft.apexcore.common.lib.component.block.BlockComponentType;
import xyz.apex.minecraft.apexcore.common.lib.helper.InteractionResultHelper;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;
import xyz.apex.minecraft.apexcore.common.lib.multiblock.MultiBlockComponent;

public final class MenuProviderBlockComponent extends BaseBlockComponent
{
    public static final BlockComponentType<MenuProviderBlockComponent> COMPONENT_TYPE = BlockComponentType.register(ApexCore.ID, "menu_provider", MenuProviderBlockComponent::new);

    @Nullable private MenuConstructor menuConstructor;
    private ExtraDataWriter extraData = (level, pos, blockState, buffer) -> { };

    private MenuProviderBlockComponent(BlockComponentHolder componentHolder)
    {
        super(componentHolder);
    }

    public MenuProviderBlockComponent withMenuConstructor(MenuConstructor menuConstructor)
    {
        Validate.isTrue(!isRegistered(), "Can only set MenuConstructor during registration!");

        this.menuConstructor = menuConstructor;
        return this;
    }

    public MenuProviderBlockComponent withExtraData(ExtraDataWriter extraData)
    {
        Validate.isTrue(!isRegistered(), "Can only set ExtraDataWriter during registration!");

        this.extraData = extraData;
        return this;
    }

    @Nullable
    public MenuConstructor getMenuConstructor(BlockGetter level, BlockPos pos, BlockState blockState)
    {
        return getBlockEntity(level, pos, blockState) instanceof MenuConstructor constructor ? constructor : menuConstructor;
    }

    public Component getMenuTitle(BlockGetter level, BlockPos pos, BlockState blockState)
    {
        return getBlockEntity(level, pos, blockState) instanceof Nameable nameable ? nameable.getName() : getGameObject().getName();
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
    {
        var menuConstructor = getMenuConstructor(level, pos, blockState);

        if(menuConstructor == null)
            return InteractionResultHelper.BlockUse.noActionTaken();

        var menuTitle = getMenuTitle(level, pos, blockState);

        return MultiBlockComponent.asRoot(level, pos, blockState, (rootPos, rootBlockState) -> {
            MenuHooks.get().openMenu(
                    player,
                    menuTitle,
                    menuConstructor,
                    buffer -> extraData.write(level, rootPos, rootBlockState, buffer)
            );

            return InteractionResultHelper.BlockUse.succeedAndSwingArmBothSides(level.isClientSide);
        });
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos pos)
    {
        var menuConstructor = getMenuConstructor(level, pos, blockState);

        if(menuConstructor == null)
            return null;

        var menuTitle = getMenuTitle(level, pos, blockState);

        return MultiBlockComponent.asRoot(level, pos, blockState, (rootPos, rootBlockState) -> MenuHooks.get().createMenuProvider(
                menuTitle,
                menuConstructor,
                buffer -> extraData.write(level, rootPos, rootBlockState, buffer)
        ));
    }

    @FunctionalInterface
    public interface ExtraDataWriter
    {
        void write(LevelReader level, BlockPos pos, BlockState blockState, FriendlyByteBuf buffer);
    }
}
