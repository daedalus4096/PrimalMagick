package com.verdantartifice.primalmagick.common.spells.vehicles;

import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

/**
 * Definition of a touch spell vehicle.  This vehicle selects the closest target within the caster's
 * reach along their line of sight.
 * 
 * @author Daedalus4096
 */
public class TouchSpellVehicle extends AbstractRaycastSpellVehicle {
    public static final String TYPE = "touch";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.BASIC_SORCERY));
    
    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }
    
    @Override
    protected double getReachDistance(LivingEntity caster) {
        return caster.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue();
    }

    @Override
    public boolean isIndirect() {
        return false;
    }
}
