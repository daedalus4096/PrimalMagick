package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Display widget for showing attunement threshold bonuses.
 * 
 * @author Daedalus4096
 */
public class AttunementThresholdWidget extends AbstractWidget {
    protected static final ItemStack WAND_STACK = Util.make(new ItemStack(ItemsPM.MODULAR_WAND.get()), stack -> {
        ItemsPM.MODULAR_WAND.get().setWandCore(stack, WandCore.HEARTWOOD);
        ItemsPM.MODULAR_WAND.get().setWandCap(stack, WandCap.IRON);
        ItemsPM.MODULAR_WAND.get().setWandGem(stack, WandGem.APPRENTICE);
    });
    protected static final ItemStack SHACKLED_OVERLAY_STACK = new ItemStack(Items.BARRIER);
    
    protected final Source source;
    protected final AttunementThreshold threshold;
    protected final boolean suppressed;
    protected final ResourceLocation texture;
    
    public AttunementThresholdWidget(@Nonnull Source source, @Nonnull AttunementThreshold threshold, int x, int y) {
        super(x, y, 18, 18, Component.empty());
        this.source = source;
        this.threshold = threshold;
        Minecraft mc = Minecraft.getInstance();
        this.suppressed = AttunementManager.isSuppressed(mc.player, source);
        this.texture = PrimalMagick.resource("textures/attunements/threshold_" + source.getTag() + "_" + threshold.getTag() + ".png");
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable(String.join(".", "attunement_threshold", PrimalMagick.MODID, threshold.getTag(), source.getTag())));
        if (this.suppressed) {
            lines.add(Component.translatable("tooltip.primalmagick.attunement_shackles.shackled").withStyle(ChatFormatting.RED));
        }
        this.setTooltip(Tooltip.create(CommonComponents.joinLines(lines)));
    }
    
    @Override
    public void renderWidget(GuiGraphics guiGraphics, int p_renderButton_1_, int p_renderButton_2_, float p_renderButton_3_) {
        if (this.threshold == AttunementThreshold.MINOR) {
            // Render casting wand into GUI
            guiGraphics.renderItem(WAND_STACK, this.getX() + 1, this.getY() + 1);
        } else {
            // Render the icon appropriate for this widget's source and threshold
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
            guiGraphics.pose().scale(0.0703125F, 0.0703125F, 0.0703125F);
            guiGraphics.blit(this.texture, 0, 0, 0, 0, 255, 255);
            guiGraphics.pose().popPose();
        }
        if (this.suppressed) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.getX() + 1, this.getY() + 1, 1.0F);
            guiGraphics.renderItem(SHACKLED_OVERLAY_STACK, 0, 0);
            guiGraphics.pose().popPose();
        }
    }
    
    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
