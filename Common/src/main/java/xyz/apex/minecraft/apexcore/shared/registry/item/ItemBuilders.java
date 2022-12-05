package xyz.apex.minecraft.apexcore.shared.registry.item;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.item.*;

import java.util.Objects;
import java.util.function.Supplier;

public final class ItemBuilders
{
    public static final int DEFAULT_SWORD_ATTACK_DAMAGE = 3;
    public static final int DEFAULT_PICKAXE_ATTACK_DAMAGE = 1;
    public static final float DEFAULT_AXE_ATTACK_DAMAGE = 6F;
    public static final float DEFAULT_SHOVEL_ATTACK_DAMAGE = 1.5F;
    public static final int DEFAULT_HOE_ATTACK_DAMAGE = 0;

    public static final float DEFAULT_SWORD_ATTACK_SPEED = -2.4F;
    public static final float DEFAULT_PICKAXE_ATTACK_SPEED = -2.8F;
    public static final float DEFAULT_AXE_ATTACK_SPEED = -3F;
    public static final float DEFAULT_SHOVEL_ATTACK_SPEED = -3F;
    public static final float DEFAULT_HOE_ATTACK_SPEED = -1F;

    private final ItemRegistry registry;
    @Nullable private String currentName;

    ItemBuilders(ItemRegistry registry)
    {
        this.registry = registry;
    }

    ItemBuilders pushName(String name)
    {
        currentName = name;
        return this;
    }

    String popName()
    {
        var name = Objects.requireNonNull(currentName, "ItemBuilders#pushName was not invoked, This is *VERY* bad, report this to ApexStudios ASAP!");
        currentName = null;
        return name;
    }

    String getNamespace()
    {
        return registry.getRegistryName().getNamespace();
    }

    // region: Basic
    public <T extends Item> ItemBuilder<T> builder(ItemFactory<T> factory)
    {
        return new ItemBuilder<>(registry, popName(), factory);
    }

    public ItemBuilder<Item> simpleItem()
    {
        return builder(Item::new);
    }
    // endregion

    // region: Horse Armor
    public <T extends HorseArmorItem> ItemBuilder<T> horseArmor(int protection, String horseArmorType, HorseArmorItemFactory<T> factory)
    {
        return builder(properties -> factory.create(protection, getNamespace(), horseArmorType, properties));
    }

    public ItemBuilder<HorseArmorItem> horseArmor(int protection, String horseArmorType)
    {
        return horseArmor(protection, horseArmorType, CustomHorseArmorItem::new);
    }
    // endregion

    // region: Tiered
    public <T extends TieredItem> ItemBuilder<T> tieredItem(Tier tier, TieredItemFactory<T> factory)
    {
        return builder(properties -> factory.create(tier, properties));
    }

    public ItemBuilder<TieredItem> tieredItem(Tier tier)
    {
        return tieredItem(tier, TieredItem::new);
    }

    // region: Sword
    public <T extends SwordItem> ItemBuilder<T> sword(Tier tier, int baseAttackDamage, float attackSpeed, SwordItemFactory<T> factory)
    {
        return tieredItem(tier, ($, properties) -> factory.create($, baseAttackDamage, attackSpeed, properties));
    }

    public <T extends SwordItem> ItemBuilder<T> sword(Tier tier, int baseAttackDamage, SwordItemFactory<T> factory)
    {
        return sword(tier, baseAttackDamage, DEFAULT_SWORD_ATTACK_SPEED, factory);
    }

    public <T extends SwordItem> ItemBuilder<T> sword(Tier tier, float attackSpeed, SwordItemFactory<T> factory)
    {
        return sword(tier, DEFAULT_SWORD_ATTACK_DAMAGE, attackSpeed, factory);
    }

    public <T extends SwordItem> ItemBuilder<T> sword(Tier tier, SwordItemFactory<T> factory)
    {
        return sword(tier, DEFAULT_SWORD_ATTACK_DAMAGE, DEFAULT_SWORD_ATTACK_SPEED, factory);
    }

    public ItemBuilder<SwordItem> sword(Tier tier, int baseAttackDamage, float attackSpeed)
    {
        return sword(tier, baseAttackDamage, attackSpeed, SwordItem::new);
    }

    public ItemBuilder<SwordItem> sword(Tier tier, int baseAttackDamage)
    {
        return sword(tier, baseAttackDamage, SwordItem::new);
    }

    public ItemBuilder<SwordItem> sword(Tier tier, float attackSpeed)
    {
        return sword(tier, attackSpeed, SwordItem::new);
    }

