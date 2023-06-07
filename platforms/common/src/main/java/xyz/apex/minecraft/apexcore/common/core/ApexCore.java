package xyz.apex.minecraft.apexcore.common.core;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@ApiStatus.NonExtendable
public class ApexCore
{
    public static final String ID = "apexcore";

    @Nullable private static ApexCore INSTANCE = null;

    protected ApexCore()
    {
        Validate.isTrue(INSTANCE == null);
        INSTANCE = this;
    }

    public static ApexCore get()
    {
        return Objects.requireNonNull(INSTANCE);
    }
}
