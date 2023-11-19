package dev.apexstudios.testmod.common;

import dev.apexstudios.apexcore.common.registry.Register;
import dev.apexstudios.apexcore.common.registry.generic.DeferredSpawnEggItem;
import dev.apexstudios.apexcore.common.registry.holder.DeferredBlock;
import dev.apexstudios.apexcore.common.registry.holder.DeferredEntityType;
import dev.apexstudios.apexcore.common.registry.holder.DeferredItem;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface TestMod
{
    String ID = "testmod";
    Logger LOGGER = LogManager.getLogger();
    Register REGISTER = Register.create(ID);

    DeferredItem<Item> TEST_ITEM = REGISTER.object("test_item").item().color(() -> () -> (stack, tintIndex) -> -1).register();
    DeferredBlock<Block> TEST_BLOCK = REGISTER.object("test_block").block().color(() -> () -> (blockState, level, pos, tintIndex) -> -1).defaultItem().register();

    DeferredEntityType<Pig> TEST_ENTITY = REGISTER
            .object("test_entity")
            .entity(MobCategory.CREATURE, Pig::new)
            .properties(builder -> builder
                    .sized(.9F, .9F)
                    .clientTrackingRange(10)
            )
            .spawnPlacement(SpawnPlacements.Type.ON_GROUND)
            // .spawnHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES) // default
            .spawnPredicate(Animal::checkAnimalSpawnRules)
            .attributes(() -> Pig.createAttributes().add(Attributes.MOVEMENT_SPEED, .75D))
            .renderer(() -> () -> PigRenderer::new)
            .defaultSpawnEgg(0x00FF00, 0xFF0000)
    .register();

    DeferredItem<DeferredSpawnEggItem> TEST_ENTITY_SPAWN_EGG = DeferredItem.createItem(TEST_ENTITY.getValueId());

    default void bootstrap()
    {
        REGISTER.register();
    }
}
