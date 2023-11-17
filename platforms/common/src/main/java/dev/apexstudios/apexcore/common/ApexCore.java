package dev.apexstudios.apexcore.common;

import dev.apexstudios.apexcore.common.registry.Register;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface ApexCore
{
    String ID = "apexcore";
    Logger LOGGER = LogManager.getLogger();
    Register REGISTER = Register.create(ID);

    default void bootstrap()
    {
        REGISTER.register();
    }
}
