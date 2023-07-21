package xyz.apex.minecraft.apexcore.common.lib.event.types;

import com.google.errorprone.annotations.DoNotCall;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.InteractionHand;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.event.Event;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;

import java.util.function.Consumer;

@SideOnly(PhysicalSide.CLIENT)
@ApiStatus.NonExtendable
public interface InputEvents
{
    EventType<RegisterKeyMapping> REGISTER_KEY_MAPPING = EventType.create(listeners -> consumer -> listeners.forEach(listener -> listener.handle(consumer)));
    EventType<Key> KEY = EventType.create(listeners -> (keyCode, scanCode, action, modifiers) -> listeners.forEach(listener -> listener.handle(keyCode, scanCode, action, modifiers)));
    EventType<Click> CLICK = EventType.create(listeners -> (attack, use, pick, hand) -> listeners.stream().anyMatch(listener -> listener.handle(attack, use, pick, hand)));

    @ApiStatus.Internal
    @DoNotCall
    static void bootstrap()
    {
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface RegisterKeyMapping extends Event
    {
        void handle(Consumer<KeyMapping> consumer);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Key extends Event
    {
        void handle(int keyCode, int scanCode, int action, int modifiers);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Click extends Event
    {
        boolean handle(boolean attack, boolean use, boolean pick, InteractionHand hand);
    }
}