    public ItemBuilder<SwordItem> sword(Tier tier)
    {
        return sword(tier, SwordItem::new);
    }
    // endregion

    // region: Digger
    public <T extends DiggerItem> ItemBuilder<T> diggerItem(Tier tier, float baseAttackDamage, float attackSpeed, TagKey<Block> effectiveBlocks, DiggerItemFactory<T> factory)
    {
        return tieredItem(tier, ($, properties) -> factory.create(baseAttackDamage, attackSpeed, $, effectiveBlocks, properties));
    }

    public ItemBuilder<DiggerItem> diggerItem(Tier tier, float baseAttackDamage, float attackSpeed, TagKey<Block> effectiveBlocks)
    {
        return diggerItem(tier, baseAttackDamage, attackSpeed, effectiveBlocks, CustomDiggerItem::new);
    }

    // region: Pickaxe
    public <T extends PickaxeItem> ItemBuilder<T> pickaxeItem(Tier tier, int baseAttackDamage, float attackSpeed, PickaxeItemFactory<T> factory)
    {
        return diggerItem(tier, baseAttackDamage, attackSpeed, BlockTags.MINEABLE_WITH_PICKAXE, ($1, $2, $3, $4, properties) -> factory.create($3, (int) $1, $2, properties));
    }

    public <T extends PickaxeItem> ItemBuilder<T> pickaxeItem(Tier tier, int baseAttackDamage, PickaxeItemFactory<T> factory)
    {
        return pickaxeItem(tier, baseAttackDamage, DEFAULT_PICKAXE_ATTACK_SPEED, factory);
    }

    public <T extends PickaxeItem> ItemBuilder<T> pickaxeItem(Tier tier, float attackSpeed, PickaxeItemFactory<T> factory)
    {
        return pickaxeItem(tier, DEFAULT_PICKAXE_ATTACK_DAMAGE, attackSpeed, factory);
    }

    public <T extends PickaxeItem> ItemBuilder<T> pickaxeItem(Tier tier, PickaxeItemFactory<T> factory)
    {
        return pickaxeItem(tier, DEFAULT_PICKAXE_ATTACK_DAMAGE, DEFAULT_PICKAXE_ATTACK_SPEED, factory);
    }

    public ItemBuilder<PickaxeItem> pickaxeItem(Tier tier, int baseAttackDamage, float attackSpeed)
    {
        return pickaxeItem(tier, baseAttackDamage, attackSpeed, CustomPickaxeItem::new);
    }

    public ItemBuilder<PickaxeItem> pickaxeItem(Tier tier, int baseAttackDamage)
    {
        return pickaxeItem(tier, baseAttackDamage, CustomPickaxeItem::new);
    }

    public ItemBuilder<PickaxeItem> pickaxeItem(Tier tier, float attackSpeed)
    {
        return pickaxeItem(tier, attackSpeed, CustomPickaxeItem::new);
    }

    public ItemBuilder<PickaxeItem> pickaxeItem(Tier tier)
    {
        return pickaxeItem(tier, CustomPickaxeItem::new);
    }
    // endregion

    // region: Axe
    public <T extends AxeItem> ItemBuilder<T> axeItem(Tier tier, double baseAttackDamage, float attackSpeed, AxeItemFactory<T> factory)
    {
        return diggerItem(tier, (float) baseAttackDamage, attackSpeed, BlockTags.MINEABLE_WITH_AXE, ($1, $2, $3, $4, properties) -> factory.create($3, $1, $2, properties));
    }

    public <T extends AxeItem> ItemBuilder<T> axeItem(Tier tier, double baseAttackDamage, AxeItemFactory<T> factory)
    {
        return axeItem(tier, baseAttackDamage, DEFAULT_AXE_ATTACK_SPEED, factory);
    }

    public <T extends AxeItem> ItemBuilder<T> axeItem(Tier tier, float attackSpeed, AxeItemFactory<T> factory)
    {
        return axeItem(tier, DEFAULT_AXE_ATTACK_DAMAGE, attackSpeed, factory);
    }

    public <T extends AxeItem> ItemBuilder<T> axeItem(Tier tier, AxeItemFactory<T> factory)
    {
        return axeItem(tier, DEFAULT_AXE_ATTACK_DAMAGE, DEFAULT_AXE_ATTACK_SPEED, factory);
    }

    public ItemBuilder<AxeItem> axeItem(Tier tier, double baseAttackDamage, float attackSpeed)
    {
        return axeItem(tier, baseAttackDamage, attackSpeed, CustomAxeItem::new);
    }

