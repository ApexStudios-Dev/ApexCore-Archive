package dev.apexstudios.apexcore.mcforge.loader;

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
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

public final class McForgePlatform implements Platform
{
    private final PhysicalSide physicalSide = switch(FMLEnvironment.dist)
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case DEDICATED_SERVER -> PhysicalSide.DEDICATED_SERVER;
    };
    private final ModLoader modLoader = new McForgeModLoader();
    private final PlatformFactory factory = new McForgeFactory();
    private final MenuHelper menuHelper = new McForgeMenuHelper();

    public McForgePlatform()
    {
        McForgeEvents.addGenericListener(BlockEntity.class, this::onAttachCapability$BlockEntity);
        McForgeEvents.addGenericListener(ItemStack.class, this::onAttachCapability$ItemStack);
    }

    private void onAttachCapability$BlockEntity(AttachCapabilitiesEvent<BlockEntity> event)
    {
        if(event.getObject() instanceof BlockEntityItemHandlerProvider provider)
        {
            var capability = McForgeItemHandler.forBlockEntity(provider);
            event.addListener(capability::invalidate);
            event.addCapability(new ResourceLocation(ApexCore.ID, "internal/item_handler/block_entity"), capability);
        }
    }

    private void onAttachCapability$ItemStack(AttachCapabilitiesEvent<ItemStack> event)
    {
        var stack = event.getObject();

        if(stack.getItem() instanceof ItemStackHandlerProvider provider)
        {
            var capability = McForgeItemHandler.forItemStack(stack, provider);
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
