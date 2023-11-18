package dev.apexstudios.apexcore.mcforge.loader;

import dev.apexstudios.apexcore.common.loader.ModLoader;
import dev.apexstudios.apexcore.common.loader.PhysicalSide;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.RegistrationHelper;
import dev.apexstudios.apexcore.common.util.OptionalLike;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

public final class McForgePlatform implements Platform
{
    private final PhysicalSide physicalSide = switch(FMLEnvironment.dist)
    {
        case CLIENT -> PhysicalSide.CLIENT;
        case DEDICATED_SERVER -> PhysicalSide.DEDICATED_SERVER;
    };
    private final ModLoader modLoader = new McForgeModLoader();

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
    public void register(AbstractRegister<?> register)
    {
        ModEvents.addListener(register.getOwnerId(), RegisterEvent.class, event -> {
            var helper = new RegistrationHelper()
            {
                @Override
                public <T> void register(ResourceKey<? extends Registry<T>> registryType, ResourceKey<T> valueKey, T value)
                {
                    event.register(registryType, forgeHelper -> forgeHelper.register(valueKey, value));
                }
            };

            register.onRegister(event.getRegistryKey(), helper);
        });

        ModEvents.addListener(register.getOwnerId(), EventPriority.LOW, RegisterEvent.class, event -> register.onRegisterLate(event.getRegistryKey()));
    }

    @Override
    public void registerColorHandler(String ownerId, ItemLike item, OptionalLike<OptionalLike<ItemColor>> colorHandler)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> ModEvents.addListener(ownerId, RegisterColorHandlersEvent.Item.class, event -> colorHandler.ifPresent(c -> c.ifPresent(handler -> event.register(handler, item)))));
    }

    @Override
    public void registerColorHandler(String ownerId, Block block, OptionalLike<OptionalLike<BlockColor>> colorHandler)
    {
        PhysicalSide.CLIENT.runWhenOn(() -> () -> ModEvents.addListener(ownerId, RegisterColorHandlersEvent.Block.class, event -> colorHandler.ifPresent(c -> c.ifPresent(handler -> event.register(handler, block)))));
    }
}
