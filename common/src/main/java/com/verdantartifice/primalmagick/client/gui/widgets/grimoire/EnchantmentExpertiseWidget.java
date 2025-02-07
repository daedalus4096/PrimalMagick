package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.stats.ExpertiseManager;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentExpertiseWidget extends AbstractWidget {
    protected static final ResourceLocation ICON_LOC = ResourceUtils.loc("textures/research/expertise_expert.png");
    
    protected final Holder<Enchantment> enchantment;
    
    public EnchantmentExpertiseWidget(Holder<Enchantment> ench, int x, int y) {
        super(x, y, 16, 16, Component.empty());
        this.enchantment = ench;
        this.setTooltip(Tooltip.create(makeTooltipComponent(ench)));
    }
    
    protected static Component makeTooltipComponent(Holder<Enchantment> ench) {
        Minecraft mc = Minecraft.getInstance();
        MutableComponent retVal = Component.empty();
        
        RuneManager.getRuneDefinition(mc.level.registryAccess(), ench).ifPresent(runeEnchDef -> {
            ExpertiseManager.getRuneEnchantmentTier(mc.level.registryAccess(), runeEnchDef).ifPresent(tier -> {
                // Render the base expertise reward line
                retVal.append(Component.translatable("tooltip.primalmagick.expertise.base", tier.getDefaultExpertise()));
                retVal.append(CommonComponents.NEW_LINE);
                
                // Render the bonus expertise reward line
                MutableComponent bonusLine = Component.translatable("tooltip.primalmagick.expertise.bonus", tier.getDefaultBonusExpertise());
                if (ExpertiseManager.isBonusEligible(mc.player, ench)) {
                    retVal.append(bonusLine);
                } else {
                    retVal.append(bonusLine.withStyle(ChatFormatting.GRAY, ChatFormatting.STRIKETHROUGH));
                    retVal.append(Component.translatable("tooltip.primalmagick.expertise.claimed"));
                }
            });
        });

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
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        // Disable click behavior
        return false;
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
    }
}
