package dev.apexstudios.apexcore.fabric.platform;

import dev.apexstudios.apexcore.common.platform.ModRegistries;

final class ModRegistriesImpl implements ModRegistries
{
    private final String ownerId;
    private boolean registered = false;

    ModRegistriesImpl(String ownerId)
    {
        this.ownerId = ownerId;
    }

    @Override
    public String ownerId()
    {
        return ownerId;
    }

    public void register()
    {
        if(registered)
            return;

        registered = true;
    }
}
