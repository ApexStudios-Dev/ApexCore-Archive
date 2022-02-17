package xyz.apex.forge.apexcore.core.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.lib.item.CreativeModeTabCategory;
import xyz.apex.forge.apexcore.lib.item.CreativeModeTabCategoryManager;
import xyz.apex.forge.apexcore.lib.util.reflection.MethodHelper;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

// Simple offloading class to allow hot swapping while using mixins
@OnlyIn(Dist.CLIENT)
public final class CreativeModeInventoryScreenHandler
{
	private static final ResourceLocation CATEGORY_TABS_TEXTURE = new ResourceLocation(ApexCore.ID, "textures/gui/container/creative_inventory/category_tabs.png");
	private static final int MAX_CATEGORY_TAB = 4;
	private static final int CATEGORY_TAB_U_SIZE = 32;
	private static final int CATEGORY_TAB_V_SIZE = 28;
	private static final int CATEGORY_TABS_TEXTURE_WIDTH = CATEGORY_TAB_U_SIZE * 2;
	private static final int CATEGORY_TABS_TEXTURE_HEIGHT = CATEGORY_TAB_V_SIZE;
	private static final Method addButtonMethod = MethodHelper.findMethod(Screen.class, "addRenderableWidget", new Class[] { GuiEventListener.class });

	private static int categoryTabPage = 0;
	private static int maxCategoryTabPages = 0;
	@Nullable private static Button buttonPreviousCategoryPage;
	@Nullable private static Button buttonNextCategoryPage;

	private static void updatePages(CreativeModeInventoryScreen screen, @Nullable CreativeModeTab tab)
	{
		Validate.notNull(buttonPreviousCategoryPage);
		Validate.notNull(buttonNextCategoryPage);

		var leftPos = screen.getGuiLeft();
		var topPos = screen.getGuiTop();

		var buttonSize = 20;

		buttonPreviousCategoryPage.x = leftPos - (CATEGORY_TAB_U_SIZE * 2) + 7;
		buttonPreviousCategoryPage.y = topPos + 2;
		buttonPreviousCategoryPage.setWidth(buttonSize);
		buttonPreviousCategoryPage.setHeight(buttonSize);

		buttonNextCategoryPage.x = buttonPreviousCategoryPage.x;
		buttonNextCategoryPage.y = buttonPreviousCategoryPage.y + (CATEGORY_TAB_U_SIZE * (MAX_CATEGORY_TAB - 1));
		buttonNextCategoryPage.setWidth(buttonSize);
		buttonNextCategoryPage.setHeight(buttonSize);

		maxCategoryTabPages = 0;

		if(isSupportedTab(tab))
		{
			var manager = CreativeModeTabCategoryManager.getInstance(tab);
			var categories = manager.getCategories();
			var tabCount = categories.size();

			if(tabCount > MAX_CATEGORY_TAB)
				maxCategoryTabPages = (int) Math.ceil((tabCount - MAX_CATEGORY_TAB) / (MAX_CATEGORY_TAB - 2D));
		}
	}

	public static void init(CreativeModeInventoryScreen screen)
	{
		buttonNextCategoryPage = addButton(screen, new Button(0, 0, 1, 1, new TextComponent("V"), b -> categoryTabPage = Math.min(categoryTabPage + 1, maxCategoryTabPages)));
		buttonNextCategoryPage.active = false;
		buttonNextCategoryPage.visible = false;

		buttonPreviousCategoryPage = addButton(screen, new Button(0, 0, 1, 1, new TextComponent("^"), b -> categoryTabPage = Math.max(categoryTabPage - 1, 0)));
		buttonPreviousCategoryPage.active = false;
		buttonPreviousCategoryPage.visible = false;

		var selectedTab = getSelectedTab(screen);
		updatePages(screen, selectedTab);
	}

	public static void selectTab_FilterItems(CreativeModeInventoryScreen screen, CreativeModeTab tab)
	{
		if(isSupportedTab(tab))
			CreativeModeTabCategoryManager.getInstance(tab).applyFilter(screen.getMenu().items);
	}

