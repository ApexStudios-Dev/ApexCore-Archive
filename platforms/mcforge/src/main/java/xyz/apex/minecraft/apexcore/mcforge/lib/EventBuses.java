package xyz.apex.minecraft.apexcore.mcforge.lib;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xyz.apex.minecraft.apexcore.common.lib.resgen.ApexDataProvider;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Simple handler to maintain and keep track of mod event buses.
 * <p>
 * Call {@link #register(String, IEventBus)} from your mods entry point.
 * <p>
 * JavaFML mods can make use of the shortcut method {@link #registerForJavaFML()}.
 */
public final class EventBuses
{
    private static final Map<String, IEventBus> MOD_EVENT_BUS_MAP = Maps.newConcurrentMap();
    private static final Multimap<String, Consumer<IEventBus>> MOD_REGISTRATION_LISTENERS = MultimapBuilder.hashKeys().linkedListValues().build();

    /**
     * Registers and returns the given mod bus.
     *
     * @param ownerId Owner id to bind event bus to.
     * @param modBus  Event bus to bind to owner id.
     * @return Mod event bus.
     */
    public static IEventBus register(String ownerId, IEventBus modBus)
    {
        if(MOD_EVENT_BUS_MAP.put(ownerId, modBus) != null)
            throw new IllegalStateException("Attempt to replace event for mod '%s'".formatted(ownerId));

        registerForInternal(ownerId, modBus);
        MOD_REGISTRATION_LISTENERS.removeAll(ownerId).forEach(listener -> listener.accept(modBus));
        return modBus;
    }

    /**
     * Registers and returns mod event bus for currently active JavaFML mod.
     *
     * @return JavaFML event bus.
     * @see #register(String, IEventBus)
     */
    public static IEventBus registerForJavaFML()
    {
        return register(ModLoadingContext.get().getActiveNamespace(), FMLJavaModLoadingContext.get().getModEventBus());
    }

    /**
     * Registers listener to be invoked when event bus for the given owner id is registered.
     * <p>
     * Listener will immeditaly be invoked if event bus has already been registered.
     *
     * @param ownerId  Owner id of mod event bus.
     * @param listener Listener to be invoked.
     */
    public static void addListener(String ownerId, Consumer<IEventBus> listener)
    {
        var modBus = MOD_EVENT_BUS_MAP.get(ownerId);
        if(modBus == null) MOD_REGISTRATION_LISTENERS.put(ownerId, listener);
        else listener.accept(modBus);
    }

    private static void registerForInternal(String ownerId, IEventBus eventBus)
    {
        EventBusHelper.addListener(eventBus, GatherDataEvent.class, event -> ApexDataProvider.register(
                ownerId,
                func -> {
                    var generator = event.getGenerator();
                    generator.addProvider(
                            event.includeClient() || event.includeServer(),
                            func.apply(generator.getPackOutput(), event.getLookupProvider())
                    );
                })
        );
    }
}
