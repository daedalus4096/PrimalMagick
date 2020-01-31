package com.verdantartifice.primalmagic.common.util;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
     * optionally excluding a specific entity.
     * 
     * @param world the world to be searched
     * @param center the center point of the area to search
     * @param exclude an entity to exclude from the search results
     * @param entityClass the type of entity to search for
     * @param range the radius in which to search
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull World world, @Nonnull BlockPos center, @Nullable Entity exclude, @Nonnull Class<? extends T> entityClass, double range) {
        return getEntitiesInRange(world, center.getX() + 0.5D, center.getY() + 0.5D, center.getZ() + 0.5D, exclude, entityClass, range);
    }

    /**
     * Get a list of all entities of the given type within a given distance of the given center point,
     * optionally excluding a specific entity.
     * 
     * @param world the world to be searched
     * @param center the center point of the area to search
     * @param exclude an entity to exclude from the search results
     * @param entityClass the type of entity to search for
     * @param range the radius in which to search
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull World world, @Nonnull Vec3d center, @Nullable Entity exclude, @Nonnull Class<? extends T> entityClass, double range) {
        return getEntitiesInRange(world, center.getX(), center.getY(), center.getZ(), exclude, entityClass, range);
    }

    /**
     * Get a list of all entities of the given type within a given distance of the given center point,
     * optionally excluding a specific entity.
     * 
     * @param world the world to be searched
     * @param x the x-coordinate of the center point of the area to search
     * @param y the y-coordinate of the center point of the area to search
     * @param z the z-coordinate of the center point of the area to search
     * @param exclude an entity to exclude from the search results
     * @param entityClass the type of entity to search for
     * @param range the radius in which to search
     * @return a list of all such entities in range
     */
    public static <T extends Entity> List<T> getEntitiesInRange(@Nonnull World world, double x, double y, double z, @Nullable Entity exclude, @Nonnull Class<? extends T> entityClass, double range) {
        List<T> retVal = world.getEntitiesWithinAABB(entityClass, new AxisAlignedBB(x, y, z, x, y, z).grow(range, range, range));
        if (exclude != null) {
            retVal = retVal.stream().filter(e -> e.getEntityId() != exclude.getEntityId()).collect(Collectors.toList());
        }
        return retVal;
    }
}
