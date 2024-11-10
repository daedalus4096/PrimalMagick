package com.verdantartifice.primalmagick.common.capabilities;

import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Extension of the default IItemHandler implementation which updates its owning tile's client
 * side when the contents of the capability change.
 * 
 * @author Daedalus4096
 */
public class ItemStackHandlerPMNeoforge extends ItemStackHandler implements IItemHandlerPM {
    protected final AbstractTilePM tile;

    public ItemStackHandlerPMNeoforge(AbstractTilePM tile) {
        super();
        this.tile = tile;
    }

    public ItemStackHandlerPMNeoforge(int size, AbstractTilePM tile) {
        super(size);
        this.tile = tile;
    }

    public ItemStackHandlerPMNeoforge(NonNullList<ItemStack> stacks, AbstractTilePM tile) {
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
