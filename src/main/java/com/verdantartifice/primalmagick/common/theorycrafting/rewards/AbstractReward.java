package com.verdantartifice.primalmagick.common.theorycrafting.rewards;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Base class for a non-theory reward provided by a theorycrafting research project upon successful completion.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractReward implements IReward, INBTSerializable<CompoundTag> {
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = new CompoundTag();
        retVal.putString("RewardType", this.getRewardType());
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        // Nothing to do in the base class
    }
}
