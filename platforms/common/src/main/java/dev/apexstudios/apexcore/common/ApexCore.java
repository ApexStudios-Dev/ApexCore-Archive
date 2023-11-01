package dev.apexstudios.apexcore.common;

import dev.apexstudios.apexcore.common.platform.ModRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface ApexCore
{
    String ID = "apexcore";
    Logger LOGGER = LogManager.getLogger();
    ModRegistries REGISTRIES = ModRegistries.get(ID);
}
