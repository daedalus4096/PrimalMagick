package com.verdantartifice.primalmagick.common.blocks.golems;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.companions.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

/**
 * Definition for the "head" block of a primalite golem.  Place it on top of a T shape of
 * primalite blocks, then use a wand on it, and a primalite golem entity will be created.
 * 
 * @author Daedalus4096
 */
public class PrimaliteGolemControllerBlock extends AbstractEnchantedGolemControllerBlock<PrimaliteGolemEntity> {
    protected static final Supplier<SimpleResearchKey> RESEARCH = ResearchNames.simpleKey(ResearchNames.PRIMALITE_GOLEM);
    
    public PrimaliteGolemControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected SimpleResearchKey getRequiredResearch() {
        return RESEARCH.get();
    }

    @Override
    protected EntityType<PrimaliteGolemEntity> getEntityType() {
        return EntityTypesPM.PRIMALITE_GOLEM.get();
    }

    @Override
    protected Block getBaseBlock() {
        return BlocksPM.PRIMALITE_BLOCK.get();
    }

    @Override
    protected Block getControllerBlock() {
        return BlocksPM.PRIMALITE_GOLEM_CONTROLLER.get();
    }
}