    public ItemBuilder<AxeItem> axeItem(Tier tier, double baseAttackDamage)
    {
        return axeItem(tier, baseAttackDamage, CustomAxeItem::new);
    }

    public ItemBuilder<AxeItem> axeItem(Tier tier, float attackSpeed)
    {
        return axeItem(tier, attackSpeed, CustomAxeItem::new);
    }

    public ItemBuilder<AxeItem> axeItem(Tier tier)
    {
        return axeItem(tier, CustomAxeItem::new);
    }
    // endregion

    // region: Shovel
    public <T extends ShovelItem> ItemBuilder<T> shovelItem(Tier tier, double baseAttackDamage, float attackSpeed, ShovelItemFactory<T> factory)
    {
        return diggerItem(tier, (float) baseAttackDamage, attackSpeed, BlockTags.MINEABLE_WITH_SHOVEL, ($1, $2, $3, $4, properties) -> factory.create($3, $1, $2, properties));
    }

    public <T extends ShovelItem> ItemBuilder<T> shovelItem(Tier tier, double baseAttackDamage, ShovelItemFactory<T> factory)
    {
        return shovelItem(tier, (float) baseAttackDamage, DEFAULT_SHOVEL_ATTACK_SPEED, factory);
    }

    public <T extends ShovelItem> ItemBuilder<T> shovelItem(Tier tier, float attackSpeed, ShovelItemFactory<T> factory)
    {
        return shovelItem(tier, DEFAULT_SHOVEL_ATTACK_DAMAGE, attackSpeed, factory);
    }

    public <T extends ShovelItem> ItemBuilder<T> shovelItem(Tier tier, ShovelItemFactory<T> factory)
    {
        return shovelItem(tier, DEFAULT_SHOVEL_ATTACK_DAMAGE, DEFAULT_SHOVEL_ATTACK_SPEED, factory);
    }

    public ItemBuilder<ShovelItem> shovelItem(Tier tier, double baseAttackDamage, float attackSpeed)
    {
        return shovelItem(tier, baseAttackDamage, attackSpeed, ShovelItem::new);
    }

    public ItemBuilder<ShovelItem> shovelItem(Tier tier, double baseAttackDamage)
    {
        return shovelItem(tier, baseAttackDamage, ShovelItem::new);
    }

    public ItemBuilder<ShovelItem> shovelItem(Tier tier, float attackSpeed)
    {
        return shovelItem(tier, attackSpeed, ShovelItem::new);
    }

    public ItemBuilder<ShovelItem> shovelItem(Tier tier)
    {
        return shovelItem(tier, ShovelItem::new);
    }
    // endregion

    // region: Hoe
    public <T extends HoeItem> ItemBuilder<T> hoeItem(Tier tier, int baseAttackDamage, float attackSpeed, HoeItemFactory<T> factory)
    {
        return diggerItem(tier, baseAttackDamage, attackSpeed, BlockTags.MINEABLE_WITH_HOE, ($1, $2, $3, $4, properties) -> factory.create($3, (int) $1, $2, properties));
    }

    public <T extends HoeItem> ItemBuilder<T> hoeItem(Tier tier, int baseAttackDamage, HoeItemFactory<T> factory)
    {
        return hoeItem(tier, baseAttackDamage, DEFAULT_HOE_ATTACK_SPEED, factory);
    }

    public <T extends HoeItem> ItemBuilder<T> hoeItem(Tier tier, float attackSpeed, HoeItemFactory<T> factory)
    {
        return hoeItem(tier, DEFAULT_HOE_ATTACK_DAMAGE, attackSpeed, factory);
    }

    public <T extends HoeItem> ItemBuilder<T> hoeItem(Tier tier, HoeItemFactory<T> factory)
    {
        return hoeItem(tier, DEFAULT_HOE_ATTACK_DAMAGE, DEFAULT_HOE_ATTACK_SPEED, factory);
    }

    public ItemBuilder<HoeItem> hoeItem(Tier tier, int baseAttackDamage, float attackSpeed)
    {
        return hoeItem(tier, baseAttackDamage, attackSpeed, CustomHoeItem::new);
    }

    public ItemBuilder<HoeItem> hoeItem(Tier tier, int baseAttackDamage)
    {
        return hoeItem(tier, baseAttackDamage, CustomHoeItem::new);
    }

    public ItemBuilder<HoeItem> hoeItem(Tier tier, float attackSpeed)
    {
        return hoeItem(tier, attackSpeed, CustomHoeItem::new);
    }

    public ItemBuilder<HoeItem> hoeItem(Tier tier)
    {
        return hoeItem(tier, CustomHoeItem::new);
    }
    // endregion
    // endregion
    // endregion

