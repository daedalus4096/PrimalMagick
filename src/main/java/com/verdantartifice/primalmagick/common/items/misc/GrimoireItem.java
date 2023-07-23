package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Item defintion for a grimoire.  The grimoire serves as a research browser and is the primary mechanism of
 * progression in the mod.
 * 
 * @author Daedalus4096
 */
public class GrimoireItem extends Item {
    public GrimoireItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!worldIn.isClientSide) {
            StatsManager.incrementValue(playerIn, StatsPM.GRIMOIRE_READ);
        }
        if (worldIn.isClientSide && FMLEnvironment.dist == Dist.CLIENT) {
            ClientUtils.openGrimoireScreen();
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
    }
}
