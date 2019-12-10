package com.verdantartifice.primalmagic.common.spells.mods;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISpellMod extends INBTSerializable<CompoundNBT> {
    public boolean isActive();
    
    public List<SpellProperty> getProperties();
    
    @Nullable
    public SpellProperty getProperty(String name);
    
    public int getPropertyValue(String name);
    
    @Nonnull
    public ITextComponent getTypeName();
    
    @Nonnull
    public ITextComponent getDefaultNamePiece();
}
