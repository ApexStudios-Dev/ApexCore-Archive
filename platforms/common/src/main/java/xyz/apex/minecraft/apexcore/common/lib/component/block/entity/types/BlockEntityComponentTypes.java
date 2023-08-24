package xyz.apex.minecraft.apexcore.common.lib.component.block.entity.types;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentType;

@ApiStatus.NonExtendable
public interface BlockEntityComponentTypes
{
    BlockEntityComponentType<NameableBlockEntityComponent> NAMEABLE = NameableBlockEntityComponent.COMPONENT_TYPE;
    BlockEntityComponentType<LockCodeBlockEntityComponent> LOCK_CODE = LockCodeBlockEntityComponent.COMPONENT_TYPE;
    BlockEntityComponentType<LootTableBlockEntityComponent> LOOT_TABLE = LootTableBlockEntityComponent.COMPONENT_TYPE;

    @ApiStatus.Internal
    static void bootstrap()
    {
    }
}
