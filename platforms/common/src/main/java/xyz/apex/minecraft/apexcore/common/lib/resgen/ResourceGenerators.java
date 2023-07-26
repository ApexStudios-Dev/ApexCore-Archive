package xyz.apex.minecraft.apexcore.common.lib.resgen;


import org.jetbrains.annotations.Nullable;
import xyz.apex.lib.Services;

import java.util.Objects;

public final class ResourceGenerators
{
    @Nullable private static ExistingResourceHelper resourceHelper = null;
    private static boolean initialized = false;

    // must be invoked manually per mod during data gen entry points
    public static void initialize()
    {
        if(initialized)
            return;

        ResourceTypes.bootstrap();

        initialized = true;
        resourceHelper = Services.singleton(ExistingResourceHelper.class);
    }

    public static ExistingResourceHelper resourceHelper()
    {
        initialize();
        return Objects.requireNonNull(resourceHelper);
    }
}
