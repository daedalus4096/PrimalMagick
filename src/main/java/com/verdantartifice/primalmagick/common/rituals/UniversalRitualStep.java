package com.verdantartifice.primalmagick.common.rituals;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * Class identifying a single step in a ritual's process, one targeting a nearby universal prop.
 * 
 * @author Daedalus4096
 */
public class UniversalRitualStep extends AbstractRitualStep {
    protected BlockPos pos;
    protected ResourceLocation expectedId;
    
    public UniversalRitualStep() {
        super();
        this.pos = null;
        this.expectedId = null;
    }
    
    public UniversalRitualStep(BlockPos pos, ResourceLocation expectedId) {
        super(RitualStepType.UNIVERSAL_PROP);
        this.pos = pos;
        this.expectedId = expectedId;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && this.pos != null && this.expectedId != null;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public ResourceLocation getExpectedId() {
        return this.expectedId;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = super.serializeNBT();
        retVal.putInt("PosX", this.pos.getX());
        retVal.putInt("PosY", this.pos.getY());
        retVal.putInt("PosZ", this.pos.getZ());
        retVal.putString("ExpectedId", this.expectedId.toString());
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.pos = new BlockPos(nbt.getInt("PosX"), nbt.getInt("PosY"), nbt.getInt("PosZ"));
        this.expectedId = new ResourceLocation(nbt.getString("ExpectedId"));
    }
}
