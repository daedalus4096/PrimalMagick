package com.verdantartifice.primalmagick.common.util;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.TeleportArrivalPacket;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;

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
        } else if (entity instanceof Boat) {
            stack = new ItemStack(((Boat)entity).getDropItem());
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
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull Level world, @Nonnull BlockPos center, @Nullable List<Entity> exclude, @Nonnull Class<T> entityClass, double range) {
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
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull Level world, @Nonnull Vec3 center, @Nullable List<Entity> exclude, @Nonnull Class<T> entityClass, double range) {
        return getEntitiesInRange(world, center.x(), center.y(), center.z(), exclude, entityClass, range);
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
     * @param selector the filter predicate to apply to the returned list of entities
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull Level world, @Nonnull Vec3 center, @Nullable List<Entity> exclude, @Nonnull Class<T> entityClass, double range, Predicate<Entity> selector) {
        return getEntitiesInRange(world, center.x(), center.y(), center.z(), exclude, entityClass, range, selector);
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
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull Level world, double x, double y, double z, @Nullable List<Entity> exclude, @Nonnull Class<T> entityClass, double range) {
        return getEntitiesInRange(world, x, y, z, exclude, entityClass, range, EntitySelector.NO_SPECTATORS);
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
     * @param selector the filter predicate to apply to the returned list of entities
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull Level world, double x, double y, double z, @Nullable List<Entity> exclude, @Nonnull Class<T> entityClass, double range, Predicate<Entity> selector) {
        List<T> retVal = world.getEntitiesOfClass(entityClass, new AABB(x, y, z, x, y, z).inflate(range, range, range), selector);
        if (exclude != null) {
            List<Integer> excludeIds = exclude.stream().map(e -> Integer.valueOf(e.getId())).collect(Collectors.toList());
            retVal = retVal.stream().filter(e -> !excludeIds.contains(Integer.valueOf(e.getId()))).collect(Collectors.toList());
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
    public static <T extends Entity> List<T> getEntitiesInRangeSorted(@Nonnull Level world, @Nonnull BlockPos center, @Nullable List<Entity> exclude, @Nonnull Class<T> entityClass, double range) {
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
    public static <T extends Entity> List<T> getEntitiesInRangeSorted(@Nonnull Level world, @Nonnull Vec3 center, @Nullable List<Entity> exclude, @Nonnull Class<T> entityClass, double range) {
        return getEntitiesInRangeSorted(world, center, exclude, entityClass, range, EntitySelector.NO_SPECTATORS);
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
    public static <T extends Entity> List<T> getEntitiesInRangeSorted(@Nonnull Level world, @Nonnull Vec3 center, @Nullable List<Entity> exclude, @Nonnull Class<T> entityClass, double range, Predicate<Entity> selector) {
        List<? extends T> entities = getEntitiesInRange(world, center, exclude, entityClass, range, selector);
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
    public static <T extends Entity> List<T> getEntitiesInRangeSorted(@Nonnull Level world, double x, double y, double z, @Nullable List<Entity> exclude, @Nonnull Class<T> entityClass, double range) {
        return getEntitiesInRangeSorted(world, new Vec3(x, y, z), exclude, entityClass, range);
    }
    
    /**
     * Teleports a player, with special effects, to the given destination point.
     * 
     * @param player the player to be teleported
     * @param world the world in which to teleport
     * @param destination the point to which to teleport
     */
    public static void teleportEntity(LivingEntity player, Level world, Vec3 destination) {
        // Fire an EntityTeleportEvent to allow cancellation or modification of the teleport
        EntityTeleportEvent.EnderEntity event = new EntityTeleportEvent.EnderEntity(player, destination.x, destination.y, destination.z);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            // Show a teleport particle effect at the destination
            if (!world.isClientSide) {
                PacketHandler.sendToAllAround(new TeleportArrivalPacket(event.getTargetX(), event.getTargetY(), event.getTargetZ()), world.dimension(), BlockPos.containing(event.getTarget()), 64.0D);

                if (player instanceof ServerPlayer serverPlayer && serverPlayer.connection.getConnection().isConnected() && player.level() == world && !player.isSleeping()) {
                    if (player.isPassenger()) {
                        player.stopRiding();
                    }
                    
                    // Record teleport distance statistic
                    StatsManager.incrementValue(serverPlayer, StatsPM.DISTANCE_TELEPORTED_CM, (int)(100 * event.getPrev().distanceTo(event.getTarget())));
                    
                    // Do the teleportation
                    player.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                    player.fallDistance = 0.0F;
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
        protected final Vec3 center;
        
        public EntityDistanceComparator(@Nonnull Vec3 center) {
            this.center = center;
        }
        
        @Override
        public int compare(Entity a, Entity b) {
            if (a.equals(b)) {
                return 0;
            } else {
                double distA = this.center.distanceToSqr(a.position());
                double distB = this.center.distanceToSqr(b.position());
                return distA > distB ? 1 : (distA < distB ? -1 : 0);
            }
        }
    }
}
