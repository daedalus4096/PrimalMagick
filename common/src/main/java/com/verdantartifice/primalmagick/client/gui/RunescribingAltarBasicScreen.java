package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.RunescribingAltarBasicMenu;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI screen for basic runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarBasicScreen extends AbstractRunescribingAltarScreen<RunescribingAltarBasicMenu> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/runescribing_altar_3.png");

    public RunescribingAltarBasicScreen(RunescribingAltarBasicMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }
    
    @Override
    @NotNull
    protected Identifier getTextureLocation() {
        return TEXTURE;
    }
}
