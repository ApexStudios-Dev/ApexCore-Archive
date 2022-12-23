package xyz.apex.minecraft.apexcore.shared.registry;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.item.CustomHorseArmorItem;
import xyz.apex.minecraft.apexcore.shared.registry.entry.ItemEntry;

public interface ItemBuilders
{
    static <T extends Item> ItemBuilder<T> builder(String modId, String registryName, ItemBuilder.ItemFactory<T> factory)
    {
        return new ItemBuilder<>(modId, registryName, factory);
    }

    static ItemBuilder<Item> builder(String modId, String registryName)
    {
        return builder(modId, registryName, Item::new);
    }

    static <T extends Item> ItemEntry<T> simple(String modId, String registryName, ItemBuilder.ItemFactory<T> factory)
    {
        return builder(modId, registryName, factory).register();
    }

    static ItemEntry<Item> simple(String modId, String registryName)
    {
        return builder(modId, registryName, Item::new).register();
    }

    // region: TieredItem
    static <T extends TieredItem> ItemBuilder<T> tiered(String modId, String registryName, Tier tier, TieredItemFactory<T> factory)
    {
        return builder(modId, registryName, properties -> factory.create(tier, properties));
    }

    static ItemBuilder<TieredItem> tiered(String modId, String registryName, Tier tier)
    {
        return tiered(modId, registryName, tier, TieredItem::new);
    }

    static <T extends TieredItem> ItemEntry<T> simpleTiered(String modId, String registryName, Tier tier, TieredItemFactory<T> factory)
    {
        return tiered(modId, registryName, tier, factory).register();
    }

    static ItemEntry<TieredItem> simpleTiered(String modId, String registryName, Tier tier)
    {
        return tiered(modId, registryName, tier).register();
    }

    // region: SwordItem
    static <T extends SwordItem> ItemBuilder<T> sword(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed, SwordItemFactory<T> factory)
    {
        return tiered(modId, registryName, tier, (tier1, properties) -> factory.create(tier1, attackDamage, attackSpeed, properties));
    }

    static ItemBuilder<SwordItem> sword(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed)
    {
        return sword(modId, registryName, tier, attackDamage, attackSpeed, SwordItem::new);
    }

    static <T extends SwordItem> ItemEntry<T> simpleSword(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed, SwordItemFactory<T> factory)
    {
        return sword(modId, registryName, tier, attackDamage, attackSpeed, factory).register();
    }

    static ItemEntry<SwordItem> simpleSword(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed)
    {
        return sword(modId, registryName, tier, attackDamage, attackSpeed).register();
    }
    // endregion

    // region: DiggerItem
    static <T extends DiggerItem> ItemBuilder<T> digger(String modId, String registryName, float attackDamage, float attackSpeed, Tier tier, TagKey<Block> mineableTag, DiggerItemFactory<T> factory)
    {
        return tiered(modId, registryName, tier, (tier1, properties) -> factory.create(attackDamage, attackSpeed, tier1, mineableTag, properties));
    }

    static ItemBuilder<DiggerItem> digger(String modId, String registryName, float attackDamage, float attackSpeed, Tier tier, TagKey<Block> mineableTag)
    {
        return digger(modId, registryName, attackDamage, attackSpeed, tier, mineableTag, DiggerItem::new);
    }

    static <T extends DiggerItem> ItemEntry<T> simpleDigger(String modId, String registryName, float attackDamage, float attackSpeed, Tier tier, TagKey<Block> mineableTag, DiggerItemFactory<T> factory)
    {
        return digger(modId, registryName, attackDamage, attackSpeed, tier, mineableTag, factory).register();
    }

    static ItemEntry<DiggerItem> simpleDigger(String modId, String registryName, float attackDamage, float attackSpeed, Tier tier, TagKey<Block> mineableTag)
    {
        return digger(modId, registryName, attackDamage, attackSpeed, tier, mineableTag).register();
    }

    // region: PickaxeItem
    static <T extends PickaxeItem> ItemBuilder<T> pickaxe(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed, PickaxeItemFactory<T> factory)
    {
        return digger(modId, registryName, attackDamage, attackSpeed, tier, BlockTags.MINEABLE_WITH_PICKAXE, (attackDamage1, attackSpeed1, tier1, $, properties) -> factory.create(tier1, attackDamage, attackSpeed, properties));
    }

