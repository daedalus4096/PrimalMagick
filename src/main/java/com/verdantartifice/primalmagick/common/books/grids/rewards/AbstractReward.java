package com.verdantartifice.primalmagick.common.books.grids.rewards;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Base class for a reward provided by a linguistics grid node upon successful unlock.
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
