package com.verdantartifice.primalmagic.common.tiles.mana;

import com.verdantartifice.primalmagic.common.tiles.TileEntityTypesPM;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;

public class AncientManaFontTileEntity extends TilePM implements ITickableTileEntity {
    protected static final int MANA_CAPACITY = 100;
    protected static final int RECHARGE_TICKS = 20;
    
    protected int ticksExisted = 0;
    protected int mana = 0;
    
    public AncientManaFontTileEntity() {
        super(TileEntityTypesPM.ANCIENT_MANA_FONT);
    }
    
    @Override
    protected void readFromTileNBT(CompoundNBT compound) {
        this.mana = compound.getShort("mana");
    }
    
    @Override
    protected CompoundNBT writeToTileNBT(CompoundNBT compound) {
        compound.putShort("mana", (short)this.mana);
        return compound;
    }
    
    public int getMana() {
        return this.mana;
    }
    
    public int getManaCapacity() {
        return MANA_CAPACITY;
    }

    @Override
    public void tick() {
        this.ticksExisted++;
        if (!this.world.isRemote && this.ticksExisted % RECHARGE_TICKS == 0) {
            this.mana++;
            if (this.mana > MANA_CAPACITY) {
                this.mana = MANA_CAPACITY;
            } else {
                this.markDirty();
                this.syncTile(false);
            }
        }
    }
}
