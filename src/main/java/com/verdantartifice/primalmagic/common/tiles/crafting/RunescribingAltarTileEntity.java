package com.verdantartifice.primalmagic.common.tiles.crafting;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TileInventoryPM;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Definition of a runescribing altar tile entity.
 * 
 * @author Daedalus4096
 */
public class RunescribingAltarTileEntity extends TileInventoryPM implements INamedContainerProvider {
    protected static final int MAX_RUNES = 9;
    
    public RunescribingAltarTileEntity() {
        super(TileEntityTypesPM.RUNESCRIBING_ALTAR.get(), MAX_RUNES + 2);
    }

    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }
}
