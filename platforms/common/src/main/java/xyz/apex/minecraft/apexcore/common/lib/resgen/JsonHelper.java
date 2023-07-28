package xyz.apex.minecraft.apexcore.common.lib.resgen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

@ApiStatus.NonExtendable
public interface JsonHelper
{
    static JsonArray toJson(Vector3fc vec)
    {
        var json = new JsonArray();
        json.add(vec.x());
        json.add(vec.y());
        json.add(vec.z());
        return json;
    }

    static JsonArray toJson(Vector4fc vec)
    {
        var json = new JsonArray();
        json.add(vec.x());
        json.add(vec.y());
        json.add(vec.z());
        json.add(vec.w());
        return json;
    }

    static void addTexture(JsonObject json, String key, @Nullable String texture)
    {
        if(texture == null || texture.isBlank())
            return;

        if(texture.charAt(0) == '#')
            json.addProperty(key, texture);
        else
            json.addProperty(key, new ResourceLocation(texture).toString());
    }

    static void addJsonIfNotEmpty(JsonObject json, String key, JsonElement element)
    {
        if(!isEmpty(element, false))
            json.add(key, element);
    }

    static void addJsonIfNotEmpty(JsonArray json, JsonElement element)
    {
        if(!isEmpty(element, false))
            json.add(element);
    }

    static boolean isEmpty(JsonElement element, boolean recurse)
    {
        if(element.isJsonNull())
            return true;
        else if(element.isJsonArray())
        {
            var array = element.getAsJsonArray();

            if(array.isEmpty())
                return true;
            if(!recurse)
                return false;

            for(var subElement : array)
            {
                if(!isEmpty(subElement, recurse))
                    return false;
            }

            return true;
        }
        else if(element.isJsonObject())
        {
            var obj = element.getAsJsonObject();

            if(obj.size() == 0)
                return true;
            if(!recurse)
                return false;

            for(var key : obj.keySet())
            {
                if(!isEmpty(obj.get(key), recurse))
                    return false;
            }

            return true;
        }
        else if(element.isJsonPrimitive())
        {
            var prim = element.getAsJsonPrimitive();

            if(prim.isString())
                return prim.getAsString().isBlank();

            return false;
        }
        else
            return false;
    }
}
