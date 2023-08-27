package xyz.apex.minecraft.apexcore.common.lib.support;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import xyz.apex.minecraft.apexcore.common.core.ApexCore;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ProviderTypes;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SupportLevel implements StringRepresentable
{
    FOUNDER("founder", "Founder"),
    CO_FOUNDER("co_founder", "Co-Founder"),

    ALPHA_TESTER("alpha_tester", "Alpha Tester"),
    BETA_TESTER("beta_tester", "Beta Tester"),

    SUPPORTER("supporter", "Supporter"),
    NONE("none", "None");

    private static final Set<SupportLevel> LEVELS = Set.of(values());
    private static final Set<String> NAMES = LEVELS.stream().map(SupportLevel::getSerializedName).collect(Collectors.toUnmodifiableSet());
    private static final Map<String, SupportLevel> BY_NAME = LEVELS.stream().collect(Collectors.toUnmodifiableMap(SupportLevel::getSerializedName, Function.identity()));

    private final String serializedName;
    private final Component displayName;
    private final String translationKey;
    private final String englishName;

    SupportLevel(String serializedName, String englishName)
    {
        this.serializedName = serializedName;
        this.englishName = englishName;

        translationKey = Util.makeDescriptionId("support", new ResourceLocation(ApexCore.ID, serializedName));
        displayName = Component.translatableWithFallback(translationKey, englishName);
    }

    public Component displayName()
    {
        return displayName;
    }

    public String translationKey()
    {
        return translationKey;
    }

    public boolean matches(SupportLevel other)
    {
        return ordinal() <= other.ordinal();
    }

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public static Set<String> getLevelNames()
    {
        return NAMES;
    }

    public static Set<SupportLevel> getLevels()
    {
        return LEVELS;
    }

    public static SupportLevel byName(String serializedName)
    {
        return BY_NAME.get(serializedName);
    }

    @DoNotCall
    public static void bootstrap()
    {
        ProviderTypes.LANGUAGES.addListener(ApexCore.ID, (provider, lookup) -> getLevels().forEach(level -> provider.enUS().add(level.translationKey, level.englishName).end()));
    }
}
