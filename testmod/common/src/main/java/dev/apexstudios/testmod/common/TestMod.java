package dev.apexstudios.testmod.common;

import dev.apexstudios.apexcore.common.platform.ModRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface TestMod
{
    String ID = "testmod";
    Logger LOGGER = LogManager.getLogger();
    ModRegistries REGISTRIES = ModRegistries.get(ID);
}
