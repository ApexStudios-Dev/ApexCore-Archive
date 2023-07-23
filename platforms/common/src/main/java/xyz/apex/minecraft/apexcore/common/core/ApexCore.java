package xyz.apex.minecraft.apexcore.common.core;

import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import xyz.apex.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types.BlockEntityComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.component.block.types.BlockComponentTypes;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;
import xyz.apex.minecraft.apexcore.common.lib.event.types.EntityEvents;
import xyz.apex.minecraft.apexcore.common.lib.hook.*;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;
import xyz.apex.minecraft.apexcore.common.lib.multiblock.MultiBlockTypes;
import xyz.apex.minecraft.apexcore.common.lib.network.NetworkManager;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;

@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface ApexCore
{
    String ID = "apexcore";
    Logger LOGGER = LogManager.getLogger();
    ApexCore INSTANCE = Services.singleton(ApexCore.class);

    ModLoader MOD_LOADER = Services.singleton(ModLoader.class);

    CreativeModeTabHooks CREATIVE_MODE_TAB_HOOKS = Services.singleton(CreativeModeTabHooks.class);
    EntityHooks ENTITY_HOOKS = Services.singleton(EntityHooks.class);
    GameRuleHooks GAME_RULE_HOOKS = Services.singleton(GameRuleHooks.class);
    MenuHooks MENU_HOOKS = Services.singleton(MenuHooks.class);
    RegistryHooks REGISTRY_HOOKS = Services.singleton(RegistryHooks.class);

    @MustBeInvokedByOverriders
    default void bootstrap()
    {
        // common check for every platform
        // all fake players *should* extend the ServerPlayer class
        // forge & fabric register their own listeners to check instanceof directly on their fake player classes
        //
        // platform listeners are registered before this one
        // meaning the platform specific check should happen first before this one
        EntityEvents.IS_FAKE_PLAYER.addListener(entity -> entity instanceof ServerPlayer && entity.getClass() != ServerPlayer.class);
        ApexTags.bootstrap();
        EventType.bootstrap();
        BlockComponentTypes.bootstrap();
        BlockEntityComponentTypes.bootstrap();
        MultiBlockTypes.bootstrap();

        ApexCoreTests.register();
        RegistrarManager.register(ID);
    }

    PhysicalSide physicalSide();

    NetworkManager createNetworkManager(String ownerId);
}
