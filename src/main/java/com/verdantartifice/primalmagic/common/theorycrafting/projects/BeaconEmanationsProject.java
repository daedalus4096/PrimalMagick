package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemTagProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class BeaconEmanationsProject extends AbstractProject {
    public static final String TYPE = "beacon_emanations";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = Util.make(new WeightedRandomBag<>(), bag -> {
        bag.add(new ItemTagProjectMaterial(new ResourceLocation("forge", "beacon_payment"), true), 1);
    });
    protected static final Block AID = Blocks.BEACON;
    
    @Override
    protected String getProjectType() {
        return TYPE;
    }

    @Override
    protected WeightedRandomBag<AbstractProjectMaterial> getMaterialOptions(PlayerEntity player) {
        return OPTIONS;
    }
    
    @Override
    public Block getAidBlock() {
        return AID;
    }

    @Override
    protected int getRequiredMaterialCount(PlayerEntity player) {
        return 1;
    }
    
    @Override
    protected double getBaseSuccessChance(PlayerEntity player) {
        return 0.5D;
    }
    
    @Override
    public int getTheoryPointReward() {
        // Return double the normal reward
        return (IPlayerKnowledge.KnowledgeType.THEORY.getProgression() / 2);
    }
}
