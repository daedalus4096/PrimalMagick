package com.verdantartifice.primalmagic.common.theorycrafting;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class AbstractProject implements INBTSerializable<CompoundNBT> {
    public boolean initialize(PlayerEntity player) {
        return true;
    }
    
    public abstract String getNameTranslationKey();
    
    public abstract String getTextTranslationKey();
    
    public abstract List<ProjectMaterial> getMaterials();
    
    protected double getBaseSuccessChance(PlayerEntity player) {
        // TODO get projects completed from stats and calculate based on that
        return 0.0D;
    }
    
    protected double getSuccessChancePerMaterial(PlayerEntity player) {
        // TODO get projects completed from stats and calculate based on that
        return 20.0D;
    }
    
    public double getSuccessChance(PlayerEntity player) {
        double chance = this.getBaseSuccessChance(player);
        double per = this.getSuccessChancePerMaterial(player);
        for (ProjectMaterial material : this.getMaterials()) {
            if (material.isSelected()) {
                chance += per;
            }
        }
        return MathHelper.clamp(chance, 0.0D, 100.0D);
    }
}