    // region: Armor
    public <T extends ArmorItem> ItemBuilder<T> armorItem(ArmorMaterial material, EquipmentSlot slot, ArmorItemFactory<T> factory)
    {
        return builder(properties -> factory.create(material, slot, properties));
    }

    public ItemBuilder<ArmorItem> armorItem(ArmorMaterial material, EquipmentSlot slot)
    {
        return armorItem(material, slot, ArmorItem::new);
    }

    // region: Helmet
    public <T extends ArmorItem> ItemBuilder<T> helmetItem(ArmorMaterial material, ArmorItemFactory<T> factory)
    {
        return armorItem(material, EquipmentSlot.HEAD, factory);
    }

    public ItemBuilder<ArmorItem> helmetItem(ArmorMaterial material)
    {
        return armorItem(material, EquipmentSlot.HEAD);
    }
    // endregion

    // region: Chestplate
    public <T extends ArmorItem> ItemBuilder<T> chestplateItem(ArmorMaterial material, ArmorItemFactory<T> factory)
    {
        return armorItem(material, EquipmentSlot.CHEST, factory);
    }

    public ItemBuilder<ArmorItem> chestplateItem(ArmorMaterial material)
    {
        return armorItem(material, EquipmentSlot.CHEST);
    }
    // endregion

    // region: Leggings
    public <T extends ArmorItem> ItemBuilder<T> leggingItem(ArmorMaterial material, ArmorItemFactory<T> factory)
    {
        return armorItem(material, EquipmentSlot.LEGS, factory);
    }

    public ItemBuilder<ArmorItem> leggingItem(ArmorMaterial material)
    {
        return armorItem(material, EquipmentSlot.LEGS);
    }
    // endregion

    // region: Boots
    public <T extends ArmorItem> ItemBuilder<T> bootsItem(ArmorMaterial material, ArmorItemFactory<T> factory)
    {
        return armorItem(material, EquipmentSlot.FEET, factory);
    }

    public ItemBuilder<ArmorItem> bootsItem(ArmorMaterial material)
    {
        return armorItem(material, EquipmentSlot.FEET);
    }
    // endregion
    // endregion

    @FunctionalInterface
    public interface ItemFactory<T extends Item>
    {
        T create(Item.Properties properties);
    }

    @FunctionalInterface
    public interface BlockItemFactory<T extends Item, E extends Block>
    {
        T create(E block, Item.Properties properties);

        default T create(Supplier<? extends E> block, Item.Properties properties)
        {
            return create(block.get(), properties);
        }
    }

    // region: Factories
    @FunctionalInterface
    public interface TieredItemFactory<T extends TieredItem>
    {
        T create(Tier tier, Item.Properties properties);
    }

    @FunctionalInterface
    public interface SwordItemFactory<T extends SwordItem>
    {
        T create(Tier tier, int baseAttackDamage, float attackSpeed, Item.Properties properties);
    }

    @FunctionalInterface
    public interface DiggerItemFactory<T extends DiggerItem>
    {
        T create(float baseAttackDamage, float attackSpeed, Tier tier, TagKey<Block> effectiveBlocks, Item.Properties properties);
    }

    @FunctionalInterface
    public interface PickaxeItemFactory<T extends PickaxeItem>
    {
        T create(Tier tier, int baseAttackDamage, float attackSpeed, Item.Properties properties);
    }

    @FunctionalInterface
    public interface AxeItemFactory<T extends AxeItem>
    {
        T create(Tier tier, float baseAttackDamage, float attackSpeed, Item.Properties properties);
    }

    @FunctionalInterface
    public interface ShovelItemFactory<T extends ShovelItem>
    {
        T create(Tier tier, float baseAttackDamage, float attackSpeed, Item.Properties properties);
    }

    @FunctionalInterface
    public interface HoeItemFactory<T extends HoeItem>
    {
        T create(Tier tier, int baseAttackDamage, float attackSpeed, Item.Properties properties);
    }

    @FunctionalInterface
    public interface HorseArmorItemFactory<T extends HorseArmorItem>
    {
        T create(int protection, ResourceLocation texturePath, Item.Properties properties);

        default T create(int protection, String namespace, String horseArmorType, Item.Properties properties)
        {
            return create(protection, CustomHorseArmorItem.texturePath(namespace, horseArmorType), properties);
        }
    }

    @FunctionalInterface
    public interface ArmorItemFactory<T extends ArmorItem>
    {
        T create(ArmorMaterial material, EquipmentSlot slot, Item.Properties properties);
    }
    // endregion
}
