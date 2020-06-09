package com.verdantartifice.primalmagic.common.tiles.crafting;

import com.verdantartifice.primalmagic.common.blocks.crafting.RunescribingAltarBlock;
import com.verdantartifice.primalmagic.common.containers.RunescribingAltarContainer;
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
    public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
        if (this.getBlockState().getBlock() instanceof RunescribingAltarBlock) {
            int maxRunes = ((RunescribingAltarBlock)this.getBlockState().getBlock()).getRunesAllowed();
            return new RunescribingAltarContainer(windowId, playerInv, this, maxRunes);
        } else {
            return null;
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getBlockState().getBlock().getTranslationKey());
    }
}
