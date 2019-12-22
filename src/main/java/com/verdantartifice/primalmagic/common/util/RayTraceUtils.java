package com.verdantartifice.primalmagic.common.util;

import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RayTraceUtils {
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static RayTraceResult getMouseOver() {
        Minecraft mc = Minecraft.getInstance();
        Entity viewEntity = mc.getRenderViewEntity();
        double reachDistance = mc.playerController.extendedReach() ? 6.0D : (double)mc.playerController.getBlockReachDistance();
        Vec3d eyePos = viewEntity.getEyePosition(1.0F);
        double sqReachDistance = (mc.objectMouseOver != null) ? mc.objectMouseOver.getHitVec().squareDistanceTo(eyePos) : (reachDistance * reachDistance);
        Vec3d lookVector = viewEntity.getLook(1.0F);
        Vec3d reachPos = eyePos.add(lookVector.scale(reachDistance));
        AxisAlignedBB aabb = viewEntity.getBoundingBox().expand(lookVector.scale(reachDistance)).grow(1.0D, 1.0D, 1.0D);
        EntityRayTraceResult entityResult = RayTraceUtils.rayTraceEntities(mc.world, viewEntity, eyePos, reachPos, aabb, (testEntity) -> {
            return !testEntity.isSpectator();
        }, sqReachDistance);
        if (entityResult != null) {
            return entityResult;
        } else {
            return mc.objectMouseOver;
        }
    }
    
    @Nullable
    public static EntityRayTraceResult rayTraceEntities(@Nonnull World world, @Nullable Entity excludeEntity, @Nonnull Vec3d startVec, @Nonnull Vec3d endVec, @Nonnull AxisAlignedBB aabb, @Nullable Predicate<Entity> selector, double maxSqDistance) {
        double sqDistThreshold = maxSqDistance;
        Entity hitEntity = null;
        Vec3d hitVec = null;
        
        for (Entity entity : world.getEntitiesInAABBexcluding(excludeEntity, aabb, selector)) {
            AxisAlignedBB entityAABB = entity.getBoundingBox().grow(0.3D);
            Optional<Vec3d> optionalHitVec = entityAABB.rayTrace(startVec, endVec);
            if (optionalHitVec.isPresent()) {
                double sqDist = startVec.squareDistanceTo(optionalHitVec.get());
                if (sqDist < sqDistThreshold) {
                    hitEntity = entity;
                    hitVec = optionalHitVec.get();
                }
            }
        }
        
        if (hitEntity == null || hitVec == null) {
            return null;
        } else {
            return new EntityRayTraceResult(hitEntity, hitVec);
        }
    }
}
