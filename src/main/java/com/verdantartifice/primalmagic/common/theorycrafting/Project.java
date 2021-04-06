package com.verdantartifice.primalmagic.common.theorycrafting;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Class representing an initialized theorycrafting project, created from a data-defined template.
 * Theorycrafting projects grant the player progress toward gaining a theory by spending materials,
 * such as items or observations.
 * 
 * @author Daedalus4096
 */
public class Project implements INBTSerializable<CompoundNBT> {
    protected ResourceLocation templateKey;
    protected List<AbstractProjectMaterial> activeMaterials = new ArrayList<>();
    protected double baseSuccessChance;
    protected int rewardPoints;
    protected ResourceLocation aidBlock;
    
    public Project() {}
    
    public Project(@Nonnull ResourceLocation templateKey, @Nonnull List<AbstractProjectMaterial> materials, double baseSuccessChance, int rewardPoints, @Nullable ResourceLocation aidBlock) {
        this.templateKey = templateKey;
        this.activeMaterials = materials;
        this.baseSuccessChance = baseSuccessChance;
        this.rewardPoints = rewardPoints;
        this.aidBlock = aidBlock;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT retVal = new CompoundNBT();
        retVal.putString("TemplateKey", this.templateKey.toString());
        retVal.putDouble("BaseSuccessChance", this.baseSuccessChance);
        retVal.putInt("RewardPoints", this.rewardPoints);
        if (this.aidBlock != null) {
            retVal.putString("AidBlock", this.aidBlock.toString());
        }
        
        ListNBT materialList = new ListNBT();
        for (AbstractProjectMaterial material : this.activeMaterials) {
            materialList.add(material.serializeNBT());
        }
        retVal.put("Materials", materialList);
        
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.templateKey = new ResourceLocation(nbt.getString("TemplateKey"));
        this.baseSuccessChance = nbt.getDouble("BaseSuccessChance");
        this.rewardPoints = nbt.getInt("RewardPoints");
        
        this.aidBlock = null;
        if (nbt.contains("AidBlock")) {
            this.aidBlock = new ResourceLocation(nbt.getString("AidBlock"));
        }
        
        this.activeMaterials.clear();
        ListNBT materialList = nbt.getList("Materials", Constants.NBT.TAG_COMPOUND);
        for (int index = 0; index < materialList.size(); index++) {
            AbstractProjectMaterial material = ProjectFactory.getMaterialFromNBT(materialList.getCompound(index));
            if (material != null) {
                this.activeMaterials.add(material);
            }
        }
    }

    @Nonnull
    public String getNameTranslationKey() {
        return this.templateKey.getNamespace() + ".research_project.name." + this.templateKey.getPath();
    }
    
    @Nonnull
    public String getTextTranslationKey() {
        return this.templateKey.getNamespace() + ".research_project.text." + this.templateKey.getPath();
    }

    @Nonnull
    public List<AbstractProjectMaterial> getMaterials() {
        return this.activeMaterials;
    }
    
    protected double getSuccessChancePerMaterial() {
        // A full complement of materials should always be enough to get the player to 100% success chance
        int materialCount = this.activeMaterials.size();
        if (materialCount <= 0) {
            return 0.0D;
        } else {
            return (1.0D - this.baseSuccessChance) / materialCount;
        }
    }
    
    public double getSuccessChance() {
        double chance = this.baseSuccessChance;
        double per = this.getSuccessChancePerMaterial();
        for (AbstractProjectMaterial material : this.getMaterials()) {
            if (material.isSelected()) {
                chance += per;
            }
        }
        return MathHelper.clamp(chance, 0.0D, 1.0D);
    }
    
    public boolean isSatisfied(PlayerEntity player) {
        // Determine satisfaction from selected materials
        for (AbstractProjectMaterial material : this.getMaterials()) {
            if (material.isSelected() && !material.isSatisfied(player)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean consumeSelectedMaterials(PlayerEntity player) {
        for (AbstractProjectMaterial material : this.getMaterials()) {
            if (material.isSelected()) {
                if (!material.consume(player)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public int getTheoryPointReward() {
        return this.rewardPoints;
    }
    
    @Nullable
    public Block getAidBlock() {
        return this.aidBlock == null ? null : ForgeRegistries.BLOCKS.getValue(this.aidBlock);
    }
}
