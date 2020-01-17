package com.verdantartifice.primalmagic.client.events;

import java.util.Collections;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.config.Config;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only rendering events.
 * 
 * @author Michael Bunting
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, value=Dist.CLIENT)
public class ClientRenderEvents {
    @SubscribeEvent
    public static void renderTooltip(ItemTooltipEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Screen gui = mc.currentScreen;
        
        // Make the tooltip changes for showing primal affinities on an itemstack
        if (gui instanceof ContainerScreen && (Screen.hasShiftDown() != Config.SHOW_AFFINITIES.get().booleanValue()) && !mc.mouseHelper.isMouseGrabbed() && event.getItemStack() != null) {
            SourceList sources = AffinityManager.getAffinities(event.getItemStack(), mc.world);
            if (sources == null || sources.isEmpty()) {
                event.getToolTip().add(new TranslationTextComponent("primalmagic.affinities.none"));
            } else if (!AffinityManager.isScanned(event.getItemStack(), mc.player) && !Config.SHOW_UNSCANNED_AFFINITIES.get()) {
                event.getToolTip().add(new TranslationTextComponent("primalmagic.affinities.unknown"));
            } else {
                int width = 0;
                for (Source source : sources.getSources()) {
                    if (source != null && sources.getAmount(source) > 0) {
                        width += 18;
                    }
                }
                if (width > 0) {
                    double spaceWidth = mc.fontRenderer.getStringWidth(" ");
                    int charCount = Math.min(120, MathHelper.ceil((double)width / spaceWidth));
                    int rowCount = MathHelper.ceil(18.0D / (double)mc.fontRenderer.FONT_HEIGHT);
                    String str = String.join("", Collections.nCopies(charCount, " "));
                    event.getToolTip().add(new TranslationTextComponent("primalmagic.affinities.label"));
                    for (int index = 0; index < rowCount; index++) {
                        event.getToolTip().add(new StringTextComponent(str));
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void renderTooltipPostBackground(RenderTooltipEvent.PostBackground event) {
        Minecraft mc = Minecraft.getInstance();
        Screen gui = mc.currentScreen;
        
        // Show the source images for primal affinities for an itemstack
        if (gui instanceof ContainerScreen && (Screen.hasShiftDown() != Config.SHOW_AFFINITIES.get().booleanValue()) && !mc.mouseHelper.isMouseGrabbed() && event.getStack() != null) {
            int bottom = event.getHeight();
            if (!event.getLines().isEmpty()) {
                for (int index = event.getLines().size() - 1; index >= 0; index--) {
                    // TODO scan for affinities label and use that as an anchor
                    if (event.getLines().get(index) != null && !event.getLines().get(index).contains("     ")) {
                        bottom -= 10;
                    } else if (index > 0 && event.getLines().get(index - 1) != null && event.getLines().get(index - 1).contains("     ")) {
                        SourceList sources = AffinityManager.getAffinities(event.getStack(), mc.world);
                        GuiUtils.renderSourcesForPlayer(sources, mc.player, event.getX(), event.getY() + bottom - 16);
                        break;
                    }
                }
            }
        }
    }
}
