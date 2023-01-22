package xyz.apex.minecraft.apexcore.shared.platform;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import xyz.apex.minecraft.apexcore.shared.util.ApexTags;

public enum ModLoader
{
    FORGE("forge", "MinecraftForge"),
    FABRIC("fabric", "c", "Fabric"),
    QUILT("quilt", "Quilt");

    private final String modId;
    private final String tagsNamespace;
    private final String displayName;

    ModLoader(String modId, String tagsNamespace, String displayName)
    {
        this.modId = modId;
        this.tagsNamespace = tagsNamespace;
        this.displayName = displayName;
    }

    ModLoader(String modId, String displayName)
    {
        this(modId, modId, displayName);
    }

    public String getModId()
    {
        return modId;
    }

    public String getTagsNamespace()
    {
        return tagsNamespace;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public ResourceLocation id(String path)
    {
        return new ResourceLocation(modId, path);
    }

    public <T> TagKey<T> tag(ResourceKey<? extends Registry<T>> registryType, String tagName)
    {
        return ApexTags.tag(registryType, tagsNamespace, tagName);
    }

    public boolean is(ModLoader other)
    {
        return this == other;
    }

    public boolean isForge()
    {
        return is(FORGE);
    }

    public boolean isFabric()
    {
        return is(FABRIC);
    }
}
