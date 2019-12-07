package com.verdantartifice.primalmagic.common.spells.packages;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISpellPackage extends INBTSerializable<CompoundNBT> {
    @Nonnull
    public UUID getSpellUUID();

    public void setSpellUUID(@Nonnull UUID spellUuid);

    @Nullable
    public String getName();

    public void setName(@Nullable String name);
    
    @Nullable
    public ISpellPayload getPayload();
    
    public void setPayload(@Nullable ISpellPayload payload);
    
    @Nullable
    public ISpellMod getPrimaryMod();
    
    public void setPrimaryMod(@Nullable ISpellMod mod);
    
    @Nullable
    public ISpellMod getSecondaryMod();
    
    public void setSecondaryMod(@Nullable ISpellMod mod);

    public int getCooldownTicks();

    @Nonnull
    public SourceList getManaCost();
    
    public void cast(@Nullable World world, @Nullable PlayerEntity caster);
}
