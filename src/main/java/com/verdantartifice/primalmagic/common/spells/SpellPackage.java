package com.verdantartifice.primalmagic.common.spells;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class SpellPackage implements INBTSerializable<CompoundNBT> {
    protected UUID spellUUID;
    protected String name;
    
    public SpellPackage() {
        this.spellUUID = UUID.randomUUID();
    }
    
    public SpellPackage(@Nullable String name) {
        super();
        this.name = name;
    }
    
    @Nonnull
    public UUID getSpellUUID() {
        return this.spellUUID;
    }
    
    public void setSpellUUID(@Nonnull UUID spellUuid) {
        this.spellUUID = spellUuid;
    }
    
    @Nullable
    public String getName() {
        return this.name;
    }
    
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        if (this.spellUUID != null) {
            nbt.putUniqueId("SpellUUID", this.spellUUID);
        }
        if (this.name != null) {
            nbt.putString("SpellName", this.name);
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.spellUUID = nbt.getUniqueId("SpellUUID");
        this.name = nbt.getString("SpellName");
    }
}
