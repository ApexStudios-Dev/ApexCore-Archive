package xyz.apex.minecraft.apexcore.mcforge.core;

import com.google.common.collect.Maps;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.component.block.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.mcforge.lib.EventBusHelper;

import java.util.Map;

@ApiStatus.Internal
final class MinecraftForgeEvents
{
    private static final ResourceLocation CAPABILITY_NAME = new ResourceLocation(ApexCore.ID, "capabilities");

    public static void register()
    {
        EventBusHelper.addGenericListener(MinecraftForge.EVENT_BUS, BlockEntity.class, MinecraftForgeEvents::onAttachBlockEntityCapability);
    }

    private static void onAttachBlockEntityCapability(AttachCapabilitiesEvent<BlockEntity> event)
    {
        if(!(event.getObject() instanceof BlockEntityComponentHolder componentHolder))
            return;

        class ComponentCapabilityProvider implements ICapabilityProvider
        {
            private final Map<Capability<?>, LazyOptional<?>> capabilities = Maps.newHashMap();
            private boolean lookedUp = false;

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side)
            {
                lookupCapabilities();
                var cached = capabilities.get(capability);

                if(cached != null)
                    return cached.cast();

                return LazyOptional.empty();
            }

            private void lookupCapabilities()
            {
                if(lookedUp)
                    return;

                // TODO: Figure this out
                // All our block entities are `containers` but that does not mean they may contain slots
                // each component as the possibility to provide a number of slots
                // merged down into a large container using `CompositeContainer`
                // register if at least 1 slot exists
                // componentHolder.findComponent(BlockEntityComponentTypes.INVENTORY).ifPresent(component -> capabilities.put(ForgeCapabilities.ITEM_HANDLER, LazyOptional.of(() -> new InvWrapper(component))));

                lookedUp = true;
            }

            private void invalidate()
            {
                capabilities.values().forEach(LazyOptional::invalidate);
                capabilities.clear();
                lookedUp = false;
            }
        }

        var provider = new ComponentCapabilityProvider();
        event.addCapability(CAPABILITY_NAME, provider);
        event.addListener(provider::invalidate);
    }
}
