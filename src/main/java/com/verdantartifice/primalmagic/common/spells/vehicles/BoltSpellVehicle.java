package com.verdantartifice.primalmagic.common.spells.vehicles;

import java.util.Map;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellProperty;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
    protected double getReachDistance(PlayerEntity caster) {
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
    protected void drawFx(World world, SpellPackage spell, Vec3d source, Vec3d target) {
        if (spell.getPayload() != null) {
            // Show a bolt particle effect to every player in range
            PacketHandler.sendToAllAround(
                    new SpellBoltPacket(source, target, spell.getPayload().getSource().getColor()), 
                    world.dimension.getType(), 
                    new BlockPos(source), 
                    64.0D);
        }
    }
}
