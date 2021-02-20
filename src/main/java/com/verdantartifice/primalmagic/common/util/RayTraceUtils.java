package com.verdantartifice.primalmagic.common.util;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
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
        Vector3d eyePos = viewEntity.getEyePosition(1.0F);
        
        // Calculate the square of the distance to search in the raytrace; limit to the square distance of the current mouseover block
        double sqReachDistance = (mc.objectMouseOver != null) ? mc.objectMouseOver.getHitVec().squareDistanceTo(eyePos) : (reachDistance * reachDistance);
        
        Vector3d lookVector = viewEntity.getLook(1.0F);
        Vector3d reachPos = eyePos.add(lookVector.scale(reachDistance));
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
    public static EntityRayTraceResult rayTraceEntities(@Nonnull World world, @Nullable Entity excludeEntity, @Nonnull Vector3d startVec, @Nonnull Vector3d endVec, @Nonnull AxisAlignedBB aabb, @Nullable Predicate<Entity> selector, double maxSqDistance) {
        double sqDistThreshold = maxSqDistance;
        Entity hitEntity = null;
        Vector3d hitVec = null;
        
        // Get all entities in the given bounding box which satisfy the given criteria
        for (Entity entity : world.getEntitiesInAABBexcluding(excludeEntity, aabb, selector)) {
            AxisAlignedBB entityAABB = entity.getBoundingBox().grow(0.3D);
            Optional<Vector3d> optionalHitVec = entityAABB.rayTrace(startVec, endVec);
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
        Vector3d entityVec = entityResult.getEntity().getPositionVec();
        BlockPos entityPos = new BlockPos(entityVec);
        Vector3d targetVec = new Vector3d(targetPos.getX() + 0.5D, targetPos.getY() + 0.5D, targetPos.getZ() + 0.5D);
        
        // Calculate a direction vector based on the raytrace result's hitVec and the entity's position
        Vector3d dirVec = entityVec.subtract(targetVec);
        Direction dir = Direction.getFacingFromVector(dirVec.x, dirVec.y, dirVec.z);
        
        return new BlockRayTraceResult(entityResult.getHitVec(), dir, entityPos, false);
    }
    
    /**
     * Determine whether the source block has an unobstructed line of sight to the target block.
     * 
     * @param world the world in which to test
     * @param source the position of the source block
     * @param target the position of the target block
     * @return true if the source block has an unobstructed line of sight to the target block, false otherwise
     */
    public static boolean hasLineOfSight(@Nullable World world, @Nullable BlockPos source, @Nullable BlockPos target) {
        if (world == null || source == null || target == null) {
            return false;
        }
        
        Vector3d startVec = new Vector3d(source.getX() + 0.5D, source.getY() + 0.5D, source.getZ() + 0.5D);
        Vector3d endVec = new Vector3d(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D);
        EntitylessRayTraceContext context = new EntitylessRayTraceContext(world, startVec, endVec, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.ANY);
        BlockRayTraceResult result = RayTraceUtils.rayTraceBlocksIgnoringSource(context);

        if (result == null || result.getType() == RayTraceResult.Type.MISS) {
            return true;
        } else if (result.getType() == RayTraceResult.Type.BLOCK) {
            return target.equals(result.getPos());
        } else {
            return false;
        }
    }
    
    /**
     * @see {@link net.minecraft.world.IBlockReader#rayTraceBlocks(RayTraceContext)}
     */
    protected static BlockRayTraceResult rayTraceBlocksIgnoringSource(EntitylessRayTraceContext context) {
        return iterateRayTrace(context, RayTraceUtils::doRayTraceCheck, RayTraceUtils::createMiss);
    }
    
    protected static BlockRayTraceResult doRayTraceCheck(EntitylessRayTraceContext context, BlockPos pos) {
        IBlockReader world = context.getWorld();
        BlockState blockState = world.getBlockState(pos);
        FluidState fluidState = world.getFluidState(pos);
        Vector3d startVec = context.getStartVec();
        Vector3d endVec = context.getEndVec();
        VoxelShape blockShape = context.getBlockShape(blockState, world, pos);
        BlockRayTraceResult blockResult = RayTraceUtils.doCollisionCheck(world, startVec, endVec, pos, blockShape, blockState);
        VoxelShape fluidShape = context.getFluidShape(fluidState, world, pos);
        BlockRayTraceResult fluidResult = fluidShape.rayTrace(startVec, endVec, pos);
        double blockDistanceSq = blockResult == null ? Double.MAX_VALUE : startVec.squareDistanceTo(blockResult.getHitVec());
        double fluidDistanceSq = fluidResult == null ? Double.MAX_VALUE : startVec.squareDistanceTo(fluidResult.getHitVec());
        return blockDistanceSq <= fluidDistanceSq ? blockResult : fluidResult;
    }
    
    protected static BlockRayTraceResult createMiss(EntitylessRayTraceContext context) {
    	Vector3d endVec = context.getEndVec();
    	Vector3d delta = context.getStartVec().subtract(endVec);
        return BlockRayTraceResult.createMiss(endVec, Direction.getFacingFromVector(delta.x, delta.y, delta.z), new BlockPos(endVec));
    }
    
    /**
     * @see {@link net.minecraft.world.IBlockReader#rayTraceBlocks(Vector3d, Vector3d, BlockPos, VoxelShape, BlockState)}
     */
    @Nullable
    protected static BlockRayTraceResult doCollisionCheck(IBlockReader world, Vector3d startVec, Vector3d endVec, BlockPos iteratedPos, VoxelShape iteratedShape, BlockState iteratedState) {
        BlockRayTraceResult result = iteratedShape.rayTrace(startVec, endVec, iteratedPos);
        if (result != null) {
            BlockRayTraceResult faceResult = iteratedState.getRayTraceShape(world, iteratedPos).rayTrace(startVec, endVec, iteratedPos);
            if (faceResult != null && (faceResult.getHitVec().subtract(startVec).lengthSquared() < result.getHitVec().subtract(startVec).lengthSquared())) {
                return result.withFace(faceResult.getFace());
            }
        }
        return result;
    }
    
    /**
     * @see {@link net.minecraft.world.IBlockReader#func_217300_a(RayTraceContext, BiFunction, Function)}
     */
    protected static BlockRayTraceResult iterateRayTrace(EntitylessRayTraceContext context, BiFunction<EntitylessRayTraceContext, BlockPos, BlockRayTraceResult> checkFunc, Function<EntitylessRayTraceContext, BlockRayTraceResult> missFunc) {
    	Vector3d startVec = context.getStartVec();
    	Vector3d endVec = context.getEndVec();
        if (startVec.equals(endVec)) {
            return missFunc.apply(context);
        } else {
            double adjEndX = MathHelper.lerp(-1.0E-7D, endVec.x, startVec.x);
            double adjEndY = MathHelper.lerp(-1.0E-7D, endVec.y, startVec.y);
            double adjEndZ = MathHelper.lerp(-1.0E-7D, endVec.z, startVec.z);
            double adjStartX = MathHelper.lerp(-1.0E-7D, startVec.x, endVec.x);
            double adjStartY = MathHelper.lerp(-1.0E-7D, startVec.y, endVec.y);
            double adjStartZ = MathHelper.lerp(-1.0E-7D, startVec.z, endVec.z);
            int adjStartXFloor = MathHelper.floor(adjStartX);
            int adjStartYFloor = MathHelper.floor(adjStartY);
            int adjStartZFloor = MathHelper.floor(adjStartZ);
            BlockPos.Mutable mbp = new BlockPos.Mutable(adjStartXFloor, adjStartYFloor, adjStartZFloor);

            double deltaX = adjEndX - adjStartX;
            double deltaY = adjEndY - adjStartY;
            double deltaZ = adjEndZ - adjStartZ;
            int signDeltaX = MathHelper.signum(deltaX);
            int signDeltaY = MathHelper.signum(deltaY);
            int signDeltaZ = MathHelper.signum(deltaZ);
            double d9 = signDeltaX == 0 ? Double.MAX_VALUE : (double)signDeltaX / deltaX;
            double d10 = signDeltaY == 0 ? Double.MAX_VALUE : (double)signDeltaY / deltaY;
            double d11 = signDeltaZ == 0 ? Double.MAX_VALUE : (double)signDeltaZ / deltaZ;
            double d12 = d9 * (signDeltaX > 0 ? 1.0D - MathHelper.frac(adjStartX) : MathHelper.frac(adjStartX));
            double d13 = d10 * (signDeltaY > 0 ? 1.0D - MathHelper.frac(adjStartY) : MathHelper.frac(adjStartY));
            double d14 = d11 * (signDeltaZ > 0 ? 1.0D - MathHelper.frac(adjStartZ) : MathHelper.frac(adjStartZ));
            
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
                
                BlockRayTraceResult result = checkFunc.apply(context, mbp.setPos(adjStartXFloor, adjStartYFloor, adjStartZFloor));
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
        private final IBlockReader world;
        private final Vector3d startVec;
        private final Vector3d endVec;
        private final RayTraceContext.BlockMode blockMode;
        private final RayTraceContext.FluidMode fluidMode;

        public EntitylessRayTraceContext(IBlockReader world, Vector3d startVec, Vector3d endVec, RayTraceContext.BlockMode blockMode, RayTraceContext.FluidMode fluidMode) {
            this.world = world;
            this.startVec = startVec;
            this.endVec = endVec;
            this.blockMode = blockMode;
            this.fluidMode = fluidMode;
        }
        
        public IBlockReader getWorld() {
            return this.world;
        }
        
        public Vector3d getStartVec() {
            return this.startVec;
        }
        
        public Vector3d getEndVec() {
            return this.endVec;
        }
        
        public VoxelShape getBlockShape(BlockState state, IBlockReader world, BlockPos pos) {
            return this.blockMode.get(state, world, pos, ISelectionContext.dummy());
        }
        
        public VoxelShape getFluidShape(FluidState state, IBlockReader world, BlockPos pos) {
            return this.fluidMode.test(state) ? state.getShape(world, pos) : VoxelShapes.empty();
        }
    }
}
