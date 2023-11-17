package dev.apexstudios.apexcore.common.loader;

import dev.apexstudios.apexcore.common.registry.AbstractRegister;

public interface ApexBootstrapper
{
    void register(AbstractRegister<?> register);
}
