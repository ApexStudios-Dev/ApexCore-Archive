package xyz.apex.forge.apexcore.core.init;

import com.tterrag.registrate.util.entry.EntityEntry;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;

import xyz.apex.forge.apexcore.core.entity.SeatEntity;
import xyz.apex.forge.apexcore.lib.client.renderer.DummyEntityRenderer;

public final class ACEntities
{
	// region: Seat Entity
	public static final EntityEntry<SeatEntity> SEAT_ENTITY = ACRegistry.INSTANCE
			.object("seat")
			.<SeatEntity>entity(SeatEntity::new, MobCategory.MISC)
				.lang("Seat")

				.properties(properties -> properties
						.sized(0F, 0F)
						.setCustomClientFactory(SeatEntity::new)
						.noSummon()
						.fireImmune()
						.immuneTo(Blocks.TNT, Blocks.LAVA)
				)

				.renderer(() -> DummyEntityRenderer::new)
			.register();
	// endregion

	static void bootstrap()
	{
	}
}
