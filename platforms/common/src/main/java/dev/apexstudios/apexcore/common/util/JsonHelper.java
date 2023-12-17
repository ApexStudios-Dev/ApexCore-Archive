package dev.apexstudios.apexcore.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

public interface JsonHelper
{
    static void appendIfNotZero(JsonObject root, String key, Vector3fc vec)
    {
        if(!vec.equals(0F, 0F, 0F))
            root.add(key, toJson(vec));
    }

    static void appendIfNotZero(JsonObject root, String key, Vector4fc vec)
    {
        if(!vec.equals(0F, 0F, 0F, 0F))
            root.add(key, toJson(vec));
    }

    static void appendIfNoneNull(JsonObject root, String key, @Nullable JsonElement element, int maxDepth)
    {
        if(!isNull(element, maxDepth))
            root.add(key, element);
    }

    static void appendIfNoneNull(JsonObject root, String key, @Nullable JsonElement element)
    {
        if(!isNull(element))
            root.add(key, element);
    }

    static void appendIfNoneNull(JsonArray root, @Nullable JsonElement element, int maxDepth)
    {
        if(!isNull(element, maxDepth))
            root.add(element);
    }

    static void appendIfNoneNull(JsonArray root, @Nullable JsonElement element)
    {
        if(!isNull(element))
            root.add(element);
    }

    static boolean isNull(@Nullable JsonElement json)
    {
        return isNull(json, -1);
    }

    static boolean isNull(@Nullable JsonElement json, int maxDepth)
    {
        return isNull(json, 0, maxDepth);
    }

    private static boolean isNull(@Nullable JsonElement json, int depth, int maxDepth)
    {
        if(json == null || json.isJsonNull())
            return true;
        else if(json.isJsonObject())
        {
            var obj = json.getAsJsonObject();

            if(obj.isEmpty())
                return true;

            if(maxDepth > 0 && depth <= maxDepth)
            {
                for(var key : obj.keySet())
                {
                    var element = obj.get(key);

                    if(!isNull(element, depth + 1, maxDepth))
                        return false;
                }

                return true;
            }
        }
        else if(json.isJsonArray())
        {
            var array = json.getAsJsonArray();

            if(array.isEmpty())
                return true;

            if(maxDepth > 0 && depth <= maxDepth)
            {
                for(var element : array)
                {
                    if(!isNull(element, depth + 1, maxDepth))
                        return false;
                }

                return true;
            }
        }

        return false;
    }

    static JsonElement nullOrElement(@Nullable JsonElement element, int maxDepth)
    {
        return isNull(element, maxDepth) ? JsonNull.INSTANCE : element;
    }

    static JsonElement nullOrElement(@Nullable JsonElement element)
    {
        return isNull(element) ? JsonNull.INSTANCE : element;
    }

    static JsonArray toJson(Vector3fc vec)
    {
        var root = new JsonArray();

        root.add(vec.x());
        root.add(vec.y());
        root.add(vec.z());

        return root;
    }

    static JsonArray toJson(Vector4fc vec)
    {
        var root = new JsonArray();

        root.add(vec.x());
        root.add(vec.y());
        root.add(vec.z());
        root.add(vec.w());

        return root;
    }
}
