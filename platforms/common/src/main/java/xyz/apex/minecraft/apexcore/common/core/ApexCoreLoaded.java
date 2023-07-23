package xyz.apex.minecraft.apexcore.common.core;

/**
 * Service interface invoked after ApexCore has initialized.
 * <p>
 * Best used to ensure code fires after ApexCore has loaded no matter the ModLoader.
 * <p>
 * Fabric mod load order is not reliable unlike NeoForge.
 */
public interface ApexCoreLoaded
{
    void onApexCoreLoaded();
}
