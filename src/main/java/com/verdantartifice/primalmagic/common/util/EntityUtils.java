package com.verdantartifice.primalmagic.common.util;

import javax.annotation.Nonnull;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class EntityUtils {
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
}
