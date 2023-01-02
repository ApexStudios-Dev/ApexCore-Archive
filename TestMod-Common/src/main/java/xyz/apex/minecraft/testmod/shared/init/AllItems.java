package xyz.apex.minecraft.testmod.shared.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import xyz.apex.minecraft.apexcore.shared.registry.entry.RegistryEntry;
import xyz.apex.minecraft.testmod.shared.TestMod;

public interface AllItems
{
    RegistryEntry<Item> TEST_ITEM = TestMod.REGISTRAR.simple(Registries.ITEM, "test_item", () -> new Item(new Item.Properties()));

    static void bootstrap() {}
}
