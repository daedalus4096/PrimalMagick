package com.verdantartifice.primalmagic.common.spells.payloads;

import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.nbt.CompoundNBT;

public class EarthDamageSpellPayload extends AbstractDamageSpellPayload {
    public static final String TYPE = "earth_damage";
    
    public EarthDamageSpellPayload() {
        super();
    }
    
    public EarthDamageSpellPayload(int power) {
        super(power);
    }
    
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
