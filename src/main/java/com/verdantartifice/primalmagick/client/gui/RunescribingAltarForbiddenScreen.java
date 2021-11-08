package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.containers.RunescribingAltarForbiddenContainer;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for forbidden runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarForbiddenScreen extends AbstractRunescribingAltarScreen<RunescribingAltarForbiddenContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/runescribing_altar_7.png");

    public RunescribingAltarForbiddenScreen(RunescribingAltarForbiddenContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    protected ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
