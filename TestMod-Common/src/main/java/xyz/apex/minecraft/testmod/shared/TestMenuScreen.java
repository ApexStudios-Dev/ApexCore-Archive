package xyz.apex.minecraft.testmod.shared;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import xyz.apex.minecraft.apexcore.shared.inventory.InventoryMenuScreen;

public final class TestMenuScreen extends InventoryMenuScreen<TestMenu>
{
    private static final ResourceLocation BACKGROUND = new ResourceLocation(TestMod.ID, "textures/gui/container/test_menu.png");

    public TestMenuScreen(TestMenu menu, Inventory inventory, Component component)
    {
        super(menu, inventory, component, BACKGROUND);
    }

    @Override
    protected void init()
    {
        imageWidth = 176;
        imageHeight = 166;

        super.init();

        inventoryLabelY = imageHeight - 94;
    }
}
