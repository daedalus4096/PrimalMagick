package com.verdantartifice.primalmagic.common.tiles.misc;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AnalysisTableTileEntity extends TilePM implements INamedContainerProvider, ITickableTileEntity {
    public int counter = 0;
    
    public AnalysisTableTileEntity() {
        super(TileEntityTypesPM.ANALYSIS_TABLE);
    }
    
    @Override
    public void tick() {
        // TODO Auto-generated method stub
        this.counter++;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        // TODO Auto-generated method stub
        return null;
    }
}
