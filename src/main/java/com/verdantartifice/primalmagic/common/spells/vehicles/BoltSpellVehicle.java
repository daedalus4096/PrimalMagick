package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;

import net.minecraft.entity.player.PlayerEntity;

public class BoltSpellVehicle extends AbstractRaycastSpellVehicle {
    public static final String TYPE = "bolt";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_VEHICLE_BOLT"));
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    protected double getReachDistance(PlayerEntity caster) {
        return 16.0D;
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }
}
