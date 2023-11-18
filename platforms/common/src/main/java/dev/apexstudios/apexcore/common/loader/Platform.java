package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.SharedConstants;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface Platform
{
    ModLoader modLoader();

    PhysicalSide physicalSide();

    default String gameVersion()
    {
        return SharedConstants.VERSION_STRING;
    }

    default int resourceVersion(PackType packType)
    {
        return switch(packType)
        {
            case CLIENT_RESOURCES -> SharedConstants.RESOURCE_PACK_FORMAT;
            case SERVER_DATA -> SharedConstants.DATA_PACK_FORMAT;
        };
    }

    default boolean isSnapshot()
    {
        return SharedConstants.SNAPSHOT;
    }

    default boolean isProduction()
    {
        return !SharedConstants.IS_RUNNING_IN_IDE;
    }

    boolean runningDataGen();

    void register(AbstractRegister<?> register);

    void registerColorHandler(String ownerId, ItemLike item, OptionalLike<OptionalLike<ItemColor>> colorHandler);

    void registerColorHandler(String ownerId, Block block, OptionalLike<OptionalLike<BlockColor>> colorHandler);

    void registerRenderType(String ownerId, Block block, OptionalLike<OptionalLike<RenderType>> renderType);

    void registerRenderType(String ownerId, Fluid fluid, OptionalLike<OptionalLike<RenderType>> renderType);

    static Platform get()
    {
        return ApexCore.INSTANCE;
    }
}
