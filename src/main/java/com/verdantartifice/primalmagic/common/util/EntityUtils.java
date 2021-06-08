package com.verdantartifice.primalmagic.common.util;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.TeleportArrivalPacket;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

/**
 * Collection of utility methods pertaining to entities.
 * 
 * @author Daedalus4096
 */
public class EntityUtils {
    /**
     * Get the itemstack with which a non-living entity is placed (e.g. boats).  For item entities,
     * returns whatever itemstack the entity contains.
     * 
     * @param entity the entity whose itemstack to find
     * @return the itemstack with which the entity is placed
     */
    @Nonnull
    public static ItemStack getEntityItemStack(Entity entity) {
        ItemStack stack = ItemStack.EMPTY;
        if (entity instanceof ItemEntity) {
            stack = ((ItemEntity)entity).getItem();
        } else if (entity instanceof BoatEntity) {
            stack = new ItemStack(((BoatEntity)entity).getItemBoat());
        } else if (entity.getType().equals(EntityType.ITEM_FRAME)) {
            stack = new ItemStack(Items.ITEM_FRAME);
        } else if (entity.getType().equals(EntityType.ARMOR_STAND)) {
            stack = new ItemStack(Items.ARMOR_STAND);
        } else if (entity.getType().equals(EntityType.MINECART)) {
            stack = new ItemStack(Items.MINECART);
        } else if (entity.getType().equals(EntityType.CHEST_MINECART)) {
            stack = new ItemStack(Items.CHEST_MINECART);
        } else if (entity.getType().equals(EntityType.FURNACE_MINECART)) {
            stack = new ItemStack(Items.FURNACE_MINECART);
        } else if (entity.getType().equals(EntityType.HOPPER_MINECART)) {
            stack = new ItemStack(Items.HOPPER_MINECART);
        } else if (entity.getType().equals(EntityType.TNT_MINECART)) {
            stack = new ItemStack(Items.TNT_MINECART);
        } else if (entity.getType().equals(EntityType.COMMAND_BLOCK_MINECART)) {
            stack = new ItemStack(Items.COMMAND_BLOCK_MINECART);
        } else if (entity.getType().equals(EntityType.END_CRYSTAL)) {
            stack = new ItemStack(Items.END_CRYSTAL);
        } else if (entity.getType().equals(EntityType.PAINTING)) {
            stack = new ItemStack(Items.PAINTING);
        }
        return stack;
    }
    
