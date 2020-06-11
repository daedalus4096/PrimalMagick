package com.verdantartifice.primalmagic.client.gui;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.containers.RunescribingAltarBasicContainer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * GUI screen for basic runescribing altar blocks.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class RunescribingAltarBasicScreen extends AbstractRunescribingAltarScreen<RunescribingAltarBasicContainer> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(PrimalMagic.MODID, "textures/gui/runescribing_altar_3.png");

    public RunescribingAltarBasicScreen(RunescribingAltarBasicContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }
    
    @Override
    protected ResourceLocation getTextureLocation() {
        return TEXTURE;
    }
}
