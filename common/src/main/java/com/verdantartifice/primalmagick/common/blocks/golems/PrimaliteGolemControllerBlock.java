package com.verdantartifice.primalmagick.common.blocks.golems;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.companions.golems.PrimaliteGolemEntity;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

/**
 * Definition for the "head" block of a primalite golem.  Place it on top of a T shape of
 * primalite blocks, then use a wand on it, and a primalite golem entity will be created.
 * 
 * @author Daedalus4096
 */
public class PrimaliteGolemControllerBlock extends AbstractEnchantedGolemControllerBlock<PrimaliteGolemEntity> {
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.PRIMALITE_GOLEM));
    
    public PrimaliteGolemControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    @Override
    protected EntityType<PrimaliteGolemEntity> getEntityType() {
        return EntityTypesPM.PRIMALITE_GOLEM.get();
    }

    @Override
    protected Block getBaseBlock() {
        return BlocksPM.get(BlocksPM.PRIMALITE_BLOCK);
    }

    @Override
    protected Block getControllerBlock() {
        return BlocksPM.get(BlocksPM.PRIMALITE_GOLEM_CONTROLLER);
    }
}
