package com.verdantartifice.primalmagick.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenGrimoireScreenPacket;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Item defintion for a grimoire.  The grimoire serves as a research browser and is the primary mechanism of
 * progression in the mod.
 * 
 * @author Daedalus4096
 */
public class GrimoireItem extends Item {
    private final boolean unlockAll;
    
    public GrimoireItem(boolean unlockAll, Item.Properties properties) {
        super(properties);
        this.unlockAll = unlockAll;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!worldIn.isClientSide && playerIn instanceof ServerPlayer serverPlayer) {
            if (this.unlockAll) {
                ResearchManager.forceGrantAll(playerIn);
            }
            StatsManager.incrementValue(playerIn, StatsPM.GRIMOIRE_READ);
            PacketHandler.sendToPlayer(new OpenGrimoireScreenPacket(), serverPlayer);
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (this.unlockAll) {
            pTooltipComponents.add(Component.translatable(this.getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }
}
