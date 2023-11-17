package dev.apexstudios.testmod.common;

import dev.apexstudios.apexcore.common.registry.DeferredItem;
import dev.apexstudios.apexcore.common.registry.Register;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface TestMod
{
    String ID = "testmod";
    Logger LOGGER = LogManager.getLogger();
    Register REGISTER = Register.create(ID);

    DeferredItem<Item> TEST_ITEM = REGISTER.object("test_item").item().register();

    default void bootstrap()
    {
        REGISTER.register();
    }
}
