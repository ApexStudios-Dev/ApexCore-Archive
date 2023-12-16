package dev.apexstudios.testmod.common.ref;

import dev.apexstudios.apexcore.common.registry.generic.DeferredSpawnEggItem;
import dev.apexstudios.apexcore.common.registry.holder.DeferredItem;
import dev.apexstudios.testmod.common.TestMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public interface TestItems
{
    DeferredItem<Item> TEST_ITEM = TestMod.REGISTER.object("test_item").item().color(() -> () -> (stack, tintIndex) -> -1).register();
    DeferredItem<BlockItem> TEST_BLOCK = DeferredItem.createItem(TestBlocks.TEST_BLOCK.registryName());
    DeferredItem<BlockItem> TEST_BLOCK_WITH_ENTITY = DeferredItem.createItem(TestBlocks.BLOCK_WITH_ENTITY.registryName());
    DeferredItem<DeferredSpawnEggItem> TEST_ENTITY_SPAWN_EGG = DeferredItem.createItem(TestEntities.TEST_ENTITY.registryName());
}
