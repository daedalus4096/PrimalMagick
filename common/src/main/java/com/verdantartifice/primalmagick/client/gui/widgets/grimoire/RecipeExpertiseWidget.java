package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.common.crafting.IHasExpertise;
import com.verdantartifice.primalmagick.common.crafting.display.ExpertiseRecipeDisplay;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

public class RecipeExpertiseWidget extends AbstractWidget {
    protected static final Identifier ICON_LOC = ResourceUtils.loc("research/expertise_expert");
    
    public RecipeExpertiseWidget(@NotNull ExpertiseRecipeDisplay display, @NotNull ResourceKey<Recipe<?>> recipeKey, int x, int y) {
        super(x, y, 16, 16, Component.empty());
        this.setTooltip(Tooltip.create(makeTooltipComponent(display, recipeKey)));
    }
    
    protected static Component makeTooltipComponent(ExpertiseRecipeDisplay display, ResourceKey<Recipe<?>> recipeKey) {
        Minecraft mc = Minecraft.getInstance();
        MutableComponent retVal = Component.empty();
        
        if (mc.level != null) {
            // Render the expertise group description line, if applicable
            display.groupOpt().map(loc -> Component.translatable(Util.makeDescriptionId("expertise_group", loc))).ifPresent(groupDesc -> {
                retVal.append(Component.translatable("tooltip.primalmagick.expertise.group", groupDesc));
                retVal.append(CommonComponents.NEW_LINE);
            });
            
            // Render the base expertise reward line
            retVal.append(Component.translatable("tooltip.primalmagick.expertise.base", display.baseValue()));
            retVal.append(CommonComponents.NEW_LINE);
            
            // Render the bonus expertise reward line
            MutableComponent bonusLine = Component.translatable("tooltip.primalmagick.expertise.bonus", display.bonusValue());
            if (display.isBonusEligible(mc.player, recipeKey)) {
                retVal.append(bonusLine);
            } else {
                retVal.append(bonusLine.withStyle(ChatFormatting.GRAY, ChatFormatting.STRIKETHROUGH));
                retVal.append(Component.translatable("tooltip.primalmagick.expertise.claimed"));
            }
        }
        
        return retVal;
    }

    @Override
    public void extractWidgetRenderState(@NotNull GuiGraphicsExtractor pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Render the icon
        pGuiGraphics.pose().pushMatrix();
        pGuiGraphics.pose().translate(this.getX(), this.getY());
        pGuiGraphics.pose().scale(0.0625F, 0.0625F);
        pGuiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ICON_LOC, 0, 0, 32, 32);
        pGuiGraphics.pose().popMatrix();

        // Don't allow the widget to become focused, to prevent keyboard navigation from moving the active tooltip
        this.setFocused(false);
    }

    @Override
    public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(@NotNull NarrationElementOutput output) {
    }
}
