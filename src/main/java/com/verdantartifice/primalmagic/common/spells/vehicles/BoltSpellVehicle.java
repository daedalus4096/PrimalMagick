package com.verdantartifice.primalmagic.common.spells.vehicles;

import java.util.Map;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a bolt spell vehicle.  Bolts are mid-range, instant spell vehicles that are not
 * affected by gravity.  Essentially, they're a longer-range touch vehicle with a particle effect.
 * 
 * @author Daedalus4096
 */
public class BoltSpellVehicle extends AbstractRaycastSpellVehicle {
    public static final String TYPE = "bolt";
    protected static final CompoundResearchKey RESEARCH = CompoundResearchKey.from(SimpleResearchKey.parse("SPELL_VEHICLE_BOLT"));
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    protected double getReachDistance(LivingEntity caster) {
        return 6.0D + (2.0D * this.getPropertyValue("range"));
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("range", new SpellProperty("range", "primalmagic.spell.property.range", 1, 5));
        return propMap;
    }
    
    @Override
    public int getBaseManaCostModifier() {
        return this.getPropertyValue("range");
    }
    
    @Override
    protected void drawFx(Level world, SpellPackage spell, Vec3 source, Vec3 target) {
        if (spell.getPayload() != null) {
            // Show a bolt particle effect to every player in range
            PacketHandler.sendToAllAround(
                    new SpellBoltPacket(source, target, spell.getPayload().getSource().getColor()), 
                    world.dimension(), 
                    new BlockPos(source), 
                    64.0D);
        }
    }
}
