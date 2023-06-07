package xyz.apex.minecraft.apexcore.common.lib.event.types;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.ApiStatus;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.event.Event;
import xyz.apex.minecraft.apexcore.common.lib.event.EventType;

import java.util.List;
import java.util.function.Consumer;

@SideOnly(PhysicalSide.CLIENT)
@ApiStatus.NonExtendable
public interface ScreenEvents
{
    EventType<Init> INIT = EventType.create(listeners -> screen -> listeners.forEach(listener -> listener.handle(screen)));
    EventType<PreRender> PRE_RENDER = EventType.create(listeners -> (screen, graphics, mouseX, mouseY, partialTicks) -> listeners.forEach(listener -> listener.handle(screen, graphics, mouseX, mouseY, partialTicks)));
    EventType<PostRender> POST_RENDER = EventType.create(listeners -> (screen, graphics, mouseX, mouseY, partialTicks) -> listeners.forEach(listener -> listener.handle(screen, graphics, mouseX, mouseY, partialTicks)));
    EventType<Opened> OPENED = EventType.create(listeners -> screen -> listeners.forEach(listener -> listener.handle(screen)));
    EventType<Closed> CLOSED = EventType.create(listeners -> screen -> listeners.forEach(listener -> listener.handle(screen)));
    EventType<ModifyWidgets> MODIFY_WIDGETS = EventType.create(listeners -> (screen, widgets, add, remove) -> listeners.forEach(listener -> listener.handle(screen, widgets, add, remove)));

    @ApiStatus.Internal
    static void bootstrap()
    {
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Init extends Event
    {
        void handle(Screen screen);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface PreRender extends Event
    {
        void handle(Screen screen, PoseStack stack, int mouseX, int mouseY, float partialTick);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface PostRender extends Event
    {
        void handle(Screen screen, PoseStack stack, int mouseX, int mouseY, float partialTick);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Opened extends Event
    {
        void handle(Screen screen);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface Closed extends Event
    {
        void handle(Screen screen);
    }

    @FunctionalInterface
    @ApiStatus.NonExtendable
    interface ModifyWidgets extends Event
    {
        void handle(Screen screen, List<AbstractWidget> widgets, Consumer<AbstractWidget> add, Consumer<AbstractWidget> remove);
    }
}
