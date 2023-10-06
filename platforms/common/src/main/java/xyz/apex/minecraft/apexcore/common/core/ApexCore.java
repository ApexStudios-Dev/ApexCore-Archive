package xyz.apex.minecraft.apexcore.common.core;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.Nullable;
import xyz.apex.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.BlockEntityComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.component.block.types.BlockComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.hook.CreativeModeTabHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.EntityHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.GameRuleHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.MenuHooks;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;
import xyz.apex.minecraft.apexcore.common.lib.multiblock.MultiBlockTypes;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.AbstractRegistrar;
import xyz.apex.minecraft.apexcore.common.lib.registry.factory.MenuFactory;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderTypes;
import xyz.apex.minecraft.apexcore.common.lib.support.SupportManager;

import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface ApexCore
{
    String ID = "apexcore";
    boolean IS_EARLY_BUILD = true; // TODO: Can this be automated somehow?
    Logger LOGGER = LogManager.getLogger();

    @ApiStatus.Internal ApexCore INSTANCE = Services.singleton(ApexCore.class);
    @ApiStatus.Internal ModLoader MOD_LOADER = Services.singleton(ModLoader.class);
    @ApiStatus.Internal CreativeModeTabHooks CREATIVE_MODE_TAB_HOOKS = Services.singleton(CreativeModeTabHooks.class);
    @ApiStatus.Internal EntityHooks ENTITY_HOOKS = Services.singleton(EntityHooks.class);
    @ApiStatus.Internal GameRuleHooks GAME_RULE_HOOKS = Services.singleton(GameRuleHooks.class);
    @ApiStatus.Internal MenuHooks MENU_HOOKS = Services.singleton(MenuHooks.class);

    @ApiStatus.Internal
    @MustBeInvokedByOverriders
    default void bootstrap()
    {
        ApexTags.bootstrap();
        BlockComponentTypes.bootstrap();
        BlockEntityComponentTypes.bootstrap();
        MultiBlockTypes.bootstrap();
        SupportManager.INSTANCE.bootstrap();

        registerGenerators();

        // tell listeners that ApexCore has been initialized
        Services.stream(ApexCoreLoaded.class).forEach(ApexCoreLoaded::onApexCoreLoaded);
    }

    private void registerGenerators()
    {
        var descriptionKey = "pack.%s.description".formatted(ID);

        ProviderTypes.LANGUAGES.addListener(ID, (provider, lookup) -> provider
                .enUS()
                    .add(descriptionKey, "ApexCore")
                .end()
        );

        ProviderTypes.registerDefaultMcMetaGenerator(ID, Component.translatable(descriptionKey));
    }

    @ApiStatus.Internal
    PhysicalSide physicalSide();

    @ApiStatus.Internal
    NetworkManager createNetworkManager(String ownerId);

    @DoNotCall
    @ApiStatus.Internal
    void register(AbstractRegistrar<?> registrar);

    @ApiStatus.Internal
    SpawnEggItem createSpawnEgg(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties);

    @ApiStatus.Internal
    <T extends AbstractContainerMenu> MenuType<T> createMenuType(MenuFactory<T> menuFactory, Supplier<MenuType<T>> selfSupplier);

    @ApiStatus.Internal
    boolean isFakePlayer(@Nullable Entity entity);
}
