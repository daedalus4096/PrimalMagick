package com.verdantartifice.primalmagic.common.spells.payloads;

import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractSpellPayload implements ISpellPayload {
    protected abstract String getPayloadType();
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("PayloadType", this.getPayloadType());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {}
}
