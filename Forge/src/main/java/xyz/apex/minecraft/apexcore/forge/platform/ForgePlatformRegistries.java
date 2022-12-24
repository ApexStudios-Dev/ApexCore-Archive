package xyz.apex.minecraft.apexcore.forge.platform;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import xyz.apex.minecraft.apexcore.shared.platform.PlatformRegistries;
import xyz.apex.minecraft.apexcore.shared.registry.ArmorMaterialBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.TierBuilder;
import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedArmorMaterial;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;
import xyz.apex.minecraft.apexcore.shared.util.Lazy;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public final class ForgePlatformRegistries extends ForgePlatformHolder implements PlatformRegistries
{
    private final Table<String, ResourceKey<? extends Registry<?>>, DeferredRegister<?>> modRegistries = HashBasedTable.create();
    private final ForgePlatformGameRules gameRules = new ForgePlatformGameRules(platform);

    ForgePlatformRegistries(ForgePlatform platform)
    {
        super(platform);
    }

    @Override
    public <T> Collection<T> getAllKnown(ResourceKey<? extends Registry<T>> registryType, String modId)
    {
        var modRegistry = getModRegistry(registryType, modId);
        return modRegistry.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }

    @Override
    public <T, R extends T, E extends RegistryEntry<R>> void register(ResourceKey<? extends Registry<T>> registryType, E registryEntry, Supplier<R> factory, BiConsumer<E, R> onRegister)
    {
        getModRegistry(registryType, registryEntry.getModId()).register(registryEntry.getName(), () -> {
            var result = factory.get();
            onRegister.accept(registryEntry, result);
            return result;
        });
    }

    @Override
    public EnhancedTier registerTier(TierBuilder builder)
    {
        platform.getLogger().debug("Registering Tier: {}:{}", builder.getModId(), builder.getRegistryName());
        return new EnhancedForgeTier(builder);
    }

    @Override
    public ArmorMaterial registerArmorMaterial(ArmorMaterialBuilder builder)
    {
        platform.getLogger().debug("Registering ArmorMaterial: {}:{}", builder.getModId(), builder.getRegistryName());
        return new EnhancedForgeArmorMaterial(builder);
    }

    @Override
    public PlatformGameRules gameRules()
    {
        return gameRules;
    }

    @SuppressWarnings({ "unchecked", "DataFlowIssue" })
    private <T> DeferredRegister<T> getModRegistry(ResourceKey<? extends Registry<T>> registryType, String modId)
    {
        if(modRegistries.contains(modId, registryType)) return (DeferredRegister<T>) modRegistries.get(modId, registryType);
        else
        {
            var modRegistry = DeferredRegister.create(registryType, modId);
            modRegistry.register(platform.modEvents.getModBus(modId));
            modRegistries.put(modId, registryType, modRegistry);
            platform.getLogger().debug("Constructed DeferredRegister for mod: {}", modId);
            return modRegistry;
        }
    }

    private static final class ForgePlatformGameRules extends ForgePlatformHolder implements PlatformGameRules
    {
        ForgePlatformGameRules(ForgePlatform platform)
        {
            super(platform);
        }

        @Override
        public <T extends GameRules.Value<T>> GameRules.Key<T> register(String modId, String registryName, GameRules.Category category, GameRules.Type<T> type)
        {
            var internalName = "%s:%s".formatted(modId, registryName);
            platform.getLogger().debug("Registering GameRule: {}", internalName);
            return GameRules.register(internalName, category, type);
        }

        @Override
        public GameRules.Type<GameRules.BooleanValue> createBooleanType(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener)
        {
            return GameRules.BooleanValue.create(defaultValue, changeListener);
        }

        @Override
        public GameRules.Type<GameRules.IntegerValue> createIntegerType(int defaultValue, BiConsumer<MinecraftServer, GameRules.IntegerValue> changeListener)
        {
            return GameRules.IntegerValue.create(defaultValue, changeListener);
        }
    }

    private static final class EnhancedForgeTier implements EnhancedTier
    {
        private final ResourceLocation registryName;
        private final int uses;
        private final float speed;
        private final float attackDamageBonus;
        private final int level;
        private final int enchantmentValue;
        private final Supplier<Ingredient> repairIngredient;
        @Nullable private final TagKey<Block> toolLevelTag;

        private EnhancedForgeTier(TierBuilder builder)
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
        public TagKey<Block> getTag()
        {
            return getToolLevelTag();
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

    private static final class EnhancedForgeArmorMaterial implements EnhancedArmorMaterial
    {
        private final ResourceLocation registryName;
        private final ToIntFunction<EquipmentSlot> durabilityForSlot;
        private final ToIntFunction<EquipmentSlot> defenseForSlot;
        private final int enchantmentValue;
        private final Supplier<SoundEvent> equipSound;
        private final Supplier<Ingredient> repairIngredient;
        private final float toughness;
        private final float knockbackResistance;

        private EnhancedForgeArmorMaterial(ArmorMaterialBuilder builder)
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