	public static void selectTab_Head(CreativeModeInventoryScreen screen, CreativeModeTab tab)
	{
		var selectedTab = getSelectedTab(screen);

		if(isSupportedTab(selectedTab))
		{
			CreativeModeTabCategoryManager.getInstance(selectedTab).disableCategories();
			categoryTabPage = 0;
		}

		updatePages(screen, tab);
	}

	public static void renderBg(CreativeModeInventoryScreen screen, PoseStack pose)
	{
		var selectedTab = getSelectedTab(screen);

		if(!isSupportedTab(selectedTab))
			return;

		renderTabButton(screen, pose, selectedTab, false);
		renderTabButton(screen, pose, selectedTab, true);
	}

	public static void render(CreativeModeInventoryScreen screen, PoseStack pose)
	{
		if(maxCategoryTabPages == 0)
			return;

		var selectedTab = getSelectedTab(screen);

		if(!isSupportedTab(selectedTab))
			return;

		if(buttonPreviousCategoryPage == null || buttonNextCategoryPage == null)
			return;
		if(!buttonPreviousCategoryPage.visible)
			return;

		var minecraft = screen.getMinecraft();
		var itemRenderer = minecraft.getItemRenderer();
		var font = minecraft.font;

		screen.setBlitOffset(300);
		itemRenderer.blitOffset = 300F;

		/*var lines = new String[] {
				"" + (categoryTabPage + 1),
				"/",
				"" + (maxCategoryTabPages + 1)
		};

		var maxLineWidth = 0;

		for(var line : lines)
		{
			int lineWidth = font.width(line);
			maxLineWidth = Math.max(lineWidth, maxLineWidth);
		}*/

		var maxLineWidth = 6;

		var x = buttonPreviousCategoryPage.x + ((buttonPreviousCategoryPage.getWidth() / 2) - (maxLineWidth / 2));
		var fontColor = buttonPreviousCategoryPage.getFGColor();

		var nextY = buttonNextCategoryPage.y;
		var nextHeight = buttonNextCategoryPage.getHeight();

		var previousY = buttonPreviousCategoryPage.y;
		var previousHeight = buttonPreviousCategoryPage.getHeight();

		var yTop = previousY + previousHeight;
		var yBottom = nextY;
		var yDist = yBottom - yTop;
		var y = yTop - (font.lineHeight / 2F) + (yDist / 2F);

		font.drawShadow(pose, "" + (categoryTabPage + 1), x, y - font.lineHeight, fontColor);
		font.drawShadow(pose, "/", x, y, fontColor);
		font.drawShadow(pose, "" + (maxCategoryTabPages + 1), x, y + font.lineHeight, fontColor);

		screen.setBlitOffset(0);
		itemRenderer.blitOffset = 0F;
	}

	private static void renderTabButton(CreativeModeInventoryScreen screen, PoseStack pose, CreativeModeTab tab, boolean enabled)
	{
		if(!isSupportedTab(tab))
			return;
		if(!isSelectedTab(screen, tab))
			return;

		var manager = CreativeModeTabCategoryManager.getInstance(tab);
		var minecraft = screen.getMinecraft();
		var itemRenderer = minecraft.getItemRenderer();

		var tabIndex = 0;
		var tabsInteracted = 0;

		for(var category : manager.getCategories())
		{
			if(tabsInteracted >= MAX_CATEGORY_TAB)
				break;

			if(manager.isCategoryEnabled(category))
			{
				if(!enabled)
				{
					tabIndex++;
					continue;
				}
			}
			else
			{
				if(enabled)
				{
					tabIndex++;
					continue;
				}
			}

			if(categoryTabPage == getTabPage(tabIndex))
			{
				var u = getTabU(manager, category);
				var x = getTabX(screen);
				var y = getTabY(screen, tabIndex);

				RenderSystem.setShaderTexture(0, CATEGORY_TABS_TEXTURE);
				RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
				RenderSystem.enableBlend();

				GuiComponent.blit(pose, x, y, screen.getBlitOffset(), u, 0, CATEGORY_TAB_U_SIZE, CATEGORY_TAB_V_SIZE, CATEGORY_TABS_TEXTURE_WIDTH, CATEGORY_TABS_TEXTURE_HEIGHT);

				itemRenderer.blitOffset = 100F;

				int iconX = x + 8;
				int iconY = y + 6;

				var icon = category.getCategoryIcon(tab);
				itemRenderer.renderAndDecorateItem(icon, iconX, iconY);
				itemRenderer.renderGuiItemDecorations(minecraft.font, icon, iconX, iconY);

				itemRenderer.blitOffset = 0F;

				tabsInteracted++;
			}

			tabIndex++;
		}
	}

