package dev.apexstudios.apexcore.neoforge.loader;

import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.inventory.BlockEntityItemHandlerProvider;
import dev.apexstudios.apexcore.common.inventory.ItemStackHandlerProvider;
import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.loader.PlatformFactory;
import dev.apexstudios.apexcore.common.util.MenuHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;

public final class NeoForgePlatform implements Platform
{
    private final PhysicalSide physicalSide = switch(FMLEnvironment.dist)
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case DEDICATED_SERVER -> PhysicalSide.DEDICATED_SERVER;
    };
    private final ModLoader modLoader = new NeoForgeModLoader();
    private final PlatformFactory factory = new NeoForgeFactory();
    private final MenuHelper menuHelper = new NeoForgeMenuHelper();

    public NeoForgePlatform()
    {
        NeoForge.EVENT_BUS.addGenericListener(BlockEntity.class, this::onAttachCapability$BlockEntity);
        NeoForge.EVENT_BUS.addGenericListener(ItemStack.class, this::onAttachCapability$ItemStack);
    }

    private void onAttachCapability$BlockEntity(AttachCapabilitiesEvent<BlockEntity> event)
    {
        if(event.getObject() instanceof BlockEntityItemHandlerProvider provider)
        {
            var capability = NeoForgeItemHandler.forBlockEntity(provider);
            event.addListener(capability::invalidate);
            event.addCapability(new ResourceLocation(ApexCore.ID, "internal/item_handler/block_entity"), capability);
        }
    }

    private void onAttachCapability$ItemStack(AttachCapabilitiesEvent<ItemStack> event)
    {
        var stack = event.getObject();

        if(stack.getItem() instanceof ItemStackHandlerProvider provider)
        {
            var capability = NeoForgeItemHandler.forItemStack(stack, provider);
            event.addListener(capability::invalidate);
            event.addCapability(new ResourceLocation(ApexCore.ID, "internal/item_handler/item_stack"), capability);
        }
    }

    @Override
    public ModLoader modLoader()
    {
        return modLoader;
    }

    @Override
    public PhysicalSide physicalSide()
    {
        return physicalSide;
    }

    @Override
    public boolean isProduction()
    {
        return FMLEnvironment.production;
    }

    @Override
    public boolean runningDataGen()
    {
        return DatagenModLoader.isRunningDataGen();
    }

    @Override
    public PlatformFactory factory()
    {
        return factory;
    }

    @Override
    public MenuHelper menuHelper()
    {
        return menuHelper;
    }
}
