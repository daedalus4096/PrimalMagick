package com.verdantartifice.primalmagic.common.rituals;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Class identifying a single step in a ritual's process.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractRitualStep implements INBTSerializable<CompoundTag> {
    protected RitualStepType type;
    
    public AbstractRitualStep() {
        this.type = null;
    }
    
    public AbstractRitualStep(RitualStepType type) {
        this.type = type;
    }
    
    public boolean isValid() {
        return this.type != null;
    }
    
    public RitualStepType getType() {
        return this.type;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = new CompoundTag();
        retVal.putString("Type", this.type.getSerializedName());
        return retVal;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.type = RitualStepType.fromName(nbt.getString("Type"));
    }
}
