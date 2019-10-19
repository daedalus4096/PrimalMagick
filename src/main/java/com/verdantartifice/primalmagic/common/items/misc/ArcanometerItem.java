package com.verdantartifice.primalmagic.common.items.misc;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ArcanometerItem extends Item {
    public ArcanometerItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.UNCOMMON));
        this.setRegistryName(PrimalMagic.MODID, "arcanometer");
        this.addPropertyOverride(new ResourceLocation(PrimalMagic.MODID, "scan_state"), (stack, world, entity) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return (float)ArcanometerItem.this.getScanState(stack);
            }
        });
    }
    
    protected int getScanState(ItemStack stack) {
        // TODO stub
        return 0;
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        // TODO Auto-generated method stub
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
