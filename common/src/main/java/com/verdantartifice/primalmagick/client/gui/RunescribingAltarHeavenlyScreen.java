package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.menus.RunescribingAltarHeavenlyMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for heavenly runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarHeavenlyScreen extends AbstractRunescribingAltarScreen<RunescribingAltarHeavenlyMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/runescribing_altar_9.png");

    public RunescribingAltarHeavenlyScreen(RunescribingAltarHeavenlyMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }
    
    @Override
    protected ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
