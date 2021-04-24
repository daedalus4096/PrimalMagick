package com.verdantartifice.primalmagic.common.blocks.golems;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.entities.companions.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;

/**
 * Definition for the "head" block of a primalite golem.  Place it on top of a T shape of
 * primalite blocks, then use a wand on it, and a primalite golem entity will be created.
 * 
 * @author Daedalus4096
 */
public class PrimaliteGolemControllerBlock extends AbstractEnchantedGolemControllerBlock<PrimaliteGolemEntity> {
    protected static final SimpleResearchKey RESEARCH = SimpleResearchKey.parse("PRIMALITE_GOLEM");
    
    public PrimaliteGolemControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected SimpleResearchKey getRequiredResearch() {
        return RESEARCH;
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
