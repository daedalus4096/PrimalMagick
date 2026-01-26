package com.verdantartifice.primalmagick.client.gui;

import com.verdantartifice.primalmagick.common.menus.RunescribingAltarEnchantedMenu;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * GUI screen for enchanted runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarEnchantedScreen extends AbstractRunescribingAltarScreen<RunescribingAltarEnchantedMenu> {
    protected static final Identifier TEXTURE = ResourceUtils.loc("textures/gui/runescribing_altar_5.png");

    public RunescribingAltarEnchantedScreen(RunescribingAltarEnchantedMenu screenMenu, Inventory inv, Component titleIn) {
        super(screenMenu, inv, titleIn);
    }
    
    @Override
    @NotNull
    protected Identifier getTextureLocation() {
        return TEXTURE;
    }
}
