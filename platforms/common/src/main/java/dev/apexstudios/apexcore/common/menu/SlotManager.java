package dev.apexstudios.apexcore.common.menu;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class SlotManager implements Iterable<Slot>
{
    public static final String GROUP_PLAYER = "player";
    public static final String GROUP_GHOST = "ghosts";

    private final Int2ObjectMap<String> slotGroups = new Int2ObjectOpenHashMap<>();
    private final ArrayListMultimap<String, Slot> byGroup = ArrayListMultimap.create();
    private final Multimap<String, String> shiftTargets = HashMultimap.create();
    private final BaseMenu menu;
    private final Consumer<Slot> slotRegistrar;

    SlotManager(BaseMenu menu, Consumer<Slot> slotRegistrar)
    {
        this.menu = menu;
        this.slotRegistrar = slotRegistrar;
    }

    public BaseMenu menu()
    {
        return menu;
    }

    public int size()
    {
        return menu.slots.size();
    }

    public Slot get(int slotIndex)
    {
        Objects.checkIndex(slotIndex, size());
        return menu.getSlot(slotIndex);
    }

    public Set<String> groups()
    {
        return byGroup.keySet();
    }

    public Collection<String> shiftTargets(String group)
    {
        return shiftTargets.get(group);
    }

    @Nullable
    public String getGroup(int slotIndex)
    {
        return slotIndex < 0 || slotIndex >= size() ? null : slotGroups.get(slotIndex);
    }

    @Nullable
    public String getGroup(Slot slot)
    {
        return slotGroups.get(slot.index);
    }

    public List<Slot> getForGroup(String group)
    {
        return Collections.unmodifiableList(byGroup.get(group));
    }

    public List<Slot> getPlayerSlots()
    {
        return getForGroup(GROUP_PLAYER);
    }

    public List<Slot> getGhostSlots()
    {
        return getForGroup(GROUP_GHOST);
    }

    public void addShiftTarget(String group, String targetGroup, boolean bidirectional)
    {
        shiftTargets.put(group, targetGroup);

        if(!group.equals(targetGroup) && bidirectional)
            shiftTargets.put(targetGroup, group);
    }

    public void addGhostSlots(int x, int y, int rows, int columns, UnaryOperator<GhostSlot> modifier)
    {
        addSlots(GROUP_GHOST, x, y, rows, columns, (slotX, slotY, slotIndex) -> modifier.apply(new GhostSlot(slotX, slotY)));
    }

    public void addGhostSlots(int x, int y, int rows, int columns)
    {
        addGhostSlots(x, y, rows, columns, UnaryOperator.identity());
    }

    public GhostSlot addGhostSlot(int x, int y)
    {
        return addGhostSlot(new GhostSlot(x, y));
    }

    public GhostSlot addGhostSlot(GhostSlot slot)
    {
        return addSlot(slot, GROUP_GHOST);
    }

    public void addSlots(Container container, String group, int x, int y, int rows, int columns)
    {
        addSlots(container, group, x, y, rows, columns, 0);
    }

    public void addSlots(Container container, String group, int x, int y, int rows, int columns, int slotIndexOffset)
    {
        addSlots(group, x, y, rows, columns, slotIndexOffset, (slotX, slotY, slotIndex) -> new Slot(container, slotIndex, slotX, slotY));
    }

    public void addSlots(String group, int x, int y, int rows, int columns, Factory factory)
    {
        addSlots(group, x, y, rows, columns, 0, factory);
    }

    public void addSlots(String group, int x, int y, int rows, int columns, int slotIndexOffset, Factory factory)
    {
        for(var j = 0; j < rows; j++)
        {
            for(var i = 0; i < columns; i++)
            {
                addSlot(factory.create(x + i * 18, y + j * 18, (i + j * columns) + slotIndexOffset), group);
            }
        }
    }

    public Slot addSlot(Container container, String group, int x, int y, int slotIndex)
    {
        return addSlot(new Slot(container, slotIndex, x, y), group);
    }

    public <T extends Slot> T addSlot(T slot, String group)
    {
        // menu.addSlot(slot);
        slotRegistrar.accept(slot);

        slotGroups.put(slot.index, group);
        byGroup.put(group, slot);
        return slot;
    }

    public void addPlayerSlots(Inventory inventory, int x, int y, boolean lockSelected)
    {
        addSlots(inventory, GROUP_PLAYER, x, y, 3, 9, 9);

        if(lockSelected)
            addSlots(GROUP_PLAYER, x, y + 58, 1, 9, (slotX, slotY, slotIndex) -> lockedIfSelected(inventory, slotX, slotY, slotIndex));
        else
            addSlots(inventory, GROUP_PLAYER, x, y + 58, 1, 9);
    }

    public void addPlayerSlots(Inventory inventory, int x, int y)
    {
        addPlayerSlots(inventory, x, y, false);
    }

    public void addPlayerSlots(Inventory inventory, boolean lockSelected)
    {
        addPlayerSlots(inventory, (menu.width() - 160) / 2, menu.height() - 82, lockSelected);
    }

    public void addPlayerSlots(Inventory inventory)
    {
        addPlayerSlots(inventory, (menu.width() - 160) / 2, menu.height() - 82, false);
    }

    private Slot lockedIfSelected(Inventory inventory, int slotX, int slotY, int slotIndex)
    {
        return new Slot(inventory, slotIndex, slotX, slotY)
        {
            @Override
            public boolean mayPickup(Player player)
            {
                return slotIndex != inventory.selected;
            }

            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return slotIndex != inventory.selected;
            }

            @Override
            public boolean isHighlightable()
            {
                return false;
            }
        };
    }

    public ItemStack handleShiftClick(Player player, int slotIndex, ItemMovementHandler movementHandler)
    {
        if(slotIndex < 0 || slotIndex >= size())
            return ItemStack.EMPTY;

        var slot = get(slotIndex);
        var group = getGroup(slotIndex);

        if(group == null || !slot.hasItem() || !slot.mayPickup(player))
            return ItemStack.EMPTY;

        var shifting = slot.getItem();
        var original = shifting.copy();

        for(var shiftTarget : shiftTargets.get(group))
        {
            for(var targetSlot : getForGroup(shiftTarget))
            {
                if(movementHandler.moveItemStackTo(shifting, targetSlot.index, targetSlot.index + 1, false))
                {
                    if(shifting.isEmpty())
                        slot.setByPlayer(ItemStack.EMPTY);
                    else
                        slot.setChanged();

                    return original;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public Iterator<Slot> iterator()
    {
        return menu.slots.iterator();
    }

    @FunctionalInterface
    public interface Factory
    {
        Slot create(int x, int y, int slotIndex);
    }

    @FunctionalInterface
    public interface ItemMovementHandler
    {
        boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reversed);
    }
}