	public static void mouseClicked(CreativeModeInventoryScreen screen, double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir)
	{
		var selectedTab = getSelectedTab(screen);

		if(!isSupportedTab(selectedTab))
			return;
		if(!isSelectedTab(screen, selectedTab))
			return;
		if(mouseButton != GLFW_MOUSE_BUTTON_LEFT)
			return;

		var categories = CreativeModeTabCategoryManager.getInstance(selectedTab).getCategories();
		var tabsInteracted = 0;

		for(var tabIndex = 0; tabIndex < categories.size() && tabsInteracted < MAX_CATEGORY_TAB; tabIndex++)
		{
			if(categoryTabPage == getTabPage(tabIndex))
			{
				var x = getTabX(screen);
				var y = getTabY(screen, tabIndex);

				if(mouseX >= x && mouseX <= x + CATEGORY_TAB_U_SIZE && mouseY >= y && mouseY <= y + CATEGORY_TAB_V_SIZE)
				{
					cir.setReturnValue(true);
					return;
				}

				tabsInteracted++;
			}
		}
	}

	public static void mouseReleased(CreativeModeInventoryScreen screen, double mouseX, double mouseY, int mouseButton, Consumer<Float> scrollOffsModifier, CallbackInfoReturnable<Boolean> cir)
	{
		var selectedTab = getSelectedTab(screen);

		if(!isSupportedTab(selectedTab))
			return;
		if(mouseButton != GLFW_MOUSE_BUTTON_LEFT)
			return;

		var manager = CreativeModeTabCategoryManager.getInstance(selectedTab);
		var tabIndex = 0;
		var tabsInteracted = 0;

		for(var category : manager.getCategories())
		{
			if(tabsInteracted >= MAX_CATEGORY_TAB)
				break;

			if(categoryTabPage == getTabPage(tabIndex))
			{
				var tabX = getTabX(screen);
				var tabY = getTabY(screen, tabIndex);

				if(mouseX >= tabX && mouseX <= tabX + CATEGORY_TAB_U_SIZE && mouseY >= tabY && mouseY <= tabY + CATEGORY_TAB_V_SIZE)
				{
					var menu = screen.getMenu();

					manager.toggleCategory(category);
					manager.applyFilter(menu.items);

					menu.scrollTo(0F);
					scrollOffsModifier.accept(0F);

					cir.setReturnValue(true);
					return;
				}

				tabsInteracted++;
			}

			tabIndex++;
		}
	}

	public static void checkTabHovering(CreativeModeInventoryScreen screen, PoseStack pose, CreativeModeTab itemGroup, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> cir)
	{
		if(!isSupportedTab(itemGroup))
			return;
		if(!isSelectedTab(screen, itemGroup))
			return;

		var manager = CreativeModeTabCategoryManager.getInstance(itemGroup);
		var tabIndex = 0;
		var tabsInteracted = 0;

		for(var category : manager.getCategories())
		{
			if(tabsInteracted >= MAX_CATEGORY_TAB)
				break;

			if(categoryTabPage == getTabPage(tabIndex))
			{
				var tabX = getTabX(screen);
				var tabY = getTabY(screen, tabIndex);

				if(mouseX >= tabX && mouseX <= tabX + CATEGORY_TAB_U_SIZE && mouseY >= tabY && mouseY <= tabY + CATEGORY_TAB_V_SIZE)
				{
					var categoryName = category.getCategoryName();
					screen.renderTooltip(pose, categoryName, mouseX, mouseY);

					cir.setReturnValue(true);
					return;
				}

				tabsInteracted++;
			}

			tabIndex++;
		}
	}