    static ItemBuilder<PickaxeItem> pickaxe(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed)
    {
        return pickaxe(modId, registryName, tier, attackDamage, attackSpeed, PickaxeItem::new);
    }

    static <T extends PickaxeItem> ItemEntry<T> simplePickaxe(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed, PickaxeItemFactory<T> factory)
    {
        return pickaxe(modId, registryName, tier, attackDamage, attackSpeed, factory).register();
    }

    static ItemEntry<PickaxeItem> simplePickaxe(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed)
    {
        return pickaxe(modId, registryName, tier, attackDamage, attackSpeed).register();
    }
    // endregion

    // region: AxeItem
    static <T extends AxeItem> ItemBuilder<T> axe(String modId, String registryName, Tier tier, float attackDamage, float attackSpeed, AxeItemFactory<T> factory)
    {
        return digger(modId, registryName, attackDamage, attackSpeed, tier, BlockTags.MINEABLE_WITH_AXE, (attackDamage1, attackSpeed1, tier1, $, properties) -> factory.create(tier1, attackDamage, attackSpeed, properties));
    }

    static ItemBuilder<AxeItem> axe(String modId, String registryName, Tier tier, float attackDamage, float attackSpeed)
    {
        return axe(modId, registryName, tier, attackDamage, attackSpeed, AxeItem::new);
    }

    static <T extends AxeItem> ItemEntry<T> simpleAxe(String modId, String registryName, Tier tier, float attackDamage, float attackSpeed, AxeItemFactory<T> factory)
    {
        return axe(modId, registryName, tier, attackDamage, attackSpeed, factory).register();
    }

    static ItemEntry<AxeItem> simpleAxe(String modId, String registryName, Tier tier, float attackDamage, float attackSpeed)
    {
        return axe(modId, registryName, tier, attackDamage, attackSpeed).register();
    }
    // endregion

    // region: ShovelItem
    static <T extends ShovelItem> ItemBuilder<T> shovel(String modId, String registryName, Tier tier, float attackDamage, float attackSpeed, ShovelItemFactory<T> factory)
    {
        return digger(modId, registryName, attackDamage, attackSpeed, tier, BlockTags.MINEABLE_WITH_SHOVEL, (attackDamage1, attackSpeed1, tier1, $, properties) -> factory.create(tier1, attackDamage, attackSpeed, properties));
    }

    static ItemBuilder<ShovelItem> shovel(String modId, String registryName, Tier tier, float attackDamage, float attackSpeed)
    {
        return shovel(modId, registryName, tier, attackDamage, attackSpeed, ShovelItem::new);
    }

    static <T extends ShovelItem> ItemEntry<T> simpleShovel(String modId, String registryName, Tier tier, float attackDamage, float attackSpeed, ShovelItemFactory<T> factory)
    {
        return shovel(modId, registryName, tier, attackDamage, attackSpeed, factory).register();
    }

    static ItemEntry<ShovelItem> simpleShovel(String modId, String registryName, Tier tier, float attackDamage, float attackSpeed)
    {
        return shovel(modId, registryName, tier, attackDamage, attackSpeed).register();
    }
    // endregion

    // region: HoeItem
    static <T extends HoeItem> ItemBuilder<T> hoe(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed, HoeItemFactory<T> factory)
    {
        return digger(modId, registryName, attackDamage, attackSpeed, tier, BlockTags.MINEABLE_WITH_HOE, (attackDamage1, attackSpeed1, tier1, $, properties) -> factory.create(tier1, attackDamage, attackSpeed, properties));
    }

    static ItemBuilder<HoeItem> hoe(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed)
    {
        return hoe(modId, registryName, tier, attackDamage, attackSpeed, HoeItem::new);
    }

    static <T extends HoeItem> ItemEntry<T> simpleHoe(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed, HoeItemFactory<T> factory)
    {
        return hoe(modId, registryName, tier, attackDamage, attackSpeed, factory).register();
    }

    static ItemEntry<HoeItem> simpleHoe(String modId, String registryName, Tier tier, int attackDamage, float attackSpeed)
    {
        return hoe(modId, registryName, tier, attackDamage, attackSpeed).register();
    }
    // endregion
    // endregion
    // endregion

