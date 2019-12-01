package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.spells.ISpellPackage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.RayTraceResult;

public abstract class AbstractDamageSpellPayload extends AbstractSpellPayload {
    protected int power;
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putInt("Power", this.power);
        return nbt;
    }
    
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.power = nbt.getInt("Power");
    }

    @Override
    public boolean execute(RayTraceResult target, ISpellPackage spell) {
        // TODO Auto-generated method stub
        return false;
    }

}
