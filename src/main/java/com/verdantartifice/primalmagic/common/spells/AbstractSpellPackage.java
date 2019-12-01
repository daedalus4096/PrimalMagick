package com.verdantartifice.primalmagic.common.spells;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractSpellPackage implements ISpellPackage {
    protected UUID spellUUID;
    protected String name;
    
    public AbstractSpellPackage() {
        this.spellUUID = UUID.randomUUID();
    }
    
    public AbstractSpellPackage(@Nullable String name) {
        super();
        this.name = name;
    }
    
    @Override
    @Nonnull
    public UUID getSpellUUID() {
        return this.spellUUID;
    }
    
    @Override
    public void setSpellUUID(@Nonnull UUID spellUuid) {
        this.spellUUID = spellUuid;
    }
    
    @Override
    @Nullable
    public String getName() {
        return this.name;
    }
    
    @Override
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
    
    @Override
    public int getCooldownTicks() {
        // TODO Calculate actual value
        return 40;
    }
    
    @Override
    @Nonnull
    public SourceList getManaCost() {
        // TODO Calculate actual cost
        return new SourceList().add(Source.EARTH, 5);
    }
}
