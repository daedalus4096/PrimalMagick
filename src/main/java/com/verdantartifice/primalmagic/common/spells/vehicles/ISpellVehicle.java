package com.verdantartifice.primalmagic.common.spells.vehicles;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

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
    
    public int getBaseManaCostModifier();
    
    public List<SpellProperty> getProperties();
    
    @Nullable
    public SpellProperty getProperty(String name);
    
    public int getPropertyValue(String name);
}
