package dev.apexstudios.apexcore.common;

import dev.apexstudios.apexcore.common.generator.ProviderTypes;
import dev.apexstudios.apexcore.common.loader.Platform;
import dev.apexstudios.apexcore.common.registry.Register;
import dev.apexstudios.apexcore.common.util.Services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

public interface ApexCore
{
    String ID = "apexcore";
    Logger LOGGER = LogManager.getLogger();

    @ApiStatus.Internal
    Platform INSTANCE = Services.singleton(Platform.class);

    Register REGISTER = Register.create(ID);

    default void bootstrap()
    {
        REGISTER.register();
        ProviderTypes.addDefaultPackMetadata(ID, "ApexCore - Client/Server Resources");
    }
}
