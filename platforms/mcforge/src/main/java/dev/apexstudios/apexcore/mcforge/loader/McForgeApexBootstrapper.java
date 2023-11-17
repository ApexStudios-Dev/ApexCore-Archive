package dev.apexstudios.apexcore.mcforge.loader;

import dev.apexstudios.apexcore.common.loader.ApexBootstrapper;
import dev.apexstudios.apexcore.common.registry.AbstractRegister;
import dev.apexstudios.apexcore.common.registry.RegistrationHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.registries.RegisterEvent;

final class McForgeApexBootstrapper implements ApexBootstrapper
{
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
}
