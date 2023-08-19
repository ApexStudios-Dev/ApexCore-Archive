package xyz.apex.minecraft.testmod.common;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import xyz.apex.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;

@ApiStatus.Internal
@ApiStatus.NonExtendable
@SideOnly(PhysicalSide.CLIENT)
public interface TestModClient
{
    TestModClient INSTANCE = Services.singleton(TestModClient.class);

    @MustBeInvokedByOverriders
    default void bootstrap()
    {
    }
}
