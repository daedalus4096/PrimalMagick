package com.verdantartifice.primalmagic.client.events;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
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
    protected static final Logger LOGGER = LogManager.getLogger();
    
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
            if (affinities != null && !affinities.isEmpty()) {  // FIXME Only show affinities if the entity has been scanned
                float partialTicks = event.getPartialTicks();
                double interpolatedPlayerX = mc.player.prevPosX + (partialTicks * (mc.player.getPosX() - mc.player.prevPosX));
                double interpolatedPlayerY = mc.player.prevPosY + (partialTicks * (mc.player.getPosY() - mc.player.prevPosY));
                double interpolatedPlayerZ = mc.player.prevPosZ + (partialTicks * (mc.player.getPosZ() - mc.player.prevPosZ));
                double interpolatedEntityX = entity.prevPosX + (partialTicks * (entity.getPosX() - entity.prevPosX));
                double interpolatedEntityY = entity.prevPosY + (partialTicks * (entity.getPosY() - entity.prevPosY));
                double interpolatedEntityZ = entity.prevPosZ + (partialTicks * (entity.getPosZ() - entity.prevPosZ));
                double dx = (interpolatedPlayerX - interpolatedEntityX + 0.5D);
                double dz = (interpolatedPlayerZ - interpolatedEntityZ + 0.5D);
                float rotYaw = 180.0F + (float)(MathHelper.atan2(dx, dz) * 180.0D / Math.PI);
                float scale = 0.03F;
                double shiftX = 0.0D;
                double startDeltaX = ((16.0D * affinities.getSources().size()) / 2.0D) * scale;
                
                for (Source source : affinities.getSourcesSorted()) {
                    MatrixStack matrixStack = event.getMatrix();
                    matrixStack.push();
                    matrixStack.translate(interpolatedEntityX - interpolatedPlayerX, interpolatedEntityY - interpolatedPlayerY + entity.getHeight() - 0.5F, interpolatedEntityZ - interpolatedPlayerZ);
                    matrixStack.rotate(Vector3f.YP.rotationDegrees(rotYaw));
                    matrixStack.rotate(Vector3f.ZP.rotationDegrees(180.0F));
                    matrixStack.translate(shiftX - startDeltaX, 0.0D, 0.0D);
                    matrixStack.scale(scale, scale, scale);
                    
                    @SuppressWarnings("deprecation")
                    TextureAtlasSprite sprite = mc.getModelManager().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).getSprite(source.getAtlasLocation());
                    IVertexBuilder builder = event.getBuffers().getBuffer(RenderType.getCutout());
                    Matrix4f matrix = matrixStack.getLast().getMatrix();
                    builder.pos(matrix, 0.0F, 16.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).tex(sprite.getMinU(), sprite.getMaxV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
                    builder.pos(matrix, 16.0F, 16.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).tex(sprite.getMaxU(), sprite.getMaxV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
                    builder.pos(matrix, 16.0F, 0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).tex(sprite.getMaxU(), sprite.getMinV()).lightmap(0, 240).normal(1, 0, 0).endVertex();
                    builder.pos(matrix, 0.0F, 0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).tex(sprite.getMinU(), sprite.getMinV()).lightmap(0, 240).normal(1, 0, 0).endVertex();

                    matrixStack.pop();
                    shiftX += 16.0D * scale;
                }
            }
        }
    }
}
