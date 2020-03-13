package com.verdantartifice.primalmagic.common.theorycrafting.projects;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProjectMaterial;
import com.verdantartifice.primalmagic.common.theorycrafting.ItemProjectMaterial;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;

/**
 * Definition of a research project option.
 * 
 * @author Daedalus4096
 */
public class HitTheBooksProject extends AbstractProject {
    public static final String TYPE = "hit_the_books";
    
    protected static final WeightedRandomBag<AbstractProjectMaterial> OPTIONS = new WeightedRandomBag<>();
    protected static final Block AID = Blocks.BOOKSHELF;
    
    static {
        OPTIONS.add(new ItemProjectMaterial(Items.BOOK, false), 1);
    }

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
