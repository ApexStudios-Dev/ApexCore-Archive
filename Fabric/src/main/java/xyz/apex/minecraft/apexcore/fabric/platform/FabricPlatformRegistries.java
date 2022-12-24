package xyz.apex.minecraft.apexcore.fabric.platform;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformRegistries;
import xyz.apex.minecraft.apexcore.shared.registry.ArmorMaterialBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.TierBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedArmorMaterial;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;
import xyz.apex.minecraft.apexcore.shared.util.Lazy;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public final class FabricPlatformRegistries extends FabricPlatformHolder implements PlatformRegistries
{
    private final FabricPlatformGameRules gameRules = new FabricPlatformGameRules(platform);

    FabricPlatformRegistries(FabricPlatform platform)
    {
        super(platform);
    }

    @Override
    public <T> Collection<T> getAllKnown(ResourceKey<? extends Registry<T>> registryType, String modId)
    {
        var registry = RegistryEntry.getRegistryOrThrow(registryType);
        return registry.stream().filter(v -> Objects.requireNonNull(registry.getKey(v)).getNamespace().equals(modId)).collect(Collectors.toList());
    }

    @Override
    public <T, R extends T, E extends RegistryEntry<R>> void register(ResourceKey<? extends Registry<T>> registryType, E registryEntry, Supplier<R> factory, BiConsumer<E, R> onRegister)
    {
        platform.modEvents.register(registryEntry.getModId());
        var registry = RegistryEntry.getRegistryOrThrow(registryType);
        var result = Registry.register(registry, registryEntry.getRegistryName(), factory.get());
        onRegister.accept(registryEntry, result);
    }

    @Override
    public EnhancedTier registerTier(TierBuilder builder)
    {
        platform.modEvents.register(builder.getModId());
        platform.getLogger().debug("Registering Tier: {}:{}", builder.getModId(), builder.getRegistryName());
        return new EnhancedFabricTier(builder);
    }

    @Override
    public ArmorMaterial registerArmorMaterial(ArmorMaterialBuilder builder)
    {
        platform.modEvents.register(builder.getModId());
        platform.getLogger().debug("Registering ArmorMaterial: {}:{}", builder.getModId(), builder.getRegistryName());
        return new EnhancedFabricArmorMaterial(builder);
    }

    @Override
    public PlatformGameRules gameRules()
    {
        return gameRules;
    }

    private static final class FabricPlatformGameRules extends FabricPlatformHolder implements PlatformGameRules
    {
        FabricPlatformGameRules(FabricPlatform platform)
        {
            super(platform);
        }

        @Override
        public <T extends GameRules.Value<T>> GameRules.Key<T> register(String modId, String registryName, GameRules.Category category, GameRules.Type<T> type)
        {
            var internalName = "%s:%s".formatted(modId, registryName);
            platform.getLogger().debug("Registering GameRule: {}", internalName);
            return GameRuleRegistry.register(internalName, category, type);
        }

        @Override
        public GameRules.Type<GameRules.BooleanValue> createBooleanType(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener)
        {
            return GameRuleFactory.createBooleanRule(defaultValue, changeListener);
        }

        @Override
        public GameRules.Type<GameRules.IntegerValue> createIntegerType(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
        {
            return GameRuleFactory.createIntRule(defaultValue, changeListener);
        }
    }

    private static final class EnhancedFabricTier implements EnhancedTier
    {
        private final ResourceLocation registryName;
        private final int uses;
        private final float speed;
        private final float attackDamageBonus;
        private final int level;
        private final int enchantmentValue;
        private final Supplier<Ingredient> repairIngredient;
        @Nullable private final TagKey<Block> toolLevelTag;

        private EnhancedFabricTier(TierBuilder builder)
        {
            registryName = new ResourceLocation(builder.getModId(), builder.getRegistryName());
            uses = builder.getUses();
            speed = builder.getSpeed();
            attackDamageBonus = builder.getAttackDamageBonus();
            level = builder.getLevel();
            enchantmentValue = builder.getEnchantmentValue();
            repairIngredient = Lazy.of(builder.getRepairIngredient());
            toolLevelTag = builder.getToolLevelTag();
        }

        @Override
        public ResourceLocation getRegistryName()
        {
            return registryName;
        }

        @Nullable
        @Override
        public TagKey<Block> getToolLevelTag()
        {
            return toolLevelTag;
        }

        @Override
        public int getUses()
        {
            return uses;
        }

        @Override
        public float getSpeed()
        {
            return speed;
        }

        @Override
        public float getAttackDamageBonus()
        {
            return attackDamageBonus;
        }

        @Override
        public int getLevel()
        {
            return level;
        }

        @Override
        public int getEnchantmentValue()
        {
            return enchantmentValue;
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return repairIngredient.get();
        }
    }

    private static final class EnhancedFabricArmorMaterial implements EnhancedArmorMaterial
    {
        private final ResourceLocation registryName;
        private final ToIntFunction<EquipmentSlot> durabilityForSlot;
        private final ToIntFunction<EquipmentSlot> defenseForSlot;
        private final int enchantmentValue;
        private final Supplier<SoundEvent> equipSound;
        private final Supplier<Ingredient> repairIngredient;
        private final float toughness;
        private final float knockbackResistance;

        private EnhancedFabricArmorMaterial(ArmorMaterialBuilder builder)
        {
            registryName = new ResourceLocation(builder.getModId(), builder.getRegistryName());
            durabilityForSlot = builder.getDurabilityForSlot();
            defenseForSlot = builder.getDefenseForSlot();
            enchantmentValue = builder.getEnchantmentValue();
            equipSound = Lazy.of(builder.getEquipSound());
            repairIngredient = Lazy.of(builder.getRepairIngredient());
            toughness = builder.getToughness();
            knockbackResistance = builder.getKnockbackResistance();
        }

        @Override
        public ResourceLocation getRegistryName()
        {
            return registryName;
        }

        @Override
        public int getDurabilityForSlot(EquipmentSlot pSlot)
        {
            return durabilityForSlot.applyAsInt(pSlot);
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot pSlot)
        {
            return defenseForSlot.applyAsInt(pSlot);
        }

        @Override
        public int getEnchantmentValue()
        {
            return enchantmentValue;
        }

        @Override
        public SoundEvent getEquipSound()
        {
            return equipSound.get();
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return repairIngredient.get();
        }

        @Override
        public float getToughness()
        {
            return toughness;
        }

        @Override
        public float getKnockbackResistance()
        {
            return knockbackResistance;
        }
    }
}
