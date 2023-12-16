package dev.apexstudios.testmod.common;

import com.google.common.reflect.Reflection;
import dev.apexstudios.apexcore.common.generator.ProviderTypes;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import dev.apexstudios.apexcore.common.registry.Register;
import dev.apexstudios.apexcore.common.util.TagHelper;
import dev.apexstudios.testmod.common.ref.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface TestMod
{
    String ID = "testmod";
    Logger LOGGER = LogManager.getLogger();
    Register REGISTER = Register.create(ID);
    NetworkManager NETWORK = NetworkManager.get(ID);
    ResourceKey<CreativeModeTab> TEST_TAB = REGISTER.defaultCreativeModeTab("test_tab", builder -> builder.icon(() -> new ItemStack(Items.DIAMOND)));
    GameRules.Key<GameRules.BooleanValue> TEST_GAMERULE = GameRules.register("test", GameRules.Category.MISC, GameRules.BooleanValue.create(false));

    default void bootstrap()
    {
        Reflection.initialize(
                TestBlocks.class,
                TestItems.class,
                TestEntities.class,
                TestMod.class,
                TestBlockEntities.class,
                TestPackets.class
        );

        REGISTER.register();

        ProviderTypes.addDefaultPackMetadata(ID, "TestMod - Client/Server Resources");

        // register listener to generate `apexcore:diamond_like` item tag
        // this tag consits of the following entries
        // - `minecraft:diamond` -> vanilla diamond item
        // - `#forge:gems/diamond` -> mcforge diamonds tag (optional)
        // - `#neoforge:gems/diamond` -> neoforge diamonds tag (optional)
        // - `#c:diamonds` -> fabric diamonds tag (optional)
        ProviderTypes.TAG_ITEM.addListener(ID, tags -> tags
                .tag(TagHelper.ITEM
                             .platformBuilder(ID, "diamond_like")
                             .withForgeLike("gems/diamond")
                             .withFabric("diamonds")
                             .create()
                )
                .addElement(Items.DIAMOND)
                .end()
        );
    }
}
