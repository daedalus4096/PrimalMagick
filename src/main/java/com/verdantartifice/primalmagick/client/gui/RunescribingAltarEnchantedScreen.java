package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.common.containers.RunescribingAltarEnchantedContainer;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for enchanted runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarEnchantedScreen extends AbstractRunescribingAltarScreen<RunescribingAltarEnchantedContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/runescribing_altar_5.png");

    public RunescribingAltarEnchantedScreen(RunescribingAltarEnchantedContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    protected ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