    /**
     * Get a list of all entities of the given type within a given distance of the given center point,
     * optionally excluding one or more entities.
     * 
     * @param world the world to be searched
     * @param center the center point of the area to search
     * @param exclude entities to exclude from the search results
     * @param entityClass the type of entity to search for
     * @param range the radius in which to search
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull World world, @Nonnull BlockPos center, @Nullable List<Entity> exclude, @Nonnull Class<? extends T> entityClass, double range) {
        return getEntitiesInRange(world, center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, exclude, entityClass, range);
    }

    /**
     * Get a list of all entities of the given type within a given distance of the given center point,
     * optionally excluding one or more entities.
     * 
     * @param world the world to be searched
     * @param center the center point of the area to search
     * @param exclude entities to exclude from the search results
     * @param entityClass the type of entity to search for
     * @param range the radius in which to search
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull World world, @Nonnull Vector3d center, @Nullable List<Entity> exclude, @Nonnull Class<? extends T> entityClass, double range) {
        return getEntitiesInRange(world, center.getX(), center.getY(), center.getZ(), exclude, entityClass, range);
    }

    /**
     * Get a list of all entities of the given type within a given distance of the given center point,
     * optionally excluding one or more entities.
     * 
     * @param world the world to be searched
     * @param x the x-coordinate of the center point of the area to search
     * @param y the y-coordinate of the center point of the area to search
     * @param z the z-coordinate of the center point of the area to search
     * @param exclude entities to exclude from the search results
     * @param entityClass the type of entity to search for
     * @param range the radius in which to search
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull World world, double x, double y, double z, @Nullable List<Entity> exclude, @Nonnull Class<? extends T> entityClass, double range) {
        List<T> retVal = world.getEntitiesWithinAABB(entityClass, new AxisAlignedBB(x, y, z, x, y, z).grow(range, range, range));
        if (exclude != null) {
            List<Integer> excludeIds = exclude.stream().map(e -> Integer.valueOf(e.getEntityId())).collect(Collectors.toList());
            retVal = retVal.stream().filter(e -> !excludeIds.contains(Integer.valueOf(e.getEntityId()))).collect(Collectors.toList());
        }
        return retVal;
    }
    
    /**
     * Get a list of all entities of the given type within a given distance of the given center point,
     * optionally excluding one or more entities.  The returned list is sorted by distance to the given
     * center point in ascending order.
     * 
     * @param world the world to be searched
     * @param center the center point of the area to search
     * @param exclude entities to exclude from the search results
     * @param entityClass the type of entity to search for
     * @param range the radius in which to search
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRangeSorted(@Nonnull World world, @Nonnull BlockPos center, @Nullable List<Entity> exclude, @Nonnull Class<? extends T> entityClass, double range) {
        return getEntitiesInRangeSorted(world, center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, exclude, entityClass, range);
    }
    
    /**
     * Get a list of all entities of the given type within a given distance of the given center point,
     * optionally excluding one or more entities.  The returned list is sorted by distance to the given
     * center point in ascending order.
     * 
     * @param world the world to be searched
     * @param center the center point of the area to search
     * @param exclude entities to exclude from the search results
     * @param entityClass the type of entity to search for
     * @param range the radius in which to search
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRangeSorted(@Nonnull World world, @Nonnull Vector3d center, @Nullable List<Entity> exclude, @Nonnull Class<? extends T> entityClass, double range) {
        List<T> entities = getEntitiesInRange(world, center, exclude, entityClass, range);
        return entities.stream().sorted(new EntityDistanceComparator(center)).collect(Collectors.toList());
    }
    
    /**
     * Get a list of all entities of the given type within a given distance of the given center point,
     * optionally excluding one or more entities.  The returned list is sorted by distance to the given
     * center point in ascending order.
     * 
     * @param world the world to be searched
     * @param x the x-coordinate of the center point of the area to search
     * @param y the y-coordinate of the center point of the area to search
     * @param z the z-coordinate of the center point of the area to search
     * @param exclude entities to exclude from the search results
     * @param entityClass the type of entity to search for
     * @param range the radius in which to search
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRangeSorted(@Nonnull World world, double x, double y, double z, @Nullable List<Entity> exclude, @Nonnull Class<? extends T> entityClass, double range) {
        return getEntitiesInRangeSorted(world, new Vector3d(x, y, z), exclude, entityClass, range);
    }
    
    /**
     * Teleports a player, with special effects, to the given destination point.
     * 
     * @param player the player to be teleported
     * @param world the world in which to teleport
     * @param destination the point to which to teleport
     */
    public static void teleportEntity(LivingEntity player, World world, Vector3d destination) {
        // Fire an EnderTeleportEvent to allow cancellation or modification of the teleport
        EnderTeleportEvent event = new EnderTeleportEvent(player, destination.x, destination.y, destination.z, 0.0F);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            // Show a teleport particle effect at the destination
            PacketHandler.sendToAllAround(new TeleportArrivalPacket(event.getTargetX(), event.getTargetY(), event.getTargetZ()), world.getDimensionKey(), new BlockPos(event.getTargetX(), event.getTargetY(), event.getTargetZ()), 64.0D);
            
            if (!world.isRemote && player instanceof ServerPlayerEntity) {
                boolean isPlayer = (player instanceof ServerPlayerEntity);
                if ((!isPlayer || ((ServerPlayerEntity)player).connection.getNetworkManager().isChannelOpen()) && player.world == world && !player.isSleeping()) {
                    if (player.isPassenger()) {
                        player.stopRiding();
                    }
                    
                    // Do the teleportation
                    player.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                    player.fallDistance = 0.0F;
                    if (event.getAttackDamage() > 0.0F) {
                        player.attackEntityFrom(DamageSource.FALL, event.getAttackDamage());
                    }
                }
            }
        }
    }
    
    /**
     * A comparator to sort Entity objects by the shortest distance to a source Entity.
     * 
     * @author Daedalus4096
     */
    protected static class EntityDistanceComparator implements Comparator<Entity> {
        protected final Vector3d center;
        
        public EntityDistanceComparator(@Nonnull Vector3d center) {
            this.center = center;
        }
        
        @Override
        public int compare(Entity a, Entity b) {
            if (a.equals(b)) {
                return 0;
            } else {
                double distA = this.center.squareDistanceTo(a.getPositionVec());
                double distB = this.center.squareDistanceTo(b.getPositionVec());
                return distA > distB ? 1 : (distA < distB ? -1 : 0);
            }
        }
    }
}
