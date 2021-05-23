package com.verdantartifice.primalmagic.common.items.concoctions;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Definition of a skyglass flask.  Used as a base item for making alchemical concoctions.
 * 
 * @author Daedalus4096
 */
public class SkyglassFlaskItem extends Item {
    public SkyglassFlaskItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        // TODO Gather water from source blocks
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        // TODO Gather water from cauldrons
        return super.onItemUse(context);
    }
}
