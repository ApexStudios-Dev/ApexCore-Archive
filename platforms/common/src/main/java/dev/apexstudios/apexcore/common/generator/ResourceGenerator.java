package dev.apexstudios.apexcore.common.generator;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import dev.apexstudios.apexcore.common.ApexCore;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public interface ResourceGenerator
{
    static <T> void save(CachedOutput cache, Codec<T> codec, @Nullable T value, Path path)
    {
        save(cache, encodeOrThrow(codec, value), path);
    }

    static void save(CachedOutput cache, JsonElement json, Path path)
    {
        try(var baos = new ByteArrayOutputStream();
            var hos = new HashingOutputStream(Hashing.sha1(), baos);
            var writer = new JsonWriter(new OutputStreamWriter(hos, StandardCharsets.UTF_8))
        )
        {
            writer.setSerializeNulls(false);
            writer.setIndent("    ");
            GsonHelper.writeValue(writer, json, DataProvider.KEY_COMPARATOR);
            writer.close();
            cache.writeIfNeeded(path, baos.toByteArray(), hos.hash());
        }
        catch(IOException e)
        {
            ApexCore.LOGGER.error("Failed to save file to {}", path, e);
        }
    }

    static <T, R> R encodeOrThrow(Codec<T> codec, DynamicOps<R> dynamicOps, @Nullable T value)
    {
        return Util.getOrThrow(codec.encodeStart(dynamicOps, value), IllegalStateException::new);
    }

    static <T> JsonElement encodeOrThrow(Codec<T> codec, @Nullable T value)
    {
        return encodeOrThrow(codec, JsonOps.INSTANCE, value);
    }
}
