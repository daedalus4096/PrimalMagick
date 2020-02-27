package com.verdantartifice.primalmagic.common.theorycrafting;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class ProjectMaterial implements INBTSerializable<CompoundNBT> {
    protected final ItemStack stack;
    protected final boolean isConsumed;
    protected final boolean isObservation;
    
    protected boolean isSelected;
    
    protected ProjectMaterial(@Nonnull ItemStack stack, boolean isConsumed, boolean isObservation) {
        this.stack = stack.copy();
        this.isConsumed = isConsumed;
        this.isObservation = isObservation;
        this.isSelected = false;
    }
    
    public static ProjectMaterial createItemMaterial(@Nonnull ItemStack stack, boolean isConsumed) {
        return new ProjectMaterial(stack, isConsumed, false);
    }
    
    public static ProjectMaterial createObservationMaterial() {
        return new ProjectMaterial(ItemStack.EMPTY, true, true);
    }

    @Override
    public CompoundNBT serializeNBT() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        // TODO Auto-generated method stub
        
    }
    
    public ResourceLocation getImage() {
        // TODO stub
        return null;
    }
    
    public boolean isSatisfied(PlayerEntity player) {
        // TODO stub
        return true;
    }
    
    public void consume(PlayerEntity player) {
        // TODO stub
    }
    
    public boolean isSelected() {
        return this.isSelected;
    }
    
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}
