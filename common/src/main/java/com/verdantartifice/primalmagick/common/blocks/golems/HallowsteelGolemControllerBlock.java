package com.verdantartifice.primalmagick.common.blocks.golems;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.companions.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

/**
 * Definition for the "head" block of a hallowsteel golem.  Place it on top of a T shape of
 * hallowsteel blocks, then use a wand on it, and a hallowsteel golem entity will be created.
 * 
 * @author Daedalus4096
 */
public class HallowsteelGolemControllerBlock extends AbstractEnchantedGolemControllerBlock<HallowsteelGolemEntity> {
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.HALLOWSTEEL_GOLEM));

    public HallowsteelGolemControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    @Override
    protected EntityType<HallowsteelGolemEntity> getEntityType() {
        return EntityTypesPM.HALLOWSTEEL_GOLEM.get();
    }

    @Override
    protected Block getBaseBlock() {
        return BlocksPM.get(BlocksPM.HALLOWSTEEL_BLOCK);
    }

    @Override
    protected Block getControllerBlock() {
        return BlocksPM.get(BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER);
    }
}
