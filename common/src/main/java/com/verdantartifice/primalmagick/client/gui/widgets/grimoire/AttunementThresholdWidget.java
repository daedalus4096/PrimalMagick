package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.wands.WandCap;
import com.verdantartifice.primalmagick.common.wands.WandCore;
import com.verdantartifice.primalmagick.common.wands.WandGem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

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
    protected final Identifier texture;
    
    public AttunementThresholdWidget(@Nonnull Source source, @Nonnull AttunementThreshold threshold, int x, int y) {
        super(x, y, 18, 18, Component.empty());
        this.source = source;
        this.threshold = threshold;
        Minecraft mc = Minecraft.getInstance();
        this.suppressed = AttunementManager.isSuppressed(mc.player, source);
        this.texture = ResourceUtils.loc("textures/attunements/threshold_" + source.getId().getPath() + "_" + threshold.getTag() + ".png");
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable(String.join(".", "attunement_threshold", Constants.MOD_ID, threshold.getTag(), source.getId().getPath())));
        if (this.suppressed) {
            lines.add(Component.translatable("tooltip.primalmagick.attunement_shackles.shackled").withStyle(ChatFormatting.RED));
        }
        this.setTooltip(Tooltip.create(CommonComponents.joinLines(lines)));
    }
    
    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.threshold == AttunementThreshold.MINOR) {
            // Render casting wand into GUI
            pGuiGraphics.renderItem(WAND_STACK, this.getX() + 1, this.getY() + 1);
        } else {
            // Render the icon appropriate for this widget's source and threshold
            pGuiGraphics.pose().pushMatrix();
            pGuiGraphics.pose().translate(this.getX(), this.getY());
            pGuiGraphics.pose().scale(0.0703125F, 0.0703125F);
            pGuiGraphics.blit(this.texture, 0, 0, 0, 0, 255, 255);
            pGuiGraphics.pose().popMatrix();
        }
        if (this.suppressed) {
            pGuiGraphics.pose().pushMatrix();
            pGuiGraphics.pose().translate(this.getX() + 1, this.getY() + 1);
            pGuiGraphics.renderItem(SHACKLED_OVERLAY_STACK, 0, 0);
            pGuiGraphics.pose().popMatrix();
        }
    }
    
    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean doubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
