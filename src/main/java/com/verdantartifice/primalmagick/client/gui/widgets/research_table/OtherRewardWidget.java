package com.verdantartifice.primalmagick.client.gui.widgets.research_table;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.verdantartifice.primalmagick.client.util.GuiUtils;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.AbstractReward;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

/**
 * Display widget for the list of non-theory project rewards.  Used on the research table screen.
 * 
 * @author Daedalus4096
 */
public class OtherRewardWidget extends AbstractWidget {
    protected static final ItemStack ICON_STACK = new ItemStack(Items.CHEST);
    protected static final Component HEADER = Component.translatable("label.primalmagick.research_table.reward.header");
    
    public OtherRewardWidget(List<AbstractReward> rewards, int x, int y) {
        super(x, y, 16, 16, Component.empty());
        List<Component> lines = new ArrayList<>();
        lines.add(HEADER);
        rewards.stream().map(AbstractReward::getDescription).forEach(lines::add);
        this.setTooltip(Tooltip.create(CommonComponents.joinLines(lines)));
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        double ticks = (double)mc.level.getLevelData().getGameTime() + (double)pPartialTick;
        float scale = 1F + (0.1F * (float)Math.sin(ticks / 2D));
        GuiUtils.renderItemStack(pGuiGraphics, ICON_STACK, this.getX(), this.getY(), this.getMessage().getString(), true, Optional.of(new Vec3(scale, scale, 1)));
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        // Disable click behavior
        return false;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }
}