    // region: ArmorItem
    static <T extends ArmorItem> ItemBuilder<T> armor(String modId, String registryName, ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, ArmorItemFactory<T> factory)
    {
        return builder(modId, registryName, properties -> factory.create(armorMaterial, equipmentSlot, properties));
    }

    static ItemBuilder<ArmorItem> armor(String modId, String registryName, ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot)
    {
        return armor(modId, registryName, armorMaterial, equipmentSlot, ArmorItem::new);
    }

    static <T extends ArmorItem> ItemEntry<T> simpleArmor(String modId, String registryName, ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, ArmorItemFactory<T> factory)
    {
        return armor(modId, registryName, armorMaterial, equipmentSlot, factory).register();
    }

    static ItemEntry<ArmorItem> simpleArmor(String modId, String registryName, ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot)
    {
        return armor(modId, registryName, armorMaterial, equipmentSlot).register();
    }

    // region: Helmet
    static <T extends ArmorItem> ItemBuilder<T> helmet(String modId, String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return armor(modId, registryName, armorMaterial, EquipmentSlot.HEAD, factory);
    }

    static ItemBuilder<ArmorItem> helmet(String modId, String registryName, ArmorMaterial armorMaterial)
    {
        return helmet(modId, registryName, armorMaterial, ArmorItem::new);
    }

    static <T extends ArmorItem> ItemEntry<T> simpleHelmet(String modId, String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return helmet(modId, registryName, armorMaterial, factory).register();
    }

    static ItemEntry<ArmorItem> simpleHelmet(String modId, String registryName, ArmorMaterial armorMaterial)
    {
        return helmet(modId, registryName, armorMaterial).register();
    }
    // endregion

    // region: Chestplate
    static <T extends ArmorItem> ItemBuilder<T> chestplate(String modId, String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return armor(modId, registryName, armorMaterial, EquipmentSlot.CHEST, factory);
    }

    static ItemBuilder<ArmorItem> chestplate(String modId, String registryName, ArmorMaterial armorMaterial)
    {
        return chestplate(modId, registryName, armorMaterial, ArmorItem::new);
    }

    static <T extends ArmorItem> ItemEntry<T> simpleChestplate(String modId, String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return chestplate(modId, registryName, armorMaterial, factory).register();
    }

    static ItemEntry<ArmorItem> simpleChestplate(String modId, String registryName, ArmorMaterial armorMaterial)
    {
        return chestplate(modId, registryName, armorMaterial).register();
    }
    // endregion

    // region: Leggings
    static <T extends ArmorItem> ItemBuilder<T> leggings(String modId, String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return armor(modId, registryName, armorMaterial, EquipmentSlot.LEGS, factory);
    }

    static ItemBuilder<ArmorItem> leggings(String modId, String registryName, ArmorMaterial armorMaterial)
    {
        return leggings(modId, registryName, armorMaterial, ArmorItem::new);
    }

    static <T extends ArmorItem> ItemEntry<T> simpleLeggings(String modId, String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return leggings(modId, registryName, armorMaterial, factory).register();
    }

    static ItemEntry<ArmorItem> simpleLeggings(String modId, String registryName, ArmorMaterial armorMaterial)
    {
        return leggings(modId, registryName, armorMaterial).register();
    }
    // endregion

    // region: Boots
    static <T extends ArmorItem> ItemBuilder<T> boots(String modId, String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return armor(modId, registryName, armorMaterial, EquipmentSlot.FEET, factory);
    }

    static ItemBuilder<ArmorItem> boots(String modId, String registryName, ArmorMaterial armorMaterial)
    {
        return boots(modId, registryName, armorMaterial, ArmorItem::new);
    }

    static <T extends ArmorItem> ItemEntry<T> simpleBoots(String modId, String registryName, ArmorMaterial armorMaterial, ArmorItemFactory<T> factory)
    {
        return boots(modId, registryName, armorMaterial, factory).register();
    }

    static ItemEntry<ArmorItem> simpleBoots(String modId, String registryName, ArmorMaterial armorMaterial)
    {
        return boots(modId, registryName, armorMaterial).register();
    }
    // endregion
    // endregion

    // region: HorseArmorItem
    static <T extends CustomHorseArmorItem> ItemBuilder<T> horseArmor(String modId, String registryName, int protection, ResourceLocation texturePath, HorseArmorItemFactory<T> factory)
    {
        return builder(modId, registryName, properties -> factory.create(protection, texturePath, properties));
    }

