package com.verdantartifice.primalmagic.common.spells.packages;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellImpactPacket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SelfSpellPackage extends AbstractSpellPackage {
    public static final String TYPE = "self";
    
    public SelfSpellPackage() {
        super();
    }
    
    public SelfSpellPackage(String name) {
        super(name);
    }

    @Override
    protected String getPackageType() {
        return TYPE;
    }

    @Override
    public void cast(World world, PlayerEntity caster) {
        super.cast(world, caster);
        if (this.payload != null) {
            RayTraceResult result = new EntityRayTraceResult(caster);
            if (!world.isRemote) {
                Vec3d hitVec = caster.getEyePosition(1.0F);
                PacketHandler.sendToAllAround(
                        new SpellImpactPacket(hitVec.x, hitVec.y, hitVec.z, this.payload.getSource().getColor()), 
                        world.getDimension().getType(), 
                        caster.getPosition(), 
                        64.0D);
            }
            this.payload.execute(result, this, world, caster);
        }
    }
}
