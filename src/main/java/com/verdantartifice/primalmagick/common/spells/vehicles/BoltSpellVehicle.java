package com.verdantartifice.primalmagick.common.spells.vehicles;

import java.util.Map;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
    protected static final CompoundResearchKey RESEARCH = ResearchNames.SPELL_VEHICLE_BOLT.get().compoundKey();
    
    public static CompoundResearchKey getResearch() {
        return RESEARCH;
    }

    @Override
    protected double getReachDistance(LivingEntity caster) {
        return (double)this.getRangeBlocks();
    }

    @Override
    protected String getVehicleType() {
        return TYPE;
    }
    
    @Override
    protected Map<String, SpellProperty> initProperties() {
        Map<String, SpellProperty> propMap = super.initProperties();
        propMap.put("range", new SpellProperty("range", "spells.primalmagick.property.range", 1, 5));
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
                    BlockPos.containing(source), 
                    64.0D);
        }
    }
    
    protected int getRangeBlocks() {
        return 6 + (2 * this.getPropertyValue("range"));
    }

    @Override
    public Component getDetailTooltip() {
        return Component.translatable("spells.primalmagick.vehicle." + this.getVehicleType() + ".detail_tooltip", this.getRangeBlocks());
    }

    @Override
    public boolean isIndirect() {
        return true;
    }
}
