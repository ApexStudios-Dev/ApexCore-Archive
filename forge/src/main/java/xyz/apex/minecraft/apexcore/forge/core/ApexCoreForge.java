package xyz.apex.minecraft.apexcore.forge.core;

import net.minecraftforge.fml.common.Mod;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.forge.lib.EventBuses;

@Mod(ApexCore.ID)
public final class ApexCoreForge implements ApexCore
{
    public ApexCoreForge()
    {
        bootstrap();
        EventBuses.registerForJavaFML();

        // TODO: Fix this, requires components but they are registered after capabilities are attached
        //MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, this::onAttachBlockEntityCapabilities);
    }

    /*private void onAttachBlockEntityCapabilities(AttachCapabilitiesEvent<BlockEntity> event)
    {
        if(!(event.getObject() instanceof BlockEntityComponentHolder holder)) return;

        holder.getOptionalComponent(BlockEntityComponentTypes.MULTI_BLOCK)
                .filter(Predicate.not(MultiBlockEntityComponent::isMaster))
                .ifPresent(component -> event.addCapability(new ResourceLocation(ApexCore.ID, "capability_proxy"), new ICapabilityProvider()
                        {
                            @NotNull
                            @Override
                            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side)
                            {
                                var masterPos = component.getMasterPos();
                                var blockEntity = component.getLevel().getBlockEntity(masterPos);
                                if(blockEntity == null) return LazyOptional.empty();
                                return blockEntity.getCapability(cap, side);
                            }
                        })
                )
        ;
    }*/
}
