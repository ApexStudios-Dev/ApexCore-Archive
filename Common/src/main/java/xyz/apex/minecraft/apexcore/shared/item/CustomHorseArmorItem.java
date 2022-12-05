package xyz.apex.minecraft.apexcore.shared.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.HorseArmorItem;

public class CustomHorseArmorItem extends HorseArmorItem
{
    protected final ResourceLocation texturePath;

    @Deprecated
    public CustomHorseArmorItem(int protection, String texturePath, Properties properties)
    {
        super(protection, texturePath, properties);

        this.texturePath = new ResourceLocation(texturePath);
    }

    public CustomHorseArmorItem(int protection, ResourceLocation texturePath, Properties properties)
    {
        super(protection, texturePath.getPath(), properties);

        this.texturePath = texturePath;
    }

    public CustomHorseArmorItem(int protection, String namespace, String horseArmorType, Properties properties)
    {
        this(protection, texturePath(namespace, horseArmorType), properties);
    }

    @Override
    public ResourceLocation getTexture()
    {
        return texturePath;
    }

    public static ResourceLocation texturePath(String namespace, String horseArmorType)
    {
        return new ResourceLocation(namespace, "textures/entity/horse/armor/horse_armor_%s.png".formatted(horseArmorType));
    }
}
