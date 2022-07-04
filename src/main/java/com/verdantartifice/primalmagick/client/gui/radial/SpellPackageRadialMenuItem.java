package com.verdantartifice.primalmagick.client.gui.radial;

import java.util.Collections;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;

public class SpellPackageRadialMenuItem extends TextRadialMenuItem {
    private final SpellPackage spellPackage;
    private final int slot;
    
    public SpellPackageRadialMenuItem(GenericRadialMenu owner, int slot, SpellPackage spell, Component altText) {
        super(owner, altText, 0x7FFFFFFF);
        this.slot = slot;
        this.spellPackage = spell;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public SpellPackage getSpellPackage() {
        return this.spellPackage;
    }
    
    @Override
    public void draw(DrawingContext context) {
        if (this.spellPackage == null) {
            super.draw(context);
        } else {
            PoseStack newStack = new PoseStack();
            newStack.pushPose();
            newStack.mulPoseMatrix(context.matrixStack.last().pose());
            newStack.translate(-8, -8, context.z + 200);
            newStack.scale(0.5F, 0.5F, 1F);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, this.spellPackage.getIcon());
            GuiComponent.blit(newStack, 2 * (int)context.x, 2 * (int)context.y, 0, 0, 32, 32, 32, 32);
            newStack.popPose();
        }
    }

    @Override
    public void drawTooltips(DrawingContext context) {
        if (this.spellPackage == null) {
            super.drawTooltips(context);
        } else {
            context.drawingHelper.renderTooltip(context.matrixStack, Collections.singletonList(this.spellPackage.getName()), (int)context.x, (int)context.y);
        }
    }
}
