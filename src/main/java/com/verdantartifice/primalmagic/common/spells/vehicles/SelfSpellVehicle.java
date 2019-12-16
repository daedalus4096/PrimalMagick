package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellImpactPacket;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SelfSpellVehicle extends AbstractSpellVehicle {
    public static final String TYPE = "self";
    
    @Override
    protected String getVehicleType() {
        return TYPE;
    }

    @Override
    public void execute(SpellPackage spell, World world, PlayerEntity caster) {
        if (spell.getPayload() != null) {
            RayTraceResult result = new EntityRayTraceResult(caster);
            if (!world.isRemote) {
                Vec3d hitVec = caster.getEyePosition(1.0F);
                PacketHandler.sendToAllAround(
                        new SpellImpactPacket(hitVec.x, hitVec.y, hitVec.z, spell.getPayload().getSource().getColor()), 
                        world.getDimension().getType(), 
                        caster.getPosition(), 
                        64.0D);
            }
            spell.getPayload().execute(result, spell, world, caster);
        }
    }
}
