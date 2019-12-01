package com.verdantartifice.primalmagic.common.spells.payloads;

import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractSpellPayload implements ISpellPayload {
    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {}
}
