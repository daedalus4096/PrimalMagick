package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.crafting.IHasExpertise;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

public class RecipeExpertiseWidget extends AbstractWidget {
    protected static final ResourceLocation ICON_LOC = ResourceUtils.loc("textures/research/expertise_expert.png");
    
    protected final RecipeHolder<?> recipeHolder;
    
    public RecipeExpertiseWidget(RecipeHolder<?> recipeHolder, int x, int y) {
        super(x, y, 16, 16, Component.empty());
        this.recipeHolder = recipeHolder;
        this.setTooltip(Tooltip.create(makeTooltipComponent(recipeHolder)));
    }
    
    protected static Component makeTooltipComponent(RecipeHolder<?> recipeHolder) {
        Minecraft mc = Minecraft.getInstance();
        MutableComponent retVal = Component.empty();
        
        if (recipeHolder.value() instanceof IHasExpertise expRecipe) {
            // Render the expertise group description line, if applicable
            expRecipe.getExpertiseGroupDescription().ifPresent(groupDesc -> {
                retVal.append(Component.translatable("tooltip.primalmagick.expertise.group", groupDesc));
                retVal.append(CommonComponents.NEW_LINE);
            });
            
            // Render the base expertise reward line
            retVal.append(Component.translatable("tooltip.primalmagick.expertise.base", expRecipe.getExpertiseReward(mc.level.registryAccess())));
            retVal.append(CommonComponents.NEW_LINE);
            
            // Render the bonus expertise reward line
            MutableComponent bonusLine = Component.translatable("tooltip.primalmagick.expertise.bonus", expRecipe.getBonusExpertiseReward(mc.level.registryAccess()));
            if (ExpertiseManager.isBonusEligible(mc.player, recipeHolder)) {
                retVal.append(bonusLine);
            } else {
                retVal.append(bonusLine.withStyle(ChatFormatting.GRAY, ChatFormatting.STRIKETHROUGH));
                retVal.append(Component.translatable("tooltip.primalmagick.expertise.claimed"));
            }
        }
        
        return retVal;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Render the icon
        pGuiGraphics.pose().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        pGuiGraphics.pose().translate(this.getX(), this.getY(), 0.0F);
        pGuiGraphics.pose().scale(0.0625F, 0.0625F, 0.0625F);
        pGuiGraphics.blit(ICON_LOC, 0, 0, 0, 0, 255, 255);
        pGuiGraphics.pose().popPose();

        // Don't allow the widget to become focused, to prevent keyboard navigation from moving the active tooltip
        this.setFocused(false);
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
