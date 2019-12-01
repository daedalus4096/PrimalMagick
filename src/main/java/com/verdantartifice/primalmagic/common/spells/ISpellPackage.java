package com.verdantartifice.primalmagic.common.spells;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISpellPackage extends INBTSerializable<CompoundNBT> {
    @Nonnull
    public UUID getSpellUUID();

    public void setSpellUUID(@Nonnull UUID spellUuid);

    @Nullable
    public String getName();

    public void setName(@Nullable String name);

    public int getCooldownTicks();

    @Nonnull
    public SourceList getManaCost();
}
