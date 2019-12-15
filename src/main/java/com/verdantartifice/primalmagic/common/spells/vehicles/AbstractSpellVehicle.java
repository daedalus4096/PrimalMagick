package com.verdantartifice.primalmagic.common.spells.vehicles;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class AbstractSpellVehicle implements ISpellVehicle {
    protected abstract String getPackageType();

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("VehicleType", this.getPackageType());
        return nbt;
    }
    
    @Override
    public void deserializeNBT(CompoundNBT nbt) {}
    
    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public ITextComponent getTypeName() {
        return new TranslationTextComponent("primalmagic.spell.vehicle.type." + this.getPackageType());
    }
    
    @Override
    public ITextComponent getDefaultNamePiece() {
        return new TranslationTextComponent("primalmagic.spell.vehicle.default_name." + this.getPackageType());
    }
}
