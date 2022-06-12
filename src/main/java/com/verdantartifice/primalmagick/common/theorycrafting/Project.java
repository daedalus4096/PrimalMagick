package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge.KnowledgeType;
import com.verdantartifice.primalmagick.common.config.Config;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Class representing an initialized theorycrafting project, created from a data-defined template.
 * Theorycrafting projects grant the player progress toward gaining a theory by spending materials,
 * such as items or observations.
 * 
 * @author Daedalus4096
 */
public class Project implements INBTSerializable<CompoundTag> {
    protected ResourceLocation templateKey;
    protected List<AbstractProjectMaterial> activeMaterials = new ArrayList<>();
    protected double baseSuccessChance;
    protected double baseRewardMultiplier;
    protected ResourceLocation aidBlock;
    
    public Project() {}
    
    public Project(@Nonnull ResourceLocation templateKey, @Nonnull List<AbstractProjectMaterial> materials, double baseSuccessChance, double baseRewardMultiplier, @Nullable ResourceLocation aidBlock) {
        this.templateKey = templateKey;
        this.activeMaterials = materials;
        this.baseSuccessChance = baseSuccessChance;
        this.baseRewardMultiplier = baseRewardMultiplier;
        this.aidBlock = aidBlock;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = new CompoundTag();
        retVal.putString("TemplateKey", this.templateKey.toString());
        retVal.putDouble("BaseSuccessChance", this.baseSuccessChance);
        retVal.putDouble("BaseRewardMultiplier", this.baseRewardMultiplier);
        if (this.aidBlock != null) {
            retVal.putString("AidBlock", this.aidBlock.toString());
        }
        
        ListTag materialList = new ListTag();
        for (AbstractProjectMaterial material : this.activeMaterials) {
            materialList.add(material.serializeNBT());
        }
        retVal.put("Materials", materialList);
        
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.templateKey = new ResourceLocation(nbt.getString("TemplateKey"));
        this.baseSuccessChance = nbt.getDouble("BaseSuccessChance");
        this.baseRewardMultiplier = nbt.getDouble("BaseRewardMultiplier");
        
        this.aidBlock = null;
        if (nbt.contains("AidBlock")) {
            this.aidBlock = new ResourceLocation(nbt.getString("AidBlock"));
        }
        
        this.activeMaterials.clear();
        ListTag materialList = nbt.getList("Materials", Tag.TAG_COMPOUND);
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
        return Mth.clamp(chance, 0.0D, 1.0D);
    }
    
    public boolean isSatisfied(Player player, Set<Block> surroundings) {
        // Determine satisfaction from selected materials
        for (AbstractProjectMaterial material : this.getMaterials()) {
            if (material.isSelected() && !material.isSatisfied(player, surroundings)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean consumeSelectedMaterials(Player player) {
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
        int value = (int)(KnowledgeType.THEORY.getProgression() * (this.baseRewardMultiplier + this.getMaterials().stream().filter(m -> m.isSelected()).mapToDouble(m -> m.getBonusReward()).sum()));
        TheorycraftSpeed modifier = Config.THEORYCRAFT_SPEED.get();
        if (modifier == TheorycraftSpeed.SLOW) {
            value /= 2;
        } else if (modifier == TheorycraftSpeed.FAST) {
            value *= 2;
        }
        return value;
    }
    
    @Nullable
    public Block getAidBlock() {
        return this.aidBlock == null ? null : ForgeRegistries.BLOCKS.getValue(this.aidBlock);
    }
}
