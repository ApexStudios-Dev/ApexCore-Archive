package xyz.apex.minecraft.apexcore.common.component.entity.types;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.entity.*;
import xyz.apex.minecraft.apexcore.common.hooks.RegistryHooks;
import xyz.apex.minecraft.apexcore.common.registry.entry.MenuEntry;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class MenuProviderBlockEntityComponent extends BaseBlockEntityComponent implements MenuEntry.ExtendedMenuProvider
{
    public static final BlockEntityComponentType<MenuProviderBlockEntityComponent> COMPONENT_TYPE = BlockEntityComponentType.register(
            new ResourceLocation(ApexCore.ID, "menu_provider"),
            MenuProviderBlockEntityComponent::new
    );

    @Nullable private Supplier<MenuType<? extends AbstractContainerMenu>> menuType;
    @Nullable private ServerMenuConstructor menuConstructor = null;
    private Consumer<FriendlyByteBuf> extraDataWriter = extraData -> {};

    private MenuProviderBlockEntityComponent(BlockEntityComponentHolder holder)
    {
        super(holder);
    }

    public void setMenuType(Supplier<MenuType<? extends AbstractContainerMenu>> menuType, ServerMenuConstructor menuConstructor, Consumer<FriendlyByteBuf> extraDataWriter)
    {
        this.menuType = menuType;
        this.menuConstructor = menuConstructor;
        this.extraDataWriter = extraDataWriter;
    }

    public MenuType<? extends AbstractContainerMenu> getMenuType()
    {
        return Objects.requireNonNull(menuType).get();
    }

    @Override
    public void validate()
    {
        Validate.notNull(menuType, "BlockEntity: '%s' is missing required MenuType!! (use the setter on the component)".formatted(holder.getClass().getName()));
        Validate.notNull(menuConstructor, "BlockEntity: '%s' is missing required ServerMenuConstructor!! (use the setter on the component)".formatted(holder.getClass().getName()));
    }

    @Override
    public Component getDisplayName()
    {
        // use custom name if component exists
        // or default to block name
        return getOptionalComponent(BlockEntityComponentTypes.NAMEABLE).map(NameableBlockEntityComponent::getDisplayName).orElseGet(() -> BaseBlockEntityComponentHolder.getDefaultName(toBlockEntity()));
    }

    @SuppressWarnings("DataFlowIssue") // checked in canOpen
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player)
    {
        if(!canOpen(holder, player)) return null;
        getOptionalComponent(BlockEntityComponentTypes.LOOTABLE).ifPresent(lootable -> lootable.unpackLootTable(player));
        // return menuType.get().create(containerId, playerInventory);
        return menuConstructor.create(containerId, playerInventory, getRequiredComponent(BlockEntityComponentTypes.CONTAINER));
    }

    @Override
    public void writeExtraData(FriendlyByteBuf extraData)
    {
        extraDataWriter.accept(extraData);
    }

    public static boolean canOpen(BlockEntityComponentHolder holder, Player player)
    {
        var menu = holder.getComponent(BlockEntityComponentTypes.MENU_PROVIDER);
        if(menu != null && (menu.menuType == null || menu.menuConstructor == null)) return false;

        if(player.isSpectator()) return true;

        var lockable = holder.getComponent(BlockEntityComponentTypes.LOCKABLE);
        if(lockable != null && lockable.isLocked()) return false;

        var lootable = holder.getComponent(BlockEntityComponentTypes.LOOTABLE);
        if(lootable != null && lootable.getLootTable() != null) return false;

        return true;
    }

    public static InteractionResult tryOpen(BlockEntityComponentHolder holder, Player player, InteractionHand hand)
    {
        // try to unlock before opening
        var lockable = holder.getComponent(BlockEntityComponentTypes.LOCKABLE);

        if(lockable != null)
        {
            lockable.tryUnlock(player, hand);
            if(lockable.isLocked()) return InteractionResult.CONSUME_PARTIAL;
        }

        if(!canOpen(holder, player)) return InteractionResult.PASS;

        var menu = holder.getComponent(BlockEntityComponentTypes.MENU_PROVIDER);
        if(menu == null) return InteractionResult.PASS;
        if(player instanceof ServerPlayer serverPlayer) RegistryHooks.getInstance().openMenu(serverPlayer, menu);
        return InteractionResult.SUCCESS;
    }

    @FunctionalInterface
    public interface ServerMenuConstructor
    {
        AbstractContainerMenu create(int containerId, Inventory playerInventory, Container container);

        default MenuConstructor toVanilla(Container container)
        {
            return (containerId, playerInventory, player) -> create(containerId, playerInventory, container);
        }
    }
}
