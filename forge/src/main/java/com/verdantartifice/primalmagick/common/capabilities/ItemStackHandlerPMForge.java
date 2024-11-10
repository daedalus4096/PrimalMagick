package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Extension of the default IItemHandler implementation which updates its owning tile's client
 * side when the contents of the capability change.
 * 
 * @author Daedalus4096
 */
public class ItemStackHandlerPMForge extends ItemStackHandler implements IItemHandlerPM {
    protected final AbstractTilePM tile;
    
    public ItemStackHandlerPMForge(AbstractTilePM tile) {
        super();
        this.tile = tile;
    }
    
    public ItemStackHandlerPMForge(int size, AbstractTilePM tile) {
        super(size);
        this.tile = tile;
    }
    
    public ItemStackHandlerPMForge(NonNullList<ItemStack> stacks, AbstractTilePM tile) {
        super(stacks);
        this.tile = tile;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        if (this.tile != null) {
            this.tile.syncTile(true);
            this.tile.setChanged();
        }
    }
}
