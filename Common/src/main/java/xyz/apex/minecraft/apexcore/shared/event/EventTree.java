package xyz.apex.minecraft.apexcore.shared.event;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

final class EventTree
{
    private final TreeElement root;

    EventTree()
    {
        root = new TreeElement(Event.class);
    }

    <T extends Event> EventFunction<T> add(Consumer<T> function, Class<T> eventClass, int priority)
    {
        var closest = getClosest(eventClass);
        TreeElement element;

        if(closest.children != null && closest.children.containsKey(eventClass)) element = closest.children.get(eventClass);
        else element = new TreeElement(eventClass);

        var functionData = new EventFunction<>(function, eventClass, priority);
        element.functions.add(functionData);
        Collections.sort(element.functions);

        Class<?> clazz = eventClass;

        while((clazz = clazz.getSuperclass()) != Event.class && clazz != closest.eventClass)
        {
            var parent = new TreeElement(clazz);
            parent.addChild(element);
            element = element.parent = parent;
        }

        element.parent = closest;
        closest.addChild(element);
        return functionData;
    }

    <T extends Event> void remove(EventFunction<T> functionData)
    {
        var eventClass = functionData.eventClass();
        var element = getClosest(eventClass);
        if(element.children != null && element.children.get(eventClass) != null) element.children.get(eventClass).removeFunction(functionData);
    }

    private <T extends Event> TreeElement getClosest(Class<T> eventClass)
    {
        var branch = new Stack<Class<?>>();
        Class<?> current = eventClass;

        while((current = current.getSuperclass()) != Event.class)
        {
            branch.push(current);
        }

        var element = root;

        while(!branch.isEmpty())
        {
            if(element.children != null && element.children.containsKey(branch.peek())) element = element.children.get(branch.pop());
            else break;
        }

        return element;
    }

    void execute(Event event)
    {
        var eventClass = event.getClass();
        var element = getClosest(eventClass);
        if(element.children != null && element.children.containsKey(eventClass)) element.children.get(eventClass).execute(event);
    }

    @Override
    public String toString()
    {
        return root.toString();
    }

    static final class TreeElement
    {
        @Nullable TreeElement parent;
        @Nullable Map<Class<?>, TreeElement> children;
        Class<?> eventClass;
        List<EventFunction<?>> functions = Lists.newArrayList();

        TreeElement(Class<?> eventClass)
        {
            this.eventClass = eventClass;
        }

        <T extends Event> void removeFunction(EventFunction<T> function)
        {
            functions.removeIf(r -> r.equals(function));
        }

        void addChild(TreeElement element)
        {
            if(children == null) children = Maps.newHashMap();
            children.put(element.eventClass, element);
        }

        void execute(Event event)
        {
            functions.forEach(function -> executeEventFunction(function.function(), event));
            if(parent != null) parent.execute(event);
        }

        @SuppressWarnings("unchecked")
        <T extends Event> void executeEventFunction(Consumer<T> function, Event event)
        {
            function.accept((T) event);
        }

        @Override
        public int hashCode()
        {
            return eventClass.hashCode();
        }

        @Override
        public String toString()
        {
            return toString(0);
        }

        String toString(int depth)
        {
            var d = "%d %s".formatted(depth, " ".repeat(depth));

            var out = new StringBuilder()
                    .append(d).append("parent=").append(parent == null ? "null" : parent.eventClass.getSimpleName())
                    .append('\n').append(d).append("eventClass=").append(eventClass.getSimpleName())
                    .append('\n').append(d).append("functions=").append(Arrays.toString(functions.toArray()))
                    .append('\n').append(d).append("children:")
            ;

            if(children == null) return out.append("null\n").toString();
            else
            {
                out.append("\n");
                children.values().stream().map(element -> element.toString(depth + 1)).forEach(out::append);
                return out.toString();
            }
        }
    }
}
