package xyz.apex.minecraft.apexcore.common.lib.resgen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.ApiStatus;

import java.io.FileNotFoundException;
import java.util.List;

@ApiStatus.NonExtendable
public interface ExistingResourceHelper
{
    boolean isEnabled();

    void enable();

    void disable();

    boolean exists(ResourceLocation resourceLocation, ResourceType resourceType);

    boolean exists(GeneratedResource resource);

    void validateExistence(ResourceLocation resourceLocation, ResourceType resourceType);

    void validateExistence(GeneratedResource resource);

    void track(ResourceLocation resourceLocation, ResourceType resourceType);

    void track(GeneratedResource resource);

    Resource getResource(ResourceLocation resourceLocation, ResourceType resourceType) throws FileNotFoundException;

    Resource getResource(GeneratedResource resource) throws FileNotFoundException;

    List<Resource> getResourceStack(ResourceLocation resourceLocation, PackType packType);
}
