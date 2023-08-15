package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.menus.RunescribingAltarHeavenlyContainer;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for heavenly runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarHeavenlyScreen extends AbstractRunescribingAltarScreen<RunescribingAltarHeavenlyContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagick.MODID, "textures/gui/runescribing_altar_9.png");

    public RunescribingAltarHeavenlyScreen(RunescribingAltarHeavenlyContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    protected ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
