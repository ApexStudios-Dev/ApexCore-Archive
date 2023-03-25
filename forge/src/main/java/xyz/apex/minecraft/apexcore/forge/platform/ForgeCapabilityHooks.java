package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.apex.minecraft.apexcore.common.ApexCore;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentHolder;
import xyz.apex.minecraft.apexcore.common.component.entity.BlockEntityComponentTypes;

import java.util.Map;
import java.util.function.Function;

final class ForgeCapabilityHooks extends ForgePlatformHolder
{
    ForgeCapabilityHooks(ForgePlatform platform)
    {
        super(platform);

        MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, this::onAttachCapabilities);
    }

    private void onAttachCapabilities(AttachCapabilitiesEvent<BlockEntity> event)
    {
        if(!(event.getObject() instanceof BlockEntityComponentHolder holder)) return;

        var provider = new ComponentCapabilityProvider(holder);
        event.addCapability(new ResourceLocation(ApexCore.ID, "components"), provider);
        event.addListener(provider::invalidate);
    }

    private static final class ComponentCapabilityProvider implements ICapabilityProvider
    {
        private final BlockEntityComponentHolder holder;
        private final Map<Capability<?>, Function<Direction, LazyOptional<?>>> capabilities = Maps.newHashMap();
        private boolean lookedup = false;

        private ComponentCapabilityProvider(BlockEntityComponentHolder holder)
        {
            this.holder = holder;
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side)
        {
            if(!lookedup) lookupCapabilities();
            var cached = capabilities.get(capability);
            if(cached != null) return cached.apply(side).cast();
            return LazyOptional.empty();
        }

        private void lookupCapabilities()
        {
            if(lookedup) return;

            var container = holder.getComponent(BlockEntityComponentTypes.CONTAINER);
            if(container != null) capabilities.put(ForgeCapabilities.ITEM_HANDLER, Util.memoize(side -> LazyOptional.of(() -> new SidedInvWrapper(container, side))));

            lookedup = true;
        }

        private void invalidate()
        {
            for(var side : Direction.values())
            {
                for(var entry : capabilities.entrySet())
                {
                    entry.getValue().apply(side).invalidate();
                }
            }

            capabilities.clear();
            lookedup = false;
        }
    }
}
