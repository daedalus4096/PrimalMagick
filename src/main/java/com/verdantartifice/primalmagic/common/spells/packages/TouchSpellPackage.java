package com.verdantartifice.primalmagic.common.spells.packages;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TouchSpellPackage extends AbstractSpellPackage {
    public static final String TYPE = "touch";
    
    public TouchSpellPackage() {
        super();
    }

    public TouchSpellPackage(String name) {
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
            this.payload.execute(result, this, world, caster);
        }
    }
}
