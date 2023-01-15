package xyz.apex.minecraft.apexcore.quilt.platform;

import com.google.common.collect.Maps;
import dev.architectury.platform.Mod;
import dev.architectury.utils.Env;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.*;
import org.quiltmc.loader.impl.QuiltLoaderImpl;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import xyz.apex.minecraft.apexcore.shared.platform.GamePlatform;
import xyz.apex.minecraft.apexcore.shared.platform.ModLoader;
import xyz.apex.minecraft.apexcore.shared.util.EnhancedTier;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class QuiltGamePlatform implements GamePlatform
{
    private boolean initialized = false;
    private final QuiltStorages storages = new QuiltStorages();
    private final Map<String, Mod> mods = Maps.newConcurrentMap();

    public QuiltGamePlatform()
    {
        initialize();
    }

    @Override
    public void initialize()
    {
        if(initialized) return;
        GamePlatform.super.initialize();
        initialized = true;
    }

    @Override
    public ModLoader getModLoader()
    {
        return ModLoader.QUILT;
    }

    @Override
    public String getModLoaderVersion()
    {
        return QuiltLoaderImpl.VERSION;
    }

    @Override
    public EnhancedTier createEnhancedTier(String registryName, int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, Supplier<Ingredient> repairIngredient, @Nullable TagKey<Block> toolLevelBlock)
    {
        return new QuiltEnhancedTier(registryName, uses, speed, attackDamageBonus, level, enchantmentValue, repairIngredient, toolLevelBlock);
    }

    // Redirect away from using Architecturys Platform class
    // They do not have a 'Quilt' module as of yet, which means Platform
    // is defaulting to old Fabric apis, we redirect to actual Quilt apis
    @Override
    public Path getGameFolder()
    {
        return QuiltLoader.getGameDir();
    }

    @Override
    public Path getConfigFolder()
    {
        return QuiltLoader.getConfigDir();
    }

    @Override
    public Path getModsFolder()
    {
        return QuiltLoaderImpl.INSTANCE.getModsDir();
    }

    @Override
    public Env getEnvironment()
    {
        return Env.fromPlatform(QuiltLoaderImpl.INSTANCE.getEnvironmentType());
    }

    @Override
    public boolean isModLoaded(String modId)
    {
        return QuiltLoader.isModLoaded(modId);
    }

    @Override
    public Mod getMod(String modId)
    {
        return mods.computeIfAbsent(modId, QuiltModImpl::new);
    }

    @Override
    public Optional<Mod> getOptionalMod(String modId)
    {
        try
        {
            return Optional.of(getMod(modId));
        }
        catch(NoSuchElementException e)
        {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Mod> getMods()
    {
        QuiltLoader.getAllMods().stream().map(mod -> mod.metadata().id()).forEach(this::getMod);
        return mods.values();
    }

    @Override
    public Collection<String> getModIds()
    {
        return QuiltLoader.getAllMods().stream().map(ModContainer::metadata).map(ModMetadata::id).toList();
    }

    @Override
    public boolean isDevelopmentEnvironment()
    {
        return QuiltLoader.isDevelopmentEnvironment();
    }

    @Override
    public boolean isRunningDataGeneration()
    {
        return FabricDataGenHelper.ENABLED;
    }

    private record QuiltModImpl(ModContainer modContainer) implements Mod
    {
        private QuiltModImpl(String modId)
        {
            this(QuiltLoader.getModContainer(modId).orElseThrow());
        }

        @Override
        public String getModId()
        {
            return modContainer.metadata().id();
        }

        @Override
        public String getVersion()
        {
            return modContainer.metadata().version().raw();
        }

        @Override
        public String getName()
        {
            return modContainer.metadata().name();
        }

        @Override
        public String getDescription()
        {
            return modContainer.metadata().description();
        }

        @Override
        public Optional<String> getLogoFile(int preferredSize)
        {
            return Optional.ofNullable(modContainer.metadata().icon(preferredSize));
        }

        @Override
        public List<Path> getFilePaths()
        {
            return Collections.singletonList(modContainer.rootPath());
        }

        @SuppressWarnings("removal")
        @Override
        public Path getFilePath()
        {
            return modContainer.rootPath();
        }

        @Override
        public Optional<Path> findResource(String... path)
        {
            return Optional.of(modContainer.getPath(String.join("/", path)));
        }

        @Override
        public Collection<String> getAuthors()
        {
            return modContainer.metadata().contributors().stream().map(ModContributor::name).toList();
        }

        @Override
        public Collection<String> getLicense()
        {
            return modContainer.metadata().licenses().stream().map(ModLicense::name).collect(Collectors.toList());
        }

        @Override
        public Optional<String> getHomepage()
        {
            return Optional.ofNullable(modContainer.metadata().contactInfo().get("homepage"));
        }

        @Override
        public Optional<String> getSources()
        {
            return Optional.ofNullable(modContainer.metadata().contactInfo().get("sources"));
        }

        @Override
        public Optional<String> getIssueTracker()
        {
            return Optional.ofNullable(modContainer.metadata().contactInfo().get("issues"));
        }

        @Override
        public void registerConfigurationScreen(ConfigurationScreenProvider provider)
        {
             // NOOP
        }
    }
}
