package xyz.apex.forge.apexcore.core.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import xyz.apex.forge.apexcore.core.ApexCore;
import xyz.apex.forge.apexcore.lib.item.ItemGroupCategory;
import xyz.apex.forge.apexcore.lib.item.ItemGroupCategoryManager;
import xyz.apex.forge.apexcore.lib.util.reflection.MethodHelper;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

// Simple offloading class to allow hot swapping while using mixins
@OnlyIn(Dist.CLIENT)
public final class CreativeScreenHandler
{
	private static final ResourceLocation CATEGORY_TABS_TEXTURE = new ResourceLocation(ApexCore.ID, "textures/gui/container/creative_inventory/category_tabs.png");
	private static final int MAX_CATEGORY_TAB = 4;
	private static final int CATEGORY_TAB_U_SIZE = 32;
	private static final int CATEGORY_TAB_V_SIZE = 28;
	private static final int CATEGORY_TABS_TEXTURE_WIDTH = CATEGORY_TAB_U_SIZE * 2;
	private static final int CATEGORY_TABS_TEXTURE_HEIGHT = CATEGORY_TAB_V_SIZE;
	private static final Method addButtonMethod = MethodHelper.findMethod(Screen.class, "addButton", new Class[] { Widget.class });

	private static int categoryTabPage = 0;
	private static int maxCategoryTabPages = 0;
	@Nullable private static Button buttonPreviousCategoryPage;
	@Nullable private static Button buttonNextCategoryPage;

	private static void updatePages(CreativeScreen screen, @Nullable ItemGroup itemGroup)
	{
		Validate.notNull(buttonPreviousCategoryPage);
		Validate.notNull(buttonNextCategoryPage);

		int leftPos = screen.getGuiLeft();
		int topPos = screen.getGuiTop();

		int buttonSize = 20;

		buttonPreviousCategoryPage.x = leftPos - (CATEGORY_TAB_U_SIZE * 2) + 7;
		buttonPreviousCategoryPage.y = topPos + 2;
		buttonPreviousCategoryPage.setWidth(buttonSize);
		buttonPreviousCategoryPage.setHeight(buttonSize);

		buttonNextCategoryPage.x = buttonPreviousCategoryPage.x;
		buttonNextCategoryPage.y = buttonPreviousCategoryPage.y + (CATEGORY_TAB_U_SIZE * (MAX_CATEGORY_TAB - 1));
		buttonNextCategoryPage.setWidth(buttonSize);
		buttonNextCategoryPage.setHeight(buttonSize);

		maxCategoryTabPages = 0;
		buttonPreviousCategoryPage.active = false;
		buttonPreviousCategoryPage.visible = false;

		buttonNextCategoryPage.active = false;
		buttonNextCategoryPage.visible = false;

		if(isSupportedTab(itemGroup))
		{
			ItemGroupCategoryManager manager = ItemGroupCategoryManager.getInstance(itemGroup);
			Set<ItemGroupCategory> categories = manager.getCategories();
			int tabCount = categories.size();

			if(tabCount > MAX_CATEGORY_TAB)
			{
				maxCategoryTabPages = (int) Math.ceil((tabCount - MAX_CATEGORY_TAB) / (MAX_CATEGORY_TAB - 2D));

				buttonPreviousCategoryPage.active = true;
				buttonPreviousCategoryPage.visible = true;

				buttonNextCategoryPage.active = true;
				buttonNextCategoryPage.visible = true;
			}
		}
	}

	public static void init(CreativeScreen screen)
	{
		buttonNextCategoryPage = addButton(screen, new Button(0, 0, 1, 1, new StringTextComponent("V"), b -> categoryTabPage = Math.min(categoryTabPage + 1, maxCategoryTabPages)));
		buttonPreviousCategoryPage = addButton(screen, new Button(0, 0, 1, 1, new StringTextComponent("^"), b -> categoryTabPage = Math.max(categoryTabPage - 1, 0)));

		ItemGroup selectedTab = getSelectedTab(screen);
		updatePages(screen, selectedTab);
	}

	public static void selectTab_FilterItems(CreativeScreen screen, ItemGroup itemGroup)
	{
		if(isSupportedTab(itemGroup))
			ItemGroupCategoryManager.getInstance(itemGroup).applyFilter(screen.getMenu().items);
	}

	public static void selectTab_Head(CreativeScreen screen, ItemGroup itemGroup)
	{
		ItemGroup selectedTab = getSelectedTab(screen);

		if(isSupportedTab(selectedTab))
		{
			ItemGroupCategoryManager.getInstance(selectedTab).disableCategories();
			categoryTabPage = 0;
		}

		updatePages(screen, itemGroup);
	}

	public static void renderBg(CreativeScreen screen, MatrixStack pose, int mouseX, int mouseY)
	{
		ItemGroup selectedTab = getSelectedTab(screen);

		if(!isSupportedTab(selectedTab))
			return;

		renderTabButton(screen, pose, selectedTab, false);
		renderTabButton(screen, pose, selectedTab, true);
	}

