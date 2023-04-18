package xyz.apex.minecraft.apexcore.common.event;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.utils.events.EventType;

@ApiStatus.Internal
public interface EventFactory
{
    /**
     * Do not use this event to directly check if entity is a FakePlayer or not.
     * <br>Make use of {@link xyz.apex.minecraft.apexcore.common.hooks.EntityHooks#isFakePlayer(Entity)}
     * <p>
     * Use this Event to mark Entities as FakePlayers.<br>
     * This is mainly useful for Fabric platforms or with Entities that are not strictly Players but could be considered a FakePlayer.
     * <p>
     * We pass whether the platform considers the Entity as a FakePlayer or not to the Event
     * and only if the platform or the Event marked the Entity as a FakePlayer,
     * do we consider the Entity as a FakePlayer
     * <ul>
     *     <li>
     *         On Forge we make use of the FakePlayer class using an instanceof check and pass that along to the Event
     *     </li>
     *     <li>
     *         On Fabric we heavily rely on the Event itself.<br>
     *              Although Technici4n does currently have a <a href="https://github.com/FabricMC/fabric/pull/3005">PR</a> open into FAPI which will add a FakePlayer API.<br>
     *              We use reflection to check if his FakePlayer class exists,<br>
     *              and if it does we register a Event listener which will mark any Entities that are instances of his class as FakePlayers.
     *     </li>
     * </ul>
     */
    EventType<FakePlayerEvent> FAKE_PLAYER = EventType.register(FakePlayerEvent.class, Entity.class, boolean.class);

    /**
     * For internal usages only, Use {@link xyz.apex.minecraft.apexcore.common.hooks.EntityHooks#isFakePlayer(Entity)}
     *
     * @see #FAKE_PLAYER
     */
    static boolean isFakePlayer(Entity entity, boolean platformConsidersFakePlayer)
    {
        var result = FAKE_PLAYER.post(entity, platformConsidersFakePlayer);
        return result.map(FakePlayerEvent::shouldBeConsideredAsFakePlayer).orElseGet(() -> platformConsidersFakePlayer || FakePlayerEvent.isFakePlayer_Default(entity));
    }

    static void bootstrap() {}
}
