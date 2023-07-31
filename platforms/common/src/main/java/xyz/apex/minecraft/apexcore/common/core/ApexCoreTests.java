package xyz.apex.minecraft.apexcore.common.core;

import joptsimple.internal.Strings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.registry.Registrar;

import java.util.stream.Stream;

@ApiStatus.Internal
public final class ApexCoreTests
{
    private static final String PROPERTY = "%s.test_elements.enabled".formatted(ApexCore.ID);
    public static final boolean ENABLED = BooleanUtils.toBoolean(System.getProperty(PROPERTY, "false"));
    public static final Marker MARKER = MarkerManager.getMarker("Tests");

    static void register()
    {
        if(!ENABLED)
            return;

        var messages = new String[] {
                "ApexCore Test Elements Enabled!",
                "Errors, Bugs and Glitches may arise, you are on your own.",
                "No support will be provided while Test Elements are Enabled!"
        };

        var maxLen = Stream.of(messages).mapToInt(String::length).max().orElse(1);
        var header = Strings.repeat('*', maxLen + 4);
        ApexCore.LOGGER.warn(MARKER, header);
        Stream.of(messages).map(str -> StringUtils.center(str, maxLen, ' ')).forEach(str -> ApexCore.LOGGER.warn(MARKER, "* {} *", str));
        ApexCore.LOGGER.warn(MARKER, header);

        var registrar = Registrar.create(ApexCore.ID);

        var testItem = registrar
                .object("test_item")
                .item()
                .defaultModel()
                .model((provider, lookup, entry) -> provider.generated(
                        entry.getRegistryName().withPrefix("item/"),
                        new ResourceLocation("item/diamond")
                ))
        .register();

        var testBlock = registrar
                .object("test_block")
                .block()
                .defaultBlockState((provider, lookup, entry) -> provider.withParent(
                        entry.getRegistryName().withPrefix("block/"),
                        "block/cube_all"
                ).texture("all", new ResourceLocation("block/debug")))
                .defaultItem()
        .register();

        var testEnchantment = registrar
                .object("test_enchantment")
                .enchantment(EnchantmentCategory.DIGGER)
                .equipmentSlots(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND)
        .register();

        var creativeModeTab = registrar
                .creativeModeTab("test")
                .lang("en_us", "ApexCore - Test Elements")
                .icon(testBlock::asStack)
                .displayItems((parameters, output) -> {
                    output.accept(testItem);
                    output.accept(testBlock);
                    output.accept(testEnchantment.asStack(1));
                })
        .register();

        registrar.register();
    }
}
