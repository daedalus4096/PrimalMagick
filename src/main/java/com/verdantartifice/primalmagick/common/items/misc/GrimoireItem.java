package com.verdantartifice.primalmagick.common.items.misc;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenGrimoireScreenPacket;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

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
        if (!worldIn.isClientSide && playerIn instanceof ServerPlayer serverPlayer) {
            StatsManager.incrementValue(playerIn, StatsPM.GRIMOIRE_READ);
            PacketHandler.sendToPlayer(new OpenGrimoireScreenPacket(), serverPlayer);
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
    }
}
