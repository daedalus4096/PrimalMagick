package com.verdantartifice.primalmagick.client.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.verdantartifice.primalmagick.common.wands.IWand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class SpellSelectionRadialScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final float RADIUS_INNER = 30F;
    private static final int BG_COLOR = 0x3F000000;
    private static final int BG_COLOR_HOVER = 0x3FFFFFFF;
    private static final float ARC_PRECISION = 2.5F / 360F;

    public SpellSelectionRadialScreen() {
        super(Component.empty());
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTick) {
        super.render(matrixStack, mouseX, mouseY, partialTick);
        
        Minecraft mc = Minecraft.getInstance();
        ItemStack stack = mc.player.getMainHandItem();
        if (stack.getItem() instanceof IWand wand) {
            float radiusInner = RADIUS_INNER;
            float radiusOuter = radiusInner * 2;
            float radiusItem = (radiusInner + radiusOuter) * 0.5F;
            int centerX = this.width / 2;
            int centerY = this.height / 2;
            int numSlices = wand.getSpellCount(stack) + 1;
            
            double mouseAngle = Math.toDegrees(Math.atan2(mouseY - centerY, mouseX - centerX));
            double mouseDistance = Math.sqrt(Math.pow(mouseX - centerX, 2) + Math.pow(mouseY - centerY, 2));
            if (mouseAngle < ((-0.5f / (float)numSlices) + 0.25F) * 360) {
                mouseAngle += 360D;
            }
            
            matrixStack.pushPose();
            this.drawBackground(matrixStack, centerX, centerY, radiusInner, radiusOuter, stack, wand);
            matrixStack.popPose();
        }
    }

    private void drawBackground(PoseStack matrixStack, float mouseX, float mouseY, float radiusInner, float radiusOuter, ItemStack wandStack, IWand wand) {
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        
        int numSlices = wand.getSpellCount(wandStack) + 1;
        for (int index = 0; index < numSlices; index++) {
            float startAngle = (float)this.getAngle(index - 0.5D, numSlices);
            float endAngle = (float)this.getAngle(index + 0.5D, numSlices);
            this.drawMenuArc(buffer, mouseX, mouseY, 0, radiusInner, radiusOuter, startAngle, endAngle, BG_COLOR);
        }

        tesselator.end();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
    
    private void drawMenuArc(BufferBuilder buffer, float x, float y, float z, float radiusInner, float radiusOuter, float startAngle, float endAngle, int color) {
        float angle = endAngle - startAngle;
        int sections = Math.max(1, Mth.ceil(angle / ARC_PRECISION));
        float slice = angle / sections;
        
        for (int index = 0; index < sections; index++) {
            float angle1 = startAngle + (index * slice);
            float angle2 = startAngle + ((index + 1) * slice);
            
            buffer.vertex(x + (radiusOuter * Mth.cos(angle1)), y + (radiusOuter * Mth.sin(angle1)), z).color(color).endVertex();
            buffer.vertex(x + (radiusInner * Mth.cos(angle1)), y + (radiusInner * Mth.sin(angle1)), z).color(color).endVertex();
            buffer.vertex(x + (radiusInner * Mth.cos(angle2)), y + (radiusInner * Mth.sin(angle2)), z).color(color).endVertex();
            buffer.vertex(x + (radiusOuter * Mth.cos(angle2)), y + (radiusOuter * Mth.sin(angle2)), z).color(color).endVertex();
        }
    }

    private double getAngle(double index, int numItems) {
        if (numItems == 0) {
            return 0;
        } else {
            return ((index / numItems) + 0.25D) * (2 * Math.PI) + Math.PI;
        }
    }

    @Override
    public void onClose() {
        // TODO Auto-generated method stub
        super.onClose();
        LOGGER.info("Closing spell selection radial screen");
    }
}

/*
 Note: This code was adapted from David Quintana's implementation in Tool Belt.
 Below is the copyright notice.
 
 Copyright (c) 2015, David Quintana <gigaherz@gmail.com>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
     * Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.
     * Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.
     * Neither the name of the author nor the
       names of the contributors may be used to endorse or promote products
       derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
