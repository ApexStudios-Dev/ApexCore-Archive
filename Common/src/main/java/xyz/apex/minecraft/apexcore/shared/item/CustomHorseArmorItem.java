package xyz.apex.minecraft.apexcore.shared.item;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;

public class CustomHorseArmorItem extends HorseArmorItem
{
    private final ResourceLocation texturePath;

    public CustomHorseArmorItem(int protection, ResourceLocation texturePath, Properties properties)
    {
        super(protection, extractIdentifier(texturePath), properties);

        this.texturePath = texturePath;
    }

    @Override
    public ResourceLocation getTexture()
    {
        return texturePath;
    }

    public static ResourceLocation constructTexturePath(String modId, String registryName)
    {
        return new ResourceLocation(modId, "textures/entity/horse/armor/horse_armor_%s.png".formatted(registryName));
    }

    private static String extractIdentifier(ResourceLocation texturePath)
    {
        var path = texturePath.getPath();

        // remove everything upto last `/`
        // `textures/entity/horse/armor/`
        path = StringUtils.substringAfterLast(path, "/");

        // remove file ext
        // `.png`
        path = StringUtils.substringBeforeLast(path, ".png");

        // remove `horse_armor_` prefix
        path = StringUtils.removeStart(path, "horse_armor_");

        // use `path` from `texturePath` if string somehow ends up empty
        if(StringUtils.isBlank(path)) return texturePath.getPath();
        return path;
    }
}
