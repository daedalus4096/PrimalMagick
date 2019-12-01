package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.nbt.CompoundNBT;

public class EarthDamageSpellPayload extends AbstractDamageSpellPayload {
    protected static final String TYPE = "earth_damage";
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putString("PayloadType", TYPE);
        return nbt;
    }
    
    @Override
    public Source getSource() {
        return Source.EARTH;
    }
}
