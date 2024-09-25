package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.menus.RunescribingAltarForbiddenMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for forbidden runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarForbiddenScreen extends AbstractRunescribingAltarScreen<RunescribingAltarForbiddenMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/runescribing_altar_7.png");

    public RunescribingAltarForbiddenScreen(RunescribingAltarForbiddenMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }
    
    @Override
    protected ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
