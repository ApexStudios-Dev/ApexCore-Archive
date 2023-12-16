package dev.apexstudios.testmod.common.ref;

import dev.apexstudios.apexcore.common.registry.holder.DeferredEntityType;
import dev.apexstudios.testmod.common.TestMod;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;

public interface TestEntities
{
    // @formatter:off
    DeferredEntityType<Pig> TEST_ENTITY = TestMod.REGISTER
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
            .tag(EntityTypeTags.DISMOUNTS_UNDERWATER)
            .defaultSpawnEgg(0x00FF00, 0xFF0000)
    .register();
    // @formatter:on
}
