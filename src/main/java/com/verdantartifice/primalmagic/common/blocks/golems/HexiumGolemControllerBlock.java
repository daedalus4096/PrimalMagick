package com.verdantartifice.primalmagic.common.blocks.golems;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagic.common.entities.companions.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

/**
 * Definition for the "head" block of a hexium golem.  Place it on top of a T shape of
 * hexium blocks, then use a wand on it, and a hexium golem entity will be created.
 * 
 * @author Daedalus4096
 */
public class HexiumGolemControllerBlock extends AbstractEnchantedGolemControllerBlock<HexiumGolemEntity> {
    protected static final SimpleResearchKey RESEARCH = SimpleResearchKey.parse("HEXIUM_GOLEM");

    public HexiumGolemControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected SimpleResearchKey getRequiredResearch() {
        return RESEARCH;
    }

    @Override
    protected EntityType<HexiumGolemEntity> getEntityType() {
        return EntityTypesPM.HEXIUM_GOLEM.get();
    }

    @Override
    protected Block getBaseBlock() {
        return BlocksPM.HEXIUM_BLOCK.get();
    }

    @Override
    protected Block getControllerBlock() {
        return BlocksPM.HEXIUM_GOLEM_CONTROLLER.get();
    }
}
