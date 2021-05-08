package com.verdantartifice.primalmagic.client.events;

import java.util.Collections;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.util.GuiUtils;
import com.verdantartifice.primalmagic.common.affinities.AffinityManager;
import com.verdantartifice.primalmagic.common.config.Config;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.armor.IManaDiscountGear;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.runes.RuneManager;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only rendering events.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID, value=Dist.CLIENT)
public class ClientRenderEvents {
    @SubscribeEvent
    public static void renderTooltip(ItemTooltipEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Screen gui = mc.currentScreen;
        
        // Show a tooltip entry if the item stack grants a mana discount
        if (event.getItemStack().getItem() instanceof IManaDiscountGear) {
            int discount = ((IManaDiscountGear)event.getItemStack().getItem()).getManaDiscount(event.getItemStack(), mc.player);
            event.getToolTip().add(new TranslationTextComponent("tooltip.primalmagic.mana_discount", discount).mergeStyle(TextFormatting.DARK_AQUA));
        }
        
        // Show a tooltip entry if the item stack is runescribed
        if (RuneManager.hasRunes(event.getItemStack())) {
            event.getToolTip().add(new TranslationTextComponent("tooltip.primalmagic.runescribed").mergeStyle(TextFormatting.DARK_AQUA));
        }
        
        // Make the tooltip changes for showing primal affinities on an itemstack
        if (gui instanceof ContainerScreen && (Screen.hasShiftDown() != Config.SHOW_AFFINITIES.get().booleanValue()) && !mc.mouseHelper.isMouseGrabbed() && event.getItemStack() != null) {
            SourceList sources = AffinityManager.getInstance().getAffinityValues(event.getItemStack(), mc.world);
            if (sources == null || sources.isEmpty()) {
                event.getToolTip().add(new TranslationTextComponent("primalmagic.affinities.none"));
            } else if (!ResearchManager.isScanned(event.getItemStack(), mc.player) && !Config.SHOW_UNSCANNED_AFFINITIES.get()) {
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
                    if (event.getLines().get(index) != null && !event.getLines().get(index).getString().contains("     ")) {
                        bottom -= 10;
                    } else if (index > 0 && event.getLines().get(index - 1) != null && event.getLines().get(index - 1).getString().contains("     ")) {
                        SourceList sources = AffinityManager.getInstance().getAffinityValues(event.getStack(), mc.world);
                        GuiUtils.renderSourcesForPlayer(event.getMatrixStack(), sources, mc.player, event.getX(), event.getY() + bottom - 16);
                        break;
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onHighlightEntity(DrawHighlightEvent.HighlightEntity event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getHeldItemMainhand().getItem() == ItemsPM.ARCANOMETER.get() || mc.player.getHeldItemOffhand().getItem() == ItemsPM.ARCANOMETER.get()) {
            Entity entity = event.getTarget().getEntity();
            SourceList affinities = AffinityManager.getInstance().getAffinityValues(entity.getType());
            boolean isScanned = ResearchManager.isScanned(entity.getType(), mc.player);
            if (isScanned && affinities != null && !affinities.isEmpty()) {
                float partialTicks = event.getPartialTicks();
                double interpolatedEntityX = entity.prevPosX + (partialTicks * (entity.getPosX() - entity.prevPosX));
                double interpolatedEntityY = entity.prevPosY + (partialTicks * (entity.getPosY() - entity.prevPosY));
                double interpolatedEntityZ = entity.prevPosZ + (partialTicks * (entity.getPosZ() - entity.prevPosZ));
                GuiUtils.renderSourcesBillboard(event.getMatrix(), event.getBuffers(), interpolatedEntityX, interpolatedEntityY + entity.getHeight(), interpolatedEntityZ, affinities, partialTicks);
            }
        }
    }
}
