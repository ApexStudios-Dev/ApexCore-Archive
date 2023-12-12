package dev.apexstudios.apexcore.common.generator.common;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import dev.apexstudios.apexcore.common.ApexCore;
import dev.apexstudios.apexcore.common.generator.AbstractResourceGenerator;
import dev.apexstudios.apexcore.common.generator.ProviderType;
import dev.apexstudios.apexcore.common.generator.ResourceGenerator;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;

import java.util.Map;

public final class LanguageGenerator extends AbstractResourceGenerator<LanguageGenerator>
{
    public static final ProviderType<LanguageGenerator> PROVIDER_TYPE = ProviderType.register(ApexCore.ID, "language", LanguageGenerator::new);

    private final Map<String, String> translations = Maps.newTreeMap();

    private LanguageGenerator(String ownerId, PackOutput output)
    {
        super(ownerId, output);
    }

    public LanguageGenerator with(String key, String value)
    {
        translations.put(key, value);
        return this;
    }

    @Override
    protected void generate(CachedOutput cache, HolderLookup.Provider registries)
    {
        var json = new JsonObject();
        translations.forEach(json::addProperty);
        ResourceGenerator.save(cache, json, output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(ownerId).resolve("lang").resolve("en_us.json"));
    }
}
