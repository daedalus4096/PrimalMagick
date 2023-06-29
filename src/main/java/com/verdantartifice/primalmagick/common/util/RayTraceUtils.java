package com.verdantartifice.primalmagick.common.util;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.joml.Vector3d;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

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
    public static HitResult getMouseOver(Level level) {
        Minecraft mc = Minecraft.getInstance();
        Entity viewEntity = mc.getCameraEntity();
        double reachDistance = mc.gameMode.hasFarPickRange() ? 6.0D : (double)mc.gameMode.getPickRange();
        Vec3 eyePos = viewEntity.getEyePosition(1.0F);
        
        // Calculate the square of the distance to search in the raytrace; limit to the square distance of the current mouseover block
        double sqReachDistance = (mc.hitResult != null) ? mc.hitResult.getLocation().distanceToSqr(eyePos) : (reachDistance * reachDistance);
        
        Vec3 lookVector = viewEntity.getViewVector(1.0F);
        Vec3 reachPos = eyePos.add(lookVector.scale(reachDistance));
        AABB aabb = viewEntity.getBoundingBox().expandTowards(lookVector.scale(reachDistance)).inflate(1.0D, 1.0D, 1.0D);
        
        // Determine if there's an entity closer than the current mouseover block
        EntityHitResult entityResult = RayTraceUtils.rayTraceEntities(level, viewEntity, eyePos, reachPos, aabb, (testEntity) -> {
            return !testEntity.isSpectator();
        }, sqReachDistance);
        
        // If an entity was found, return that; otherwise return the current mouseover block
        if (entityResult != null) {
            return entityResult;
        } else {
            return mc.hitResult;
        }
    }
    
    /**
     * Calculate the closest entity within the given raycast and return a raytrace result for it.  Like
     * ProjectileHelper#getEntityHitResult, but with a precise hitVec for the result instead of defaulting to
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
     * @see {@link net.minecraft.entity.projectile.ProjectileHelper#getEntityHitResult}
     */
    @Nullable
    public static EntityHitResult rayTraceEntities(@Nonnull Level world, @Nullable Entity excludeEntity, @Nonnull Vec3 startVec, @Nonnull Vec3 endVec, @Nonnull AABB aabb, @Nullable Predicate<Entity> selector, double maxSqDistance) {
        double sqDistThreshold = maxSqDistance;
        Entity hitEntity = null;
        Vec3 hitVec = null;
        
        // Get all entities in the given bounding box which satisfy the given criteria
        for (Entity entity : world.getEntities(excludeEntity, aabb, selector)) {
            AABB entityAABB = entity.getBoundingBox().inflate(0.3D);
            Optional<Vec3> optionalHitVec = entityAABB.clip(startVec, endVec);
            if (optionalHitVec.isPresent()) {
                // If the entity is hit by the ray, determine if it's the closest one yet
                double sqDist = startVec.distanceToSqr(optionalHitVec.get());
                if (sqDist < sqDistThreshold) {
                    hitEntity = entity;
                    hitVec = optionalHitVec.get();
                }
            }
        }
        
        if (hitEntity == null || hitVec == null) {
            return null;
        } else {
            return new EntityHitResult(hitEntity, hitVec);
        }
    }
    
    /**
     * Transform the given entity raytrace result into a block raytrace result for the same position.
     * 
     * @param entityResult the raytrace result to be transformed
     * @return a block raytrace result for the same position
     */
    @Nullable
    public static BlockHitResult getBlockResultFromEntityResult(@Nullable EntityHitResult entityResult) {
        if (entityResult == null) {
            return null;
        }
        
        // Get the raytrace result's hitVec and the entity's position
        BlockPos targetPos = BlockPos.containing(entityResult.getLocation());
        Vec3 entityVec = entityResult.getEntity().position();
        BlockPos entityPos = BlockPos.containing(entityVec);
        Vec3 targetVec = new Vec3(targetPos.getX() + 0.5D, targetPos.getY() + 0.5D, targetPos.getZ() + 0.5D);
        
        // Calculate a direction vector based on the raytrace result's hitVec and the entity's position
        Vec3 dirVec = entityVec.subtract(targetVec);
        Direction dir = Direction.getNearest(dirVec.x, dirVec.y, dirVec.z);
        
        return new BlockHitResult(entityResult.getLocation(), dir, entityPos, false);
    }
    
    /**
     * Determine whether the source entity has an unobstructed line of sight from its eye to the target block.
     * 
     * @param source the source entity
     * @param target the position of the target block
     * @return true if the source entity has an unobstructed line of sight to the target block, false otherwise
     */
    public static boolean hasLineOfSight(@Nullable Entity source, @Nullable BlockPos target) {
        if (source == null || target == null) {
            return false;
        }
        
        Vec3 sourceVec = source.getEyePosition();
        Vec3 targetVec = Vec3.atCenterOf(target);
        ClipContext context = new ClipContext(sourceVec, targetVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, source);
        BlockHitResult result = source.level().clip(context);
        
        if (result == null || result.getType() == HitResult.Type.MISS) {
            return true;
        } else if (result.getType() == HitResult.Type.BLOCK) {
            return target.equals(result.getBlockPos());
        } else {
            return false;
        }
    }
    
    /**
     * Determine whether the source block has an unobstructed line of sight to the target block.
     * 
     * @param world the world in which to test
     * @param source the position of the source block
     * @param target the position of the target block
     * @return true if the source block has an unobstructed line of sight to the target block, false otherwise
     */
    public static boolean hasLineOfSight(@Nullable Level world, @Nullable BlockPos source, @Nullable BlockPos target) {
        // TODO Refactor this to use the standard ClipContext now that the entity seems optional
        if (world == null || source == null || target == null) {
            return false;
        }
        
        Vec3 startVec = new Vec3(source.getX() + 0.5D, source.getY() + 0.5D, source.getZ() + 0.5D);
        Vec3 endVec = new Vec3(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D);
        EntitylessRayTraceContext context = new EntitylessRayTraceContext(world, startVec, endVec, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY);
        BlockHitResult result = RayTraceUtils.rayTraceBlocksIgnoringSource(context);

        if (result == null || result.getType() == HitResult.Type.MISS) {
            return true;
        } else if (result.getType() == HitResult.Type.BLOCK) {
            return target.equals(result.getBlockPos());
        } else {
            return false;
        }
    }
    
    /**
     * @see {@link net.minecraft.world.IBlockReader#rayTraceBlocks(RayTraceContext)}
     */
    protected static BlockHitResult rayTraceBlocksIgnoringSource(EntitylessRayTraceContext context) {
        return iterateRayTrace(context, RayTraceUtils::doRayTraceCheck, RayTraceUtils::createMiss);
    }
    
    protected static BlockHitResult doRayTraceCheck(EntitylessRayTraceContext context, BlockPos pos) {
        BlockGetter world = context.getWorld();
        BlockState blockState = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);
        Vec3 startVec = context.getStartVec();
        Vec3 endVec = context.getEndVec();
        VoxelShape blockShape = context.getBlockShape(blockState, world, pos);
        BlockHitResult blockResult = RayTraceUtils.doCollisionCheck(world, startVec, endVec, pos, blockShape, blockState);
        VoxelShape fluidShape = context.getFluidShape(fluidState, world, pos);
        BlockHitResult fluidResult = fluidShape.clip(startVec, endVec, pos);
        double blockDistanceSq = blockResult == null ? Double.MAX_VALUE : startVec.distanceToSqr(blockResult.getLocation());
        double fluidDistanceSq = fluidResult == null ? Double.MAX_VALUE : startVec.distanceToSqr(fluidResult.getLocation());
        return blockDistanceSq <= fluidDistanceSq ? blockResult : fluidResult;
    }
    
    protected static BlockHitResult createMiss(EntitylessRayTraceContext context) {
        Vec3 endVec = context.getEndVec();
        Vec3 delta = context.getStartVec().subtract(endVec);
        return BlockHitResult.miss(endVec, Direction.getNearest(delta.x, delta.y, delta.z), BlockPos.containing(endVec));
    }
    
    /**
     * @see {@link net.minecraft.world.IBlockReader#rayTraceBlocks(Vector3d, Vector3d, BlockPos, VoxelShape, BlockState)}
     */
    @Nullable
    protected static BlockHitResult doCollisionCheck(BlockGetter world, Vec3 startVec, Vec3 endVec, BlockPos iteratedPos, VoxelShape iteratedShape, BlockState iteratedState) {
        BlockHitResult result = iteratedShape.clip(startVec, endVec, iteratedPos);
        if (result != null) {
            BlockHitResult faceResult = iteratedState.getInteractionShape(world, iteratedPos).clip(startVec, endVec, iteratedPos);
            if (faceResult != null && (faceResult.getLocation().subtract(startVec).lengthSqr() < result.getLocation().subtract(startVec).lengthSqr())) {
                return result.withDirection(faceResult.getDirection());
            }
        }
        return result;
    }
    
    /**
     * @see {@link net.minecraft.world.IBlockReader#traverseBlocks(RayTraceContext, BiFunction, Function)}
     */
    protected static BlockHitResult iterateRayTrace(EntitylessRayTraceContext context, BiFunction<EntitylessRayTraceContext, BlockPos, BlockHitResult> checkFunc, Function<EntitylessRayTraceContext, BlockHitResult> missFunc) {
        Vec3 startVec = context.getStartVec();
        Vec3 endVec = context.getEndVec();
        if (startVec.equals(endVec)) {
            return missFunc.apply(context);
        } else {
            double adjEndX = Mth.lerp(-1.0E-7D, endVec.x, startVec.x);
            double adjEndY = Mth.lerp(-1.0E-7D, endVec.y, startVec.y);
            double adjEndZ = Mth.lerp(-1.0E-7D, endVec.z, startVec.z);
            double adjStartX = Mth.lerp(-1.0E-7D, startVec.x, endVec.x);
            double adjStartY = Mth.lerp(-1.0E-7D, startVec.y, endVec.y);
            double adjStartZ = Mth.lerp(-1.0E-7D, startVec.z, endVec.z);
            int adjStartXFloor = Mth.floor(adjStartX);
            int adjStartYFloor = Mth.floor(adjStartY);
            int adjStartZFloor = Mth.floor(adjStartZ);
            BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos(adjStartXFloor, adjStartYFloor, adjStartZFloor);

            double deltaX = adjEndX - adjStartX;
            double deltaY = adjEndY - adjStartY;
            double deltaZ = adjEndZ - adjStartZ;
            int signDeltaX = Mth.sign(deltaX);
            int signDeltaY = Mth.sign(deltaY);
            int signDeltaZ = Mth.sign(deltaZ);
            double d9 = signDeltaX == 0 ? Double.MAX_VALUE : (double)signDeltaX / deltaX;
            double d10 = signDeltaY == 0 ? Double.MAX_VALUE : (double)signDeltaY / deltaY;
            double d11 = signDeltaZ == 0 ? Double.MAX_VALUE : (double)signDeltaZ / deltaZ;
            double d12 = d9 * (signDeltaX > 0 ? 1.0D - Mth.frac(adjStartX) : Mth.frac(adjStartX));
            double d13 = d10 * (signDeltaY > 0 ? 1.0D - Mth.frac(adjStartY) : Mth.frac(adjStartY));
            double d14 = d11 * (signDeltaZ > 0 ? 1.0D - Mth.frac(adjStartZ) : Mth.frac(adjStartZ));
            
            while (d12 <= 1.0D || d13 <= 1.0D || d14 <= 1.0D) {
                if (d12 < d13) {
                    if (d12 < d14) {
                        adjStartXFloor += signDeltaX;
                        d12 += d9;
                    } else {
                        adjStartZFloor += signDeltaZ;
                        d14 += d11;
                    }
                } else if (d13 < d14) {
                    adjStartYFloor += signDeltaY;
                    d13 += d10;
                } else {
                    adjStartZFloor += signDeltaZ;
                    d14 += d11;
                }
                
                BlockHitResult result = checkFunc.apply(context, mbp.set(adjStartXFloor, adjStartYFloor, adjStartZFloor));
                if (result != null) {
                    return result;
                }
            }
            return missFunc.apply(context);
        }
    }
    
    /**
     * Like a normal RayTraceContext, but uses a dummy selection context and doesn't require an Entity.
     * 
     * @author Daedalus4096
     * @see {@link net.minecraft.util.math.RayTraceContext}
     */
    protected static class EntitylessRayTraceContext {
        private final BlockGetter world;
        private final Vec3 startVec;
        private final Vec3 endVec;
        private final ClipContext.Block blockMode;
        private final ClipContext.Fluid fluidMode;

        public EntitylessRayTraceContext(BlockGetter world, Vec3 startVec, Vec3 endVec, ClipContext.Block blockMode, ClipContext.Fluid fluidMode) {
            this.world = world;
            this.startVec = startVec;
            this.endVec = endVec;
            this.blockMode = blockMode;
            this.fluidMode = fluidMode;
        }
        
        public BlockGetter getWorld() {
            return this.world;
        }
        
        public Vec3 getStartVec() {
            return this.startVec;
        }
        
        public Vec3 getEndVec() {
            return this.endVec;
        }
        
        public VoxelShape getBlockShape(BlockState state, BlockGetter world, BlockPos pos) {
            return this.blockMode.get(state, world, pos, CollisionContext.empty());
        }
        
        public VoxelShape getFluidShape(FluidState state, BlockGetter world, BlockPos pos) {
            return this.fluidMode.canPick(state) ? state.getShape(world, pos) : Shapes.empty();
        }
    }
}
