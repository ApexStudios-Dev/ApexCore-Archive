package xyz.apex.forge.apexcore.core.init;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;

import xyz.apex.forge.apexcore.core.entity.SeatEntity;
import xyz.apex.forge.utility.registrator.entry.EntityEntry;

import static xyz.apex.forge.utility.registrator.provider.RegistrateLangExtProvider.EN_GB;

public final class ACEntities
{
	private static final ACRegistry REGISTRY = ACRegistry.getRegistry();

	// region: Seat Entity
	public static final EntityEntry<SeatEntity> SEAT_ENTITY = REGISTRY
			.<SeatEntity>entity("seat", MobCategory.MISC, SeatEntity::new)
				.lang("Seat")
				.lang(EN_GB, "Seat")

				.sized(0F, 0F)
				.setCustomClientFactory(SeatEntity::new)

				.noSummon()
				.fireImmune()
				.immuneTo(() -> Blocks.TNT, () -> Blocks.LAVA)

				.renderer(() -> SeatEntityRenderer::new)
			.register();
	// endregion

	static void bootstrap()
	{
	}
}
