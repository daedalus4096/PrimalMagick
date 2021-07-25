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
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
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
        Screen gui = mc.screen;
        
        // Show a tooltip entry if the item stack grants a mana discount
        if (event.getItemStack().getItem() instanceof IManaDiscountGear) {
            int discount = ((IManaDiscountGear)event.getItemStack().getItem()).getManaDiscount(event.getItemStack(), mc.player);
            event.getToolTip().add(new TranslatableComponent("tooltip.primalmagic.mana_discount", discount).withStyle(ChatFormatting.DARK_AQUA));
        }
        
        // Show a tooltip entry if the item stack is runescribed
        if (RuneManager.hasRunes(event.getItemStack())) {
            event.getToolTip().add(new TranslatableComponent("tooltip.primalmagic.runescribed").withStyle(ChatFormatting.DARK_AQUA));
        }
        
        // Make the tooltip changes for showing primal affinities on an itemstack
        if (gui instanceof AbstractContainerScreen && (Screen.hasShiftDown() != Config.SHOW_AFFINITIES.get().booleanValue()) && !mc.mouseHandler.isMouseGrabbed() && event.getItemStack() != null) {
            SourceList sources = AffinityManager.getInstance().getAffinityValues(event.getItemStack(), mc.level);
            if (sources == null || sources.isEmpty()) {
                event.getToolTip().add(new TranslatableComponent("primalmagic.affinities.none"));
            } else if (!ResearchManager.isScanned(event.getItemStack(), mc.player) && !Config.SHOW_UNSCANNED_AFFINITIES.get()) {
                event.getToolTip().add(new TranslatableComponent("primalmagic.affinities.unknown"));
            } else {
                int width = 0;
                for (Source source : sources.getSources()) {
                    if (source != null && sources.getAmount(source) > 0) {
                        width += 18;
                    }
                }
                if (width > 0) {
                    double spaceWidth = mc.font.width(" ");
                    int charCount = Math.min(120, Mth.ceil((double)width / spaceWidth));
                    int rowCount = Mth.ceil(18.0D / (double)mc.font.lineHeight);
                    String str = String.join("", Collections.nCopies(charCount, " "));
                    event.getToolTip().add(new TranslatableComponent("primalmagic.affinities.label"));
                    for (int index = 0; index < rowCount; index++) {
                        event.getToolTip().add(new TextComponent(str));
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void renderTooltipPostBackground(RenderTooltipEvent.PostBackground event) {
        Minecraft mc = Minecraft.getInstance();
        Screen gui = mc.screen;
        
        // Show the source images for primal affinities for an itemstack
        if (gui instanceof AbstractContainerScreen && (Screen.hasShiftDown() != Config.SHOW_AFFINITIES.get().booleanValue()) && !mc.mouseHandler.isMouseGrabbed() && event.getStack() != null) {
            int bottom = event.getHeight();
            if (!event.getLines().isEmpty()) {
                for (int index = event.getLines().size() - 1; index >= 0; index--) {
                    // TODO scan for affinities label and use that as an anchor
                    if (event.getLines().get(index) != null && !event.getLines().get(index).getString().contains("     ")) {
                        bottom -= 10;
                    } else if (index > 0 && event.getLines().get(index - 1) != null && event.getLines().get(index - 1).getString().contains("     ")) {
                        SourceList sources = AffinityManager.getInstance().getAffinityValues(event.getStack(), mc.level);
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
        if (mc.player.getMainHandItem().getItem() == ItemsPM.ARCANOMETER.get() || mc.player.getOffhandItem().getItem() == ItemsPM.ARCANOMETER.get()) {
            Entity entity = event.getTarget().getEntity();
            SourceList affinities = AffinityManager.getInstance().getAffinityValues(entity.getType());
            boolean isScanned = ResearchManager.isScanned(entity.getType(), mc.player);
            if (isScanned && affinities != null && !affinities.isEmpty()) {
                float partialTicks = event.getPartialTicks();
                double interpolatedEntityX = entity.xo + (partialTicks * (entity.getX() - entity.xo));
                double interpolatedEntityY = entity.yo + (partialTicks * (entity.getY() - entity.yo));
                double interpolatedEntityZ = entity.zo + (partialTicks * (entity.getZ() - entity.zo));
                GuiUtils.renderSourcesBillboard(event.getMatrix(), event.getBuffers(), interpolatedEntityX, interpolatedEntityY + entity.getBbHeight(), interpolatedEntityZ, affinities, partialTicks);
            }
        }
    }
}
