package xyz.apex.minecraft.apexcore.common.core;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import xyz.apex.lib.Services;
import xyz.apex.minecraft.apexcore.common.lib.PhysicalSide;
import xyz.apex.minecraft.apexcore.common.lib.SideOnly;
import xyz.apex.minecraft.apexcore.common.lib.hook.ColorHandlerHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.GenericHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.ParticleHooks;
import xyz.apex.minecraft.apexcore.common.lib.hook.RendererHooks;
import xyz.apex.minecraft.apexcore.common.lib.modloader.ModLoader;

@ApiStatus.Internal
@ApiStatus.NonExtendable
@SideOnly(PhysicalSide.CLIENT)
public interface ApexCoreClient
{
    ApexCoreClient INSTANCE = Services.singleton(ApexCoreClient.class);

    ColorHandlerHooks COLOR_HANDLER_HOOKS = Services.singleton(ColorHandlerHooks.class);
    RendererHooks RENDERER_HOOKS = Services.singleton(RendererHooks.class);
    GenericHooks CLIENT_HOOKS = Services.singleton(GenericHooks.class);
    ParticleHooks PARTICLE_HOOKS = Services.singleton(ParticleHooks.class);

    @MustBeInvokedByOverriders
    default void bootstrap()
    {
    }

    default void renderEarlyBuildOverlay(GuiGraphics graphics, boolean hud)
    {
        if(!ApexCore.IS_EARLY_BUILD)
            return;

        var client = Minecraft.getInstance();

        if(hud)
        {
            if(client.screen != null)
                return;
        }
        else
        {
            if(client.screen instanceof TitleScreen)
                return;
        }

        var text = Component.literal("Early Alpha").withStyle(ChatFormatting.ITALIC);
        var width = graphics.guiWidth();
        var height = graphics.guiHeight();

        var x = width - client.font.width(text) - 2;
        var y = height - client.font.lineHeight - 2;

        var color = 0x40808080;

        if(client.options.renderDebug)
        {
            for(var mod : ModLoader.get().getLoadedMods())
            {
                switch(mod.id()) {
                    case "apexcore", "itemresistance", "infusedfoods", "fantasyfurniture", "fantasydice", "apexcore_testmod" -> {
                        var modText = Component.literal("%s - v%s".formatted(mod.displayName(), mod.version())).withStyle(ChatFormatting.ITALIC);
                        client.font.drawInBatch(modText, width - client.font.width(modText) - 2, y, color, true, graphics.pose().last().pose(), graphics.bufferSource(), Font.DisplayMode.SEE_THROUGH, 0, 0xf000f0);
                        y -= client.font.lineHeight + 3;
                    }
                }
            }
        }

        client.font.drawInBatch(text, x, y, color, true, graphics.pose().last().pose(), graphics.bufferSource(), Font.DisplayMode.SEE_THROUGH, 0, 0xf000f0);

        var logoSize = 16;
        var logoX = x - logoSize - 4;
        var logoY = y - (logoSize / 2);

        graphics.drawManaged(() -> {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            graphics.setColor(1F, 1F, 1F, .125F);
            graphics.blitSprite(new ResourceLocation(ApexCore.ID, "branding/logo-16x"), logoX, logoY, logoSize, logoSize);
            graphics.setColor(1F, 1F, 1F, 1F);
            RenderSystem.disableBlend();
        });
    }
}
