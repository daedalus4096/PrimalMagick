package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.menus.RunescribingAltarEnchantedMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for enchanted runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarEnchantedScreen extends AbstractRunescribingAltarScreen<RunescribingAltarEnchantedMenu> {
    protected static final ResourceLocation TEXTURE = PrimalMagick.resource("textures/gui/runescribing_altar_5.png");

    public RunescribingAltarEnchantedScreen(RunescribingAltarEnchantedMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }
    
    @Override
    protected ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
