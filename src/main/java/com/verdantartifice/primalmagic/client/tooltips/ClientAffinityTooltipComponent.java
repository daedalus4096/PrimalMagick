package com.verdantartifice.primalmagic.client.tooltips;

import com.mojang.blaze3d.vertex.PoseStack;
import com.verdantartifice.primalmagic.common.affinities.AffinityTooltipComponent;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureManager;

/**
 * Tooltip component renderer for an item's affinities.
 * 
 * @author Daedalus4096
 */
public class ClientAffinityTooltipComponent implements ClientTooltipComponent {
    protected final SourceList affinities;
    
    public ClientAffinityTooltipComponent(AffinityTooltipComponent component) {
        this.affinities = component.getAffinities().copy();
    }

    @Override
    public int getHeight() {
        return 18;
    }

    @Override
    public int getWidth(Font font) {
        return 18 * this.affinities.getSources().size();
    }

    @Override
    public void renderImage(Font font, int x, int y, PoseStack poseStack, ItemRenderer itemRenderer, int z, TextureManager textureManager) {
        // TODO Auto-generated method stub
    }
}