	public static void tick(CreativeModeInventoryScreen screen, int tabPage)
	{
		if(screen.getMinecraft().gameMode != null && !screen.getMinecraft().gameMode.hasInfiniteItems())
			return;

		var selectedTab = getSelectedTab(screen);

		updateButtonState(screen, selectedTab, tabPage, true);
		updateButtonState(screen, selectedTab, tabPage, false);

		if(!isSupportedTab(selectedTab))
			return;
		if(!isSelectedTab(screen, selectedTab))
			return;

		var cycleIcons = !Screen.hasShiftDown();
		CreativeModeTabCategoryManager.getInstance(selectedTab).getCategories().forEach(c -> c.tick(cycleIcons));
	}

	private static void updateButtonState(CreativeModeInventoryScreen screen, @Nullable CreativeModeTab selectedTab, int tabPage, boolean isNext)
	{
		boolean isVisible = false;
		boolean isActive = false;

		if(maxCategoryTabPages > 0)
		{
			if(isSupportedTab(selectedTab) && isSelectedTab(screen, selectedTab))
			{
				if(tabPage == selectedTab.getTabPage())
				{
					isVisible = true;

					if(isNext)
					{
						if(categoryTabPage <= maxCategoryTabPages - 1)
							isActive = true;
					}
					else
					{
						if(categoryTabPage - 1 >= 0)
							isActive = true;
					}
				}
			}
		}

		if(!isVisible)
			isActive = false;
		if(isActive)
			isVisible = true;

		if(isNext)
		{
			if(buttonNextCategoryPage != null)
			{
				buttonNextCategoryPage.visible = isVisible;
				buttonNextCategoryPage.active = isActive;
			}
		}
		else
		{
			if(buttonPreviousCategoryPage != null)
			{
				buttonPreviousCategoryPage.visible = isVisible;
				buttonPreviousCategoryPage.active = isActive;
			}
		}
	}

	private static int getTabPage(int tabIndex)
	{
		if(tabIndex < MAX_CATEGORY_TAB)
			return 0;

		return ((tabIndex - MAX_CATEGORY_TAB) / (MAX_CATEGORY_TAB - 2)) + 1;
	}

	private static int getTabU(CreativeModeTabCategoryManager manager, CreativeModeTabCategory category)
	{
		return manager.isCategoryEnabled(category) ? CATEGORY_TAB_U_SIZE : 0;
	}

	private static int getTabX(CreativeModeInventoryScreen screen)
	{
		return (screen.getGuiLeft() - CATEGORY_TAB_U_SIZE) + 4;
	}

	private static int getTabY(CreativeModeInventoryScreen screen, int tabIndex)
	{
		var offset = (tabIndex % MAX_CATEGORY_TAB) * CATEGORY_TAB_V_SIZE;
		return screen.getGuiTop() + offset + 12;
	}

	@Nullable
	private static CreativeModeTab getSelectedTab(CreativeModeInventoryScreen screen)
	{
		var selectedTab = screen.getSelectedTab();

		if(selectedTab == -1)
			return null;

		return CreativeModeTab.TABS[selectedTab];
	}

	private static boolean isSelectedTab(CreativeModeInventoryScreen screen, CreativeModeTab tab)
	{
		return screen.getSelectedTab() == tab.getId();
	}

	private static boolean isSupportedTab(@Nullable CreativeModeTab tab)
	{
		if(tab == null)
			return false;
		if(matches(tab, CreativeModeTab.TAB_INVENTORY))
			return false;
		if(matches(tab, CreativeModeTab.TAB_HOTBAR))
			return false;
		if(matches(tab, CreativeModeTab.TAB_SEARCH))
			return false;
		if(CreativeModeTabCategoryManager.getInstance(tab).getCategories().isEmpty())
			return false;
		return true;
	}

	private static boolean matches(CreativeModeTab a, CreativeModeTab b)
	{
		return a.getId() == b.getId();
	}

	private static <T extends Widget> T addButton(Screen screen, T widget)
	{
		try
		{
			return (T) addButtonMethod.invoke(screen, widget);
		}
		catch(IllegalAccessException | InvocationTargetException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
