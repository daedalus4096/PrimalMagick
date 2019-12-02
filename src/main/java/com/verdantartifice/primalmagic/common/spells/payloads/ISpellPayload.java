package com.verdantartifice.primalmagic.common.spells.payloads;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.ISpellPackage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISpellPayload extends INBTSerializable<CompoundNBT> {
    public void execute(@Nullable RayTraceResult target, @Nonnull ISpellPackage spell, @Nonnull World world, @Nonnull PlayerEntity caster);
    
    @Nonnull
    public Source getSource();
    
    @Nonnull
    public SourceList getManaCost();
    
    public void playSounds(@Nonnull World world, @Nonnull PlayerEntity caster);
}
