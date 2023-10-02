package com.verdantartifice.primalmagick.common.blocks.golems;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.companions.golems.HallowsteelGolemEntity;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

/**
 * Definition for the "head" block of a hallowsteel golem.  Place it on top of a T shape of
 * hallowsteel blocks, then use a wand on it, and a hallowsteel golem entity will be created.
 * 
 * @author Daedalus4096
 */
public class HallowsteelGolemControllerBlock extends AbstractEnchantedGolemControllerBlock<HallowsteelGolemEntity> {
    protected static final SimpleResearchKey RESEARCH = SimpleResearchKey.find("HALLOWSTEEL_GOLEM");

    public HallowsteelGolemControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected SimpleResearchKey getRequiredResearch() {
        return RESEARCH;
    }

    @Override
    protected EntityType<HallowsteelGolemEntity> getEntityType() {
        return EntityTypesPM.HALLOWSTEEL_GOLEM.get();
    }

    @Override
    protected Block getBaseBlock() {
        return BlocksPM.HALLOWSTEEL_BLOCK.get();
    }

    @Override
    protected Block getControllerBlock() {
        return BlocksPM.HALLOWSTEEL_GOLEM_CONTROLLER.get();
    }
}
