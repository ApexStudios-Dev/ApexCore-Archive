package dev.apexstudios.testmod.common;

import com.google.common.reflect.Reflection;
import dev.apexstudios.apexcore.common.generator.ProviderTypes;
import dev.apexstudios.apexcore.common.network.NetworkManager;
import dev.apexstudios.apexcore.common.registry.Register;
import dev.apexstudios.testmod.common.ref.*;
import net.minecraft.core.Holder;
import net.minecraft.world.item.CreativeModeTab;
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
    Holder<CreativeModeTab> TEST_TAB = REGISTER.creativeModeTab("test_tab").icon(Items.DIAMOND).register();
    GameRules.Key<GameRules.BooleanValue> TEST_GAMERULE = GameRules.register("test", GameRules.Category.MISC, GameRules.BooleanValue.create(false));

    default void bootstrap()
    {
        Reflection.initialize(
                TestBlocks.class,
                TestItems.class,
                TestEntities.class,
                TestMenus.class,
                TestBlockEntities.class,
                TestPackets.class
        );

        REGISTER.register();

        ProviderTypes.addDefaultPackMetadata(ID, "TestMod - Client/Server Resources");
    }
}