	public static void render(CreativeScreen screen, MatrixStack pose)
	{
		if(maxCategoryTabPages == 0)
			return;

		ItemGroup selectedTab = getSelectedTab(screen);

		if(!isSupportedTab(selectedTab))
			return;

		Minecraft minecraft = screen.getMinecraft();
		ItemRenderer itemRenderer = minecraft.getItemRenderer();
		FontRenderer font = minecraft.font;

		RenderSystem.disableLighting();
		screen.setBlitOffset(300);
		itemRenderer.blitOffset = 300F;

		/*String[] lines = new String[] {
				"" + (categoryTabPage + 1),
				"/",
				"" + (maxCategoryTabPages + 1)
		};

		int maxLineWidth = 0;

		for(String line : lines)
		{
			int lineWidth = font.width(line);
			maxLineWidth = Math.max(lineWidth, maxLineWidth);
		}*/

		int maxLineWidth = 6;

		int x = buttonPreviousCategoryPage.x + ((buttonPreviousCategoryPage.getWidth() / 2) - (maxLineWidth / 2));
		int fontColor = buttonPreviousCategoryPage.getFGColor();

		int nextY = buttonNextCategoryPage.y;
		int nextHeight = buttonNextCategoryPage.getHeight();

		int previousY = buttonPreviousCategoryPage.y;
		int previousHeight = buttonPreviousCategoryPage.getHeight();

		float yTop = previousY + previousHeight;
		float yBottom = nextY;
		float yDist = yBottom - yTop;
		float y = yTop - (font.lineHeight / 2F) + (yDist / 2F);

		font.drawShadow(pose, "" + (categoryTabPage + 1), x, y - font.lineHeight, fontColor);
		font.drawShadow(pose, "/", x, y, fontColor);
		font.drawShadow(pose, "" + (maxCategoryTabPages + 1), x, y + font.lineHeight, fontColor);

		screen.setBlitOffset(0);
		itemRenderer.blitOffset = 0F;
	}

	private static void renderTabButton(CreativeScreen screen, MatrixStack pose, ItemGroup itemGroup, boolean enabled)
	{
		if(!isSupportedTab(itemGroup))
			return;
		if(!isSelectedTab(screen, itemGroup))
			return;

		ItemGroupCategoryManager manager = ItemGroupCategoryManager.getInstance(itemGroup);
		Minecraft minecraft = screen.getMinecraft();
		ItemRenderer itemRenderer = minecraft.getItemRenderer();
		TextureManager textureManager = minecraft.getTextureManager();

		int tabIndex = 0;
		int tabsInteracted = 0;

		for(ItemGroupCategory category : manager.getCategories())
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
				int u = getTabU(manager, category);
				int x = getTabX(screen);
				int y = getTabY(screen, tabIndex);

				textureManager.bind(CATEGORY_TABS_TEXTURE);

				RenderSystem.color3f(1F, 1F, 1F);
				RenderSystem.enableBlend();

				AbstractGui.blit(pose, x, y, screen.getBlitOffset(), u, 0, CATEGORY_TAB_U_SIZE, CATEGORY_TAB_V_SIZE, CATEGORY_TABS_TEXTURE_HEIGHT, CATEGORY_TABS_TEXTURE_WIDTH);

				itemRenderer.blitOffset = 100F;
				RenderSystem.enableRescaleNormal();

				int iconX = x + 8;
				int iconY = y + 6;

				ItemStack icon = category.getCategoryIcon(itemGroup);
				itemRenderer.renderAndDecorateItem(icon, iconX, iconY);
				itemRenderer.renderGuiItemDecorations(minecraft.font, icon, iconX, iconY);

				itemRenderer.blitOffset = 0F;

				tabsInteracted++;
			}

