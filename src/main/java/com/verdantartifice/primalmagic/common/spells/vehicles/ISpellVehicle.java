package com.verdantartifice.primalmagic.common.spells.vehicles;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISpellVehicle extends INBTSerializable<CompoundNBT> {
    public void execute(@Nonnull SpellPackage spell, @Nonnull World world, @Nonnull PlayerEntity caster);

    public boolean isActive();
    
    @Nonnull
    public ITextComponent getTypeName();
    
    @Nonnull
    public ITextComponent getDefaultNamePiece();
    
    @Nonnull
    public SourceList modifyManaCost(@Nonnull SourceList cost);
}
