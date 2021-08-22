package com.verdantartifice.primalmagic.common.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

/**
 * Class identifying a single step in a ritual's process, one targeting a nearby universal prop.
 * 
 * @author Daedalus4096
 */
public class UniversalRitualStep extends AbstractRitualStep {
    protected BlockPos pos;
    
    public UniversalRitualStep() {
        super();
        this.pos = BlockPos.ZERO;
    }
    
    public UniversalRitualStep(BlockPos pos) {
        super(RitualStepType.UNIVERSAL_PROP);
        this.pos = pos;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && this.pos != null;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = super.serializeNBT();
        retVal.putInt("PosX", this.pos.getX());
        retVal.putInt("PosY", this.pos.getY());
        retVal.putInt("PosZ", this.pos.getZ());
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.pos = new BlockPos(nbt.getInt("PosX"), nbt.getInt("PosY"), nbt.getInt("PosZ"));
    }
}
