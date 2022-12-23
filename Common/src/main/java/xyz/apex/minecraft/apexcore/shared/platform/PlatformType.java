package xyz.apex.minecraft.apexcore.shared.platform;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public enum PlatformType
{
    FABRIC("fabric", "c"), // for some reason fabric has all compat tags under 'c'
    FORGE("forge"),
    // Internal platform, never used in production
    // internal-private usages only
    @ApiStatus.Internal
    VANILLA("minecraft");

    public final String modId;
    private final String tagNamespace;

    PlatformType(String modId, String tagNamespace)
    {
        this.modId = modId;
        this.tagNamespace = tagNamespace;
    }

    PlatformType(String modId)
    {
        this(modId, modId);
    }

    public <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryType, String name)
    {
        return TagKey.create(registryType, new ResourceLocation(tagNamespace, name));
    }
}
