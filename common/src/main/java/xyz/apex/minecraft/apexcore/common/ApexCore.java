package xyz.apex.minecraft.apexcore.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface ApexCore
{
    String ID = "apexcore";
    Logger LOGGER = LogManager.getLogger();

    default void bootstrap()
    {
    }
}
