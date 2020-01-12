package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.entity.player.PlayerEntity;

public class TouchSpellVehicle extends AbstractRaycastSpellVehicle {
    public static final String TYPE = "touch";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("BASIC_SORCERY"));
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }
    
    @Override
    protected double getReachDistance(PlayerEntity caster) {
        return caster.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
    }
}
