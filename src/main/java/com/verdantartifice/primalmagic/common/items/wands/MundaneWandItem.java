package com.verdantartifice.primalmagic.common.items.wands;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.base.ItemPM;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class MundaneWandItem extends ItemPM {
    public MundaneWandItem() {
        super("mundane_wand", new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1));
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            PrimalMagic.LOGGER.info("Using mundane wand");
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
