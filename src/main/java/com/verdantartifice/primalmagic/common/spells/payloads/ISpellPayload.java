package com.verdantartifice.primalmagic.common.spells.payloads;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISpellPayload extends INBTSerializable<CompoundNBT> {
    public void execute(@Nullable RayTraceResult target, @Nullable Vec3d burstPoint, @Nonnull SpellPackage spell, @Nonnull World world, @Nonnull LivingEntity caster);
    
    public boolean isActive();
    
    @Nonnull
    public Source getSource();
    
    @Nonnull
    public int getBaseManaCost();
    
    public void playSounds(@Nonnull World world, @Nonnull BlockPos origin);
    
    @Nonnull
    public List<SpellProperty> getProperties();
    
    @Nullable
    public SpellProperty getProperty(String name);
    
    public int getPropertyValue(String name);
    
    @Nonnull
    public ITextComponent getTypeName();
    
    @Nonnull
    public ITextComponent getDefaultNamePiece();
}