    static ItemBuilder<CustomHorseArmorItem> horseArmor(String modId, String registryName, int protection, ResourceLocation texturePath)
    {
        return horseArmor(modId, registryName, protection, texturePath, CustomHorseArmorItem::new);
    }

    static <T extends CustomHorseArmorItem> ItemEntry<T> simpleHorseArmor(String modId, String registryName, int protection, ResourceLocation texturePath, HorseArmorItemFactory<T> factory)
    {
        return horseArmor(modId, registryName, protection, texturePath, factory).register();
    }

    static ItemEntry<CustomHorseArmorItem> simpleHorseArmor(String modId, String registryName, int protection, ResourceLocation texturePath)
    {
        return horseArmor(modId, registryName, protection, texturePath).register();
    }

    static <T extends CustomHorseArmorItem> ItemBuilder<T> horseArmor(String modId, String registryName, int protection, String textureName, HorseArmorItemFactory<T> factory)
    {
        return horseArmor(modId, registryName, protection, CustomHorseArmorItem.constructTexturePath(modId, textureName), factory);
    }

    static ItemBuilder<CustomHorseArmorItem> horseArmor(String modId, String registryName, int protection, String textureName)
    {
        return horseArmor(modId, registryName, protection, textureName, CustomHorseArmorItem::new);
    }

    static <T extends CustomHorseArmorItem> ItemEntry<T> simpleHorseArmor(String modId, String registryName, int protection, String textureName, HorseArmorItemFactory<T> factory)
    {
        return horseArmor(modId, registryName, protection, textureName, factory).register();
    }

    static ItemEntry<CustomHorseArmorItem> simpleHorseArmor(String modId, String registryName, int protection, String textureName)
    {
        return horseArmor(modId, registryName, protection, textureName).register();
    }

    static <T extends CustomHorseArmorItem> ItemBuilder<T> horseArmor(String modId, String registryName, int protection, HorseArmorItemFactory<T> factory)
    {
        // remove `_horse_armor` suffix
        // `lead_horse_armor` -> `lead`
        // if string comes back as empty, default to full registry name
        var textureName = StringUtils.removeEnd(registryName, "_horse_armor");
        textureName = StringUtils.isBlank(textureName) ? registryName : textureName;

        return horseArmor(modId, registryName, protection, textureName, factory);
    }

    static ItemBuilder<CustomHorseArmorItem> horseArmor(String modId, String registryName, int protection)
    {
        return horseArmor(modId, registryName, protection, CustomHorseArmorItem::new);
    }

    static <T extends CustomHorseArmorItem> ItemEntry<T> simpleHorseArmor(String modId, String registryName, int protection, HorseArmorItemFactory<T> factory)
    {
        return horseArmor(modId, registryName, protection, factory).register();
    }

    static ItemEntry<CustomHorseArmorItem> simpleHorseArmor(String modId, String registryName, int protection)
    {
        return horseArmor(modId, registryName, protection).register();
    }
    // endregion

    @FunctionalInterface
    interface TieredItemFactory<T extends TieredItem>
    {
        T create(Tier tier, Item.Properties properties);
    }

    @FunctionalInterface
    interface DiggerItemFactory<T extends DiggerItem>
    {
        T create(float attackDamage, float attackSpeed, Tier tier, TagKey<Block> mineableTag, Item.Properties properties);
    }

    @FunctionalInterface
    interface SwordItemFactory<T extends SwordItem>
    {
        T create(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties);
    }

    @FunctionalInterface
    interface PickaxeItemFactory<T extends PickaxeItem>
    {
        T create(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties);
    }

    @FunctionalInterface
    interface AxeItemFactory<T extends AxeItem>
    {
        T create(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties);
    }

    @FunctionalInterface
    interface ShovelItemFactory<T extends ShovelItem>
    {
        T create(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties);
    }

    @FunctionalInterface
    interface HoeItemFactory<T extends HoeItem>
    {
        T create(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties);
    }

    @FunctionalInterface
    interface ArmorItemFactory<T extends ArmorItem>
    {
        T create(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Item.Properties properties);
    }

    @FunctionalInterface
    interface HorseArmorItemFactory<T extends CustomHorseArmorItem>
    {
        T create(int protection, ResourceLocation texturePath, Item.Properties properties);
    }
}
