package xyz.apex.minecraft.apexcore.common.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.apex.minecraft.apexcore.common.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.registry.RegistrarManager;

public interface ApexCore
{
    String ID = "apexcore";
    Logger LOGGER = LogManager.getLogger();

    default void bootstrap()
    {
        Services.bootstrap();

        RegistrarManager.register(ID);

        /*RegistrarManager.register(ID);
        var items = Registrar.get(ID, Registries.ITEM);
        items.register("test_item", () -> new Item(new Item.Properties()));
        var blocks = Registrar.get(ID, Registries.BLOCK);
        var block = blocks.register("test_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
        items.register(block.getRegistrationName(), () -> new BlockItem(block.get(), new Item.Properties()));*/

        /*var builders = BuilderManager.create(ID);
        builders.item("test_item").register();
        builders.block("test_block").defaultItem().end().register();
        builders.block("test_block_no_item").register();*/
    }
}
