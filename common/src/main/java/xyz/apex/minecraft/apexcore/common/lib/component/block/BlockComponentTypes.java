package xyz.apex.minecraft.apexcore.common.lib.component.block;

import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.component.block.types.FacingBlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.component.block.types.WaterLoggedBlockComponent;
import xyz.apex.minecraft.apexcore.common.lib.multiblock.MultiBlockComponent;

@ApiStatus.NonExtendable
public interface BlockComponentTypes
{
    BlockComponentType<FacingBlockComponent> FACING = FacingBlockComponent.COMPONENT_TYPE;
    BlockComponentType<WaterLoggedBlockComponent> WATER_LOGGED = WaterLoggedBlockComponent.COMPONENT_TYPE;
    BlockComponentType<MultiBlockComponent> MULTI_BLOCK = MultiBlockComponent.COMPONENT_TYPE;

    /**
     * Registers and returns new block component type.
     *
     * @param ownerId          Owner id to register component type for.
     * @param registrationName Registration name of component type.
     * @param componentFactory Factory used to construct components.
     * @param <T>              Type of component.
     * @return Newly registered item component type.
     */
    static <T extends BlockComponent> BlockComponentType<T> register(String ownerId, String registrationName, BlockComponentFactory<T> componentFactory)
    {
        return BlockComponentType.register(ownerId, registrationName, componentFactory);
    }

    static void bootstrap()
    {
    }
}
