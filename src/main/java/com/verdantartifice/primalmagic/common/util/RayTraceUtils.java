package com.verdantartifice.primalmagic.common.util;

import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Collection of utility methods pertaining to raytracing.
 * 
 * @author Daedalus4096
 */
public class RayTraceUtils {
    /**
     * Calculate the first block/entity being moused over by the player that's within touch range.  Only
     * usable client-side.
     * 
     * @return a raytrace result containing the first block/entity being moused over by the player
     */
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static RayTraceResult getMouseOver() {
        Minecraft mc = Minecraft.getInstance();
        Entity viewEntity = mc.getRenderViewEntity();
        double reachDistance = mc.playerController.extendedReach() ? 6.0D : (double)mc.playerController.getBlockReachDistance();
        Vec3d eyePos = viewEntity.getEyePosition(1.0F);
        
        // Calculate the square of the distance to search in the raytrace; limit to the square distance of the current mouseover block
        double sqReachDistance = (mc.objectMouseOver != null) ? mc.objectMouseOver.getHitVec().squareDistanceTo(eyePos) : (reachDistance * reachDistance);
        
        Vec3d lookVector = viewEntity.getLook(1.0F);
        Vec3d reachPos = eyePos.add(lookVector.scale(reachDistance));
        AxisAlignedBB aabb = viewEntity.getBoundingBox().expand(lookVector.scale(reachDistance)).grow(1.0D, 1.0D, 1.0D);
        
        // Determine if there's an entity closer than the current mouseover block
        EntityRayTraceResult entityResult = RayTraceUtils.rayTraceEntities(mc.world, viewEntity, eyePos, reachPos, aabb, (testEntity) -> {
            return !testEntity.isSpectator();
        }, sqReachDistance);
        
        // If an entity was found, return that; otherwise return the current mouseover block
        if (entityResult != null) {
            return entityResult;
        } else {
            return mc.objectMouseOver;
        }
    }
    
    /**
     * Calculate the closest entity within the given raycast and return a raytrace result for it.  Like
     * ProjectileHelper#func_221269_a, but with a precise hitVec for the result instead of defaulting to
     * the entity's position.
     *  
     * @param world the world in which to perform the raytrace search
     * @param excludeEntity an optional entity to exclude from the raytrace
     * @param startVec starting world position of the ray to trace
     * @param endVec ending world position of the ray to trace
     * @param aabb the bounding box in which to search for entities
     * @param selector an optional predicate to filter out entities from the search set
     * @param maxSqDistance the square of the maximum distance to search
     * @return a raytrace result with the closest found entity that meets the given criteria, or null if none were found
     * @see {@link net.minecraft.entity.projectile.ProjectileHelper#func_221269_a}
     */
    @Nullable
    public static EntityRayTraceResult rayTraceEntities(@Nonnull World world, @Nullable Entity excludeEntity, @Nonnull Vec3d startVec, @Nonnull Vec3d endVec, @Nonnull AxisAlignedBB aabb, @Nullable Predicate<Entity> selector, double maxSqDistance) {
        double sqDistThreshold = maxSqDistance;
        Entity hitEntity = null;
        Vec3d hitVec = null;
        
        // Get all entities in the given bounding box which satisfy the given criteria
        for (Entity entity : world.getEntitiesInAABBexcluding(excludeEntity, aabb, selector)) {
            AxisAlignedBB entityAABB = entity.getBoundingBox().grow(0.3D);
            Optional<Vec3d> optionalHitVec = entityAABB.rayTrace(startVec, endVec);
            if (optionalHitVec.isPresent()) {
                // If the entity is hit by the ray, determine if it's the closest one yet
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
    
    /**
     * Transform the given entity raytrace result into a block raytrace result for the same position.
     * 
     * @param entityResult the raytrace result to be transformed
     * @return a block raytrace result for the same position
     */
    @Nullable
    public static BlockRayTraceResult getBlockResultFromEntityResult(@Nullable EntityRayTraceResult entityResult) {
        if (entityResult == null) {
            return null;
        }
        
        // Get the raytrace result's hitVec and the entity's position
        BlockPos targetPos = new BlockPos(entityResult.getHitVec());
        Vec3d entityVec = entityResult.getEntity().getPositionVec();
        BlockPos entityPos = new BlockPos(entityVec);
        Vec3d targetVec = new Vec3d(targetPos.getX() + 0.5D, targetPos.getY() + 0.5D, targetPos.getZ() + 0.5D);
        
        // Calculate a direction vector based on the raytrace result's hitVec and the entity's position
        Vec3d dirVec = entityVec.subtract(targetVec);
        Direction dir = Direction.getFacingFromVector(dirVec.x, dirVec.y, dirVec.z);
        
        return new BlockRayTraceResult(entityResult.getHitVec(), dir, entityPos, false);
    }
}
