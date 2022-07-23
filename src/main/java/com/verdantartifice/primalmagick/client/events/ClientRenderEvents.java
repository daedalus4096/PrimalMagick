package com.verdantartifice.primalmagick.client.events;

import com.mojang.datafixers.util.Either;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.client.gui.SpellSelectionRadialScreen;
import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagick.common.config.Config;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.armor.IManaDiscountGear;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.sources.SourceList;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Respond to client-only rendering events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID, value=Dist.CLIENT)
public class ClientRenderEvents {
    @SubscribeEvent
    public static void renderTooltip(ItemTooltipEvent event) {
        Minecraft mc = Minecraft.getInstance();
        
        // Show a tooltip entry if the item stack grants a mana discount
        if (event.getItemStack().getItem() instanceof IManaDiscountGear) {
            int discount = ((IManaDiscountGear)event.getItemStack().getItem()).getManaDiscount(event.getItemStack(), mc.player);
            event.getToolTip().add(Component.translatable("tooltip.primalmagick.mana_discount", discount).withStyle(ChatFormatting.DARK_AQUA));
        }
        
        // Show a tooltip entry if the item stack is runescribed
        if (RuneManager.hasRunes(event.getItemStack())) {
            event.getToolTip().add(Component.translatable("tooltip.primalmagick.runescribed").withStyle(ChatFormatting.DARK_AQUA));
        }
    }
    
    @SubscribeEvent
    public static void onRenderTooltipGatherComponents(RenderTooltipEvent.GatherComponents event) {
        Minecraft mc = Minecraft.getInstance();
        Screen gui = mc.screen;

        // Assemble the tooltip components for showing primal affinities on an item stack
        if (gui instanceof AbstractContainerScreen && (Screen.hasShiftDown() != Config.SHOW_AFFINITIES.get().booleanValue()) && !mc.mouseHandler.isMouseGrabbed() && event.getItemStack() != null && !event.getItemStack().isEmpty()) {
            SourceList sources = AffinityManager.getInstance().getAffinityValues(event.getItemStack(), mc.level);
            if (sources == null || sources.isEmpty()) {
                event.getTooltipElements().add(Either.left(Component.translatable("primalmagick.affinities.none")));
            } else if (!ResearchManager.isScanned(event.getItemStack(), mc.player) && !Config.SHOW_UNSCANNED_AFFINITIES.get()) {
                event.getTooltipElements().add(Either.left(Component.translatable("primalmagick.affinities.unknown")));
            } else {
                event.getTooltipElements().add(Either.left(Component.translatable("primalmagick.affinities.label")));
                event.getTooltipElements().add(Either.right(new AffinityTooltipComponent(sources)));
            }
        }
    }
    
    @SubscribeEvent
    public static void onHighlightEntity(RenderHighlightEvent.Entity event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.getMainHandItem().getItem() == ItemsPM.ARCANOMETER.get() || mc.player.getOffhandItem().getItem() == ItemsPM.ARCANOMETER.get()) {
            Entity entity = event.getTarget().getEntity();
            SourceList affinities = AffinityManager.getInstance().getAffinityValues(entity.getType());
            boolean isScanned = ResearchManager.isScanned(entity.getType(), mc.player);
            if (isScanned && affinities != null && !affinities.isEmpty()) {
                float partialTicks = event.getPartialTick();
                double interpolatedEntityX = entity.xo + (partialTicks * (entity.getX() - entity.xo));
                double interpolatedEntityY = entity.yo + (partialTicks * (entity.getY() - entity.yo));
                double interpolatedEntityZ = entity.zo + (partialTicks * (entity.getZ() - entity.zo));
                GuiUtils.renderSourcesBillboard(event.getPoseStack(), event.getMultiBufferSource(), interpolatedEntityX, interpolatedEntityY + entity.getBbHeight(), interpolatedEntityZ, affinities, partialTicks);
            }
        }
    }
    
    @SubscribeEvent
    public static void onRenderGameOverlayPreLayer(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type() && mc.screen instanceof SpellSelectionRadialScreen) {
            event.setCanceled(true);
        }
    }
}
