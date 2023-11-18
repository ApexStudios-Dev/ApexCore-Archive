package dev.apexstudios.testmod.common;

import dev.apexstudios.apexcore.common.registry.Register;
import dev.apexstudios.apexcore.common.registry.holder.DeferredBlock;
import dev.apexstudios.apexcore.common.registry.holder.DeferredItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface TestMod
{
    String ID = "testmod";
    Logger LOGGER = LogManager.getLogger();
    Register REGISTER = Register.create(ID);

    DeferredItem<Item> TEST_ITEM = REGISTER.object("test_item").item().register();
    DeferredBlock<Block> TEST_BLOCK = REGISTER.object("test_block").block().defaultItem().register();

    default void bootstrap()
    {
        REGISTER.register();
    }
}
