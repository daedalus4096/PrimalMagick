package com.verdantartifice.primalmagick.common.rituals;

import com.verdantartifice.primalmagick.common.rituals.steps.AbstractRitualStep;

import net.minecraft.nbt.CompoundTag;

/**
 * Class identifying a single step in a ritual's process, one controlled by the ritual's recipe.
 * 
 * @author Daedalus4096
 */
public class RecipeRitualStep extends AbstractRitualStep {
    protected int index;

    public RecipeRitualStep() {
        super();
        this.index = -1;
    }
    
    public RecipeRitualStep(RitualStepType type, int index) {
        super(type);
        this.index = index;
    }
    
    @Override
    public boolean isValid() {
        return super.isValid() && (this.type == RitualStepType.OFFERING || this.type == RitualStepType.PROP) && this.index >= 0;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = super.serializeNBT();
        retVal.putInt("Index", this.index);
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.index = nbt.getInt("Index");
    }
}
