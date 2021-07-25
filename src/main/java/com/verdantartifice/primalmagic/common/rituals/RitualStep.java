package com.verdantartifice.primalmagic.common.rituals;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Class identifying a single step in a ritual's process.
 * 
 * @author Daedalus4096
 */
public class RitualStep implements INBTSerializable<CompoundTag> {
    protected RitualStepType type;
    protected int index;
    
    public RitualStep() {
        this.type = null;
        this.index = -1;
    }
    
    public RitualStep(RitualStepType type, int index) {
        this.type = type;
        this.index = index;
    }
    
    public boolean isValid() {
        return this.type != null && this.index >= 0;
    }
    
    public RitualStepType getType() {
        return this.type;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = new CompoundTag();
        retVal.putString("Type", this.type.getSerializedName());
        retVal.putInt("Index", this.index);
        return retVal;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.type = RitualStepType.fromName(nbt.getString("Type"));
        this.index = nbt.getInt("Index");
    }
}
