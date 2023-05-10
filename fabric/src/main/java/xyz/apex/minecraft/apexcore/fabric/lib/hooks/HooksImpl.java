package xyz.apex.minecraft.apexcore.fabric.lib.hooks;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.hook.*;

import java.util.function.Supplier;

@ApiStatus.Internal
public final class HooksImpl implements Hooks
{
    private final RegisterRendererHooks registerRendererHooks = new RegisterRendererHooksImpl();
    private final RegisterColorHandlerHooks colorHandlerHooks = new RegisterColorHandlerHooksImpl();
    private final EntityHooks entityHooks = new EntityHooksImpl();
    private final GameRuleHooks gameRuleHooks = new GameRuleHooksImpl();
    private final CreativeModeTabHooks creativeModeTabHooks = new CreativeModeTabHooksImpl();
    private final MenuHooks menuHooks = new MenuHooksImpl();

    @Override
    public EntityHooks entity()
    {
        return entityHooks;
    }

    @Override
    public GameRuleHooks gameRules()
    {
        return gameRuleHooks;
    }

    @Override
    public CreativeModeTabHooks creativeModeTabs()
    {
        return creativeModeTabHooks;
    }

    @Override
    public MenuHooks menu()
    {
        return menuHooks;
    }

    @Override
    public RegisterRendererHooks registerRenderer()
    {
        return registerRendererHooks;
    }

    @Override
    public RegisterColorHandlerHooks registerColorHandler()
    {
        return colorHandlerHooks;
    }

    @Override
    public void registerEntityDefaultAttribute(Supplier<EntityType<? extends LivingEntity>> entityType, Supplier<AttributeSupplier.Builder> defaultAttributes)
    {
        FabricDefaultAttributeRegistry.register(entityType.get(), defaultAttributes.get());
    }

    @Override
    public <T extends Mob> void registerEntitySpawnPlacement(Supplier<EntityType<T>> entityType, SpawnPlacements.Type spawnPlacementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate)
    {
        SpawnPlacements.register(entityType.get(), spawnPlacementType, heightmapType, spawnPredicate);
    }
}
