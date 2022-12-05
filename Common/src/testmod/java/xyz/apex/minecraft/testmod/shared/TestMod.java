package xyz.apex.minecraft.testmod.shared;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;

import xyz.apex.minecraft.apexcore.shared.platform.Platform;
import xyz.apex.minecraft.apexcore.shared.platform.PlatformTierRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.block.BlockRegistry;
import xyz.apex.minecraft.apexcore.shared.registry.item.ItemRegistry;
import xyz.apex.minecraft.testmod.shared.init.AllBlocks;
import xyz.apex.minecraft.testmod.shared.init.AllItems;

public interface TestMod
{
    String ID = "testmod";
    PlatformTierRegistry.ArmorMaterialTier LEAD_ITEM_TIER = Platform.tierRegistry().registerMerged(
            ID, "lead", 14, () -> Ingredient.of(AllItems.LEAD_INGOT),
            250, 6F, 2F, 2, BlockTags.NEEDS_IRON_TOOL,
            () -> SoundEvents.ARMOR_EQUIP_GENERIC, 0F, 0F, slot -> slot.getIndex() * 15, slot -> switch(slot) {
                case FEET -> 2;
                case LEGS -> 5;
                case CHEST -> 6;
                case HEAD -> 2;
                default -> throw new IllegalArgumentException("Unknown EquipmentSlot type for ArmorMaterial: %s".formatted(slot.getName()));
            }
    );

    static void bootstrap()
    {
        Registries.bootstrap();

        AllBlocks.bootstrap();
        AllItems.bootstrap();
    }

    interface Registries
    {
        BlockRegistry BLOCKS  = BlockRegistry.create(ID);
        ItemRegistry ITEMS = ItemRegistry.create(ID);

        private static void bootstrap()
        {
        }
    }
}
