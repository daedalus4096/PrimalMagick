package com.verdantartifice.primalmagick.common.blocks.golems;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.entities.EntityTypesPM;
import com.verdantartifice.primalmagick.common.entities.companions.golems.HexiumGolemEntity;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

/**
 * Definition for the "head" block of a hexium golem.  Place it on top of a T shape of
 * hexium blocks, then use a wand on it, and a hexium golem entity will be created.
 * 
 * @author Daedalus4096
 */
public class HexiumGolemControllerBlock extends AbstractEnchantedGolemControllerBlock<HexiumGolemEntity> {
    protected static final Supplier<SimpleResearchKey> RESEARCH = ResearchNames.simpleKey(ResearchNames.HEXIUM_GOLEM);

    public HexiumGolemControllerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected SimpleResearchKey getRequiredResearch() {
        return RESEARCH.get();
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
