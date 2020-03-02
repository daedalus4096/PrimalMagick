package com.verdantartifice.primalmagic.common.theorycrafting;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;
import com.verdantartifice.primalmagic.common.util.InventoryUtils;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Base class for a theorycrafting research project.  Research projects grant the player progress
 * toward gaining a theory by spending materials, such as items or observations.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractProject implements INBTSerializable<CompoundNBT> {
    protected List<AbstractProjectMaterial> activeMaterials = new ArrayList<>();
    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT retVal = new CompoundNBT();
        retVal.putString("ProjectType", this.getProjectType());
        
        ListNBT materialList = new ListNBT();
        for (AbstractProjectMaterial material : this.activeMaterials) {
            materialList.add(material.serializeNBT());
        }
        retVal.put("Materials", materialList);
        
        return retVal;
    }
    
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.activeMaterials.clear();
        ListNBT materialList = nbt.getList("Materials", Constants.NBT.TAG_COMPOUND);
        for (int index = 0; index < materialList.size(); index++) {
            AbstractProjectMaterial material = ProjectFactory.getMaterialFromNBT(materialList.getCompound(index));
            if (material != null) {
                this.activeMaterials.add(material);
            }
        }
    }
    
    public boolean initialize(PlayerEntity player) {
        SimpleResearchKey research = this.getRequiredResearch();
        if (research != null && !research.isKnownByStrict(player)) {
            // Fail initialization to prevent use if the player doesn't have the right research unlocked
            return false;
        }
        
        // Randomly select materials to use from the bag of options
        WeightedRandomBag<AbstractProjectMaterial> options = this.getMaterialOptions();
        for (int index = 0; index < this.getRequiredMaterialCount(player); index++) {
            this.activeMaterials.add(options.getRandom(player.getRNG()).copy());
        }
        
        return true;
    }
    
    /**
     * Get the type name for this project.
     * 
     * @return the type name for this project
     */
    protected abstract String getProjectType();
    
    @Nonnull
    public String getNameTranslationKey() {
        return PrimalMagic.MODID + ".research_project.name." + this.getProjectType();
    }
    
    @Nonnull
    public String getTextTranslationKey() {
        return PrimalMagic.MODID + ".research_project.text." + this.getProjectType();
    }
    
    @Nullable
    public SimpleResearchKey getRequiredResearch() {
        return null;
    }
    
    /**
     * Get the weighted material options for this project.
     * 
     * @return the weighted material options for this project
     */
    @Nonnull
    protected abstract WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions();
    
    @Nonnull
    public List<AbstractProjectMaterial> getMaterials() {
        return this.activeMaterials;
    }
    
    protected int getRequiredMaterialCount(PlayerEntity player) {
        // Get projects completed from stats and calculate based on that
        int completed = StatsManager.getValue(player, StatsPM.RESEARCH_PROJECTS_COMPLETED);
        return Math.min(4, 1 + (completed / 5));
    }
    
    protected double getBaseSuccessChance(PlayerEntity player) {
        // Get projects completed from stats and calculate based on that
        int completed = StatsManager.getValue(player, StatsPM.RESEARCH_PROJECTS_COMPLETED);
        return Math.max(0.0D, 50.0D - (10.0D * (completed / 3)));
    }
    
    protected double getSuccessChancePerMaterial(PlayerEntity player) {
        // A full complement of materials should always be enough to get the player to 100% success chance
        int materialCount = this.activeMaterials.size();
        if (materialCount <= 0) {
            return 0.0D;
        } else {
            return (100.0D - this.getBaseSuccessChance(player)) / materialCount;
        }
    }
    
    public double getSuccessChance(PlayerEntity player) {
        double chance = this.getBaseSuccessChance(player);
        double per = this.getSuccessChancePerMaterial(player);
        for (AbstractProjectMaterial material : this.getMaterials()) {
            if (material.isSelected()) {
                chance += per;
            }
        }
        return MathHelper.clamp(chance, 0.0D, 100.0D);
    }
    
    public boolean isSatisfied(PlayerEntity player) {
        // Gather requirements from selected materials
        SatisfactionCritera criteria = new SatisfactionCritera();
        for (AbstractProjectMaterial material : this.getMaterials()) {
            if (material.isSelected()) {
                material.gatherRequirements(criteria);
            }
        }
        
        // Determine if gathered requirements are satisfied
        for (ItemStack stack : criteria.itemStacks) {
            if (!InventoryUtils.isPlayerCarrying(player, stack)) {
                return false;
            }
        }
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null || knowledge.getKnowledge(IPlayerKnowledge.KnowledgeType.OBSERVATION) < criteria.observations) {
            return false;
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
    
    public static class SatisfactionCritera {
        public List<ItemStack> itemStacks = new ArrayList<>();
        public int observations = 0;
    }
}
