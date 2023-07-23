package com.verdantartifice.primalmagick.client.gui.radial;

import java.util.Collections;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ImageRadialMenuItem extends TextRadialMenuItem {
    private final ResourceLocation imageLoc;
    private final int slot;

    public ImageRadialMenuItem(GenericRadialMenu owner, int slot, ResourceLocation imageLoc, Component altText) {
        super(owner, altText, 0x7FFFFFFF);
        this.imageLoc = imageLoc;
        this.slot = slot;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public ResourceLocation getImageLoc() {
        return this.imageLoc;
    }
    
    @Override
    public void draw(DrawingContext context) {
        if (this.imageLoc == null) {
            super.draw(context);
        } else {
            context.guiGraphics.pose().pushPose();
            context.guiGraphics.pose().translate(-8, -8, context.z + 200);
            context.guiGraphics.pose().scale(0.5F, 0.5F, 1F);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            context.guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            context.guiGraphics.blit(this.imageLoc, 2 * (int)context.x, 2 * (int)context.y, 0, 0, 32, 32, 32, 32);
            context.guiGraphics.pose().popPose();
        }
    }

    @Override
    public void drawTooltips(DrawingContext context) {
        if (this.getText() == null) {
            super.drawTooltips(context);
        } else {
            context.drawingHelper.renderTooltip(context.guiGraphics, Collections.singletonList(this.getText()), (int)context.x, (int)context.y);
        }
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
