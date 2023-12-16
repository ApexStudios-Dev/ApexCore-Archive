package dev.apexstudios.testmod.common.ref;

import dev.apexstudios.apexcore.common.menu.BaseMenuScreen;
import dev.apexstudios.apexcore.common.registry.DeferredHolder;
import dev.apexstudios.testmod.common.TestMod;
import dev.apexstudios.testmod.common.menu.TestMenu;
import net.minecraft.world.inventory.MenuType;

public interface TestMenus
{
    DeferredHolder<MenuType<?>, MenuType<TestMenu>> TEST_MENU_TYPE = TestMod.REGISTER.<TestMenu, BaseMenuScreen<TestMenu>>menu("test_menu", TestMenu::forNetwork, () -> () -> BaseMenuScreen::new);
}