			tabIndex++;
		}
	}

	public static void mouseClicked(CreativeScreen screen, double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> cir)
	{
		ItemGroup selectedTab = getSelectedTab(screen);

		if(!isSupportedTab(selectedTab))
			return;
		if(!isSelectedTab(screen, selectedTab))
			return;
		if(mouseButton != GLFW_MOUSE_BUTTON_LEFT)
			return;

		Set<ItemGroupCategory> categories = ItemGroupCategoryManager.getInstance(selectedTab).getCategories();
		int tabsInteracted = 0;

		for(int tabIndex = 0; tabIndex < categories.size() && tabsInteracted < MAX_CATEGORY_TAB; tabIndex++)
		{
			if(categoryTabPage == getTabPage(tabIndex))
			{
				int x = getTabX(screen);
				int y = getTabY(screen, tabIndex);

				if(mouseX >= x && mouseX <= x + CATEGORY_TAB_U_SIZE && mouseY >= y && mouseY <= y + CATEGORY_TAB_V_SIZE)
				{
					cir.setReturnValue(true);
					return;
				}

				tabsInteracted++;
			}
		}
	}

	public static void mouseReleased(CreativeScreen screen, double mouseX, double mouseY, int mouseButton, Consumer<Float> scrollOffsModifier, CallbackInfoReturnable<Boolean> cir)
	{
		ItemGroup selectedTab = getSelectedTab(screen);

		if(!isSupportedTab(selectedTab))
			return;
		if(mouseButton != GLFW_MOUSE_BUTTON_LEFT)
			return;

		ItemGroupCategoryManager manager = ItemGroupCategoryManager.getInstance(selectedTab);
		int tabIndex = 0;
		int tabsInteracted = 0;

		for(ItemGroupCategory category : manager.getCategories())
		{
			if(tabsInteracted >= MAX_CATEGORY_TAB)
				break;

			if(categoryTabPage == getTabPage(tabIndex))
			{
				int tabX = getTabX(screen);
				int tabY = getTabY(screen, tabIndex);

				if(mouseX >= tabX && mouseX <= tabX + CATEGORY_TAB_U_SIZE && mouseY >= tabY && mouseY <= tabY + CATEGORY_TAB_V_SIZE)
				{
					CreativeScreen.CreativeContainer menu = screen.getMenu();

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

	public static void checkTabHovering(CreativeScreen screen, MatrixStack pose, ItemGroup itemGroup, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> cir)
	{
		if(!isSupportedTab(itemGroup))
			return;
		if(!isSelectedTab(screen, itemGroup))
			return;

		ItemGroupCategoryManager manager = ItemGroupCategoryManager.getInstance(itemGroup);
		int tabIndex = 0;
		int tabsInteracted = 0;

		for(ItemGroupCategory category : manager.getCategories())
		{
			if(tabsInteracted >= MAX_CATEGORY_TAB)
				break;

			if(categoryTabPage == getTabPage(tabIndex))
			{
				int tabX = getTabX(screen);
				int tabY = getTabY(screen, tabIndex);

				if(mouseX >= tabX && mouseX <= tabX + CATEGORY_TAB_U_SIZE && mouseY >= tabY && mouseY <= tabY + CATEGORY_TAB_V_SIZE)
				{
					ITextComponent categoryName = category.getCategoryName();
					screen.renderTooltip(pose, categoryName, mouseX, mouseY);

					cir.setReturnValue(true);
					return;
				}

				tabsInteracted++;
			}

			tabIndex++;
		}
	}

	public static void tick(CreativeScreen screen)
	{
		if(screen.getMinecraft().gameMode != null && !screen.getMinecraft().gameMode.hasInfiniteItems())
			return;

		ItemGroup selectedTab = getSelectedTab(screen);

		if(!isSupportedTab(selectedTab))
			return;
		if(!isSelectedTab(screen, selectedTab))
			return;

		boolean cycleIcons = !Screen.hasShiftDown();
		ItemGroupCategoryManager.getInstance(selectedTab).getCategories().forEach(c -> c.tick(cycleIcons));
	}

	private static int getTabPage(int tabIndex)
	{
		if(tabIndex < MAX_CATEGORY_TAB)
			return 0;

		return ((tabIndex - MAX_CATEGORY_TAB) / (MAX_CATEGORY_TAB - 2)) + 1;
	}

	private static int getTabU(ItemGroupCategoryManager manager, ItemGroupCategory category)
	{
		return manager.isCategoryEnabled(category) ? CATEGORY_TAB_U_SIZE : 0;
	}

	private static int getTabX(CreativeScreen screen)
	{
		return (screen.getGuiLeft() - CATEGORY_TAB_U_SIZE) + 4;
	}

	private static int getTabY(CreativeScreen screen, int tabIndex)
	{
		int offset = (tabIndex % MAX_CATEGORY_TAB) * CATEGORY_TAB_V_SIZE;
		return screen.getGuiTop() + offset + 12;
	}

	@Nullable
	private static ItemGroup getSelectedTab(CreativeScreen screen)
	{
		int selectedTab = screen.getSelectedTab();

		if(selectedTab == -1)
			return null;

		return ItemGroup.TABS[selectedTab];
	}

	private static boolean isSelectedTab(CreativeScreen screen, ItemGroup itemGroup)
	{
		return screen.getSelectedTab() == itemGroup.getId();
	}

	private static boolean isSupportedTab(@Nullable ItemGroup itemGroup)
	{
		if(itemGroup == null)
			return false;
		if(matches(itemGroup, ItemGroup.TAB_INVENTORY))
			return false;
		if(matches(itemGroup, ItemGroup.TAB_HOTBAR))
			return false;
		if(matches(itemGroup, ItemGroup.TAB_SEARCH))
			return false;
		if(ItemGroupCategoryManager.getInstance(itemGroup).getCategories().isEmpty())
			return false;
		return true;
	}

	private static boolean matches(ItemGroup a, ItemGroup b)
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
