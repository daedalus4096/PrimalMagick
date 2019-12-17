package com.verdantartifice.primalmagic.common.spells.vehicles;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellImpactPacket;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TouchSpellVehicle extends AbstractSpellVehicle {
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
    public void execute(SpellPackage spell, World world, PlayerEntity caster) {
        if (spell.getPayload() != null) {
            double reachDistance = caster.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
            Vec3d eyePos = caster.getEyePosition(1.0F);
            Vec3d lookVector = caster.getLook(1.0F);
            Vec3d reachPos = eyePos.add(lookVector.scale(reachDistance));
            AxisAlignedBB aabb = caster.getBoundingBox().expand(lookVector.scale(reachDistance)).grow(1.0D, 1.0D, 1.0D);
            EntityRayTraceResult entityResult = ProjectileHelper.func_221269_a(world, caster, eyePos, reachPos, aabb, (testEntity) -> {
                return !testEntity.isSpectator();
            }, (reachDistance * reachDistance));
            BlockRayTraceResult blockResult = world.rayTraceBlocks(new RayTraceContext(eyePos, reachPos, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.SOURCE_ONLY, caster));
            RayTraceResult result;
            if (entityResult == null) {
                result = blockResult;
            } else {
                result = (eyePos.squareDistanceTo(entityResult.getHitVec()) <= eyePos.squareDistanceTo(blockResult.getHitVec())) ? entityResult : blockResult;
            }
            if (!world.isRemote) {
                Vec3d hitVec = result.getHitVec();
                PacketHandler.sendToAllAround(
                        new SpellImpactPacket(hitVec.x, hitVec.y, hitVec.z, spell.getPayload().getSource().getColor()), 
                        world.getDimension().getType(), 
                        new BlockPos(hitVec), 
                        64.0D);
            }
            spell.getPayload().execute(result, spell, world, caster);
        }
    }
}
