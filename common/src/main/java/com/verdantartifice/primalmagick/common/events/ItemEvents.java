package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenEnchantedBookScreenPacket;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.Comparator;

/**
 * Handlers for item related events.
 * 
 * @author Daedalus4096
 */
public class ItemEvents {
    public static InteractionResult onItemRightClick(ItemStack stack, Player player, Level level) {
        InteractionResult result = InteractionResult.PASS;
        if (stack.is(Items.ENCHANTED_BOOK)) {
            result = handleEnchantedBookRightClick(stack, player, level);
        }
        return result;
    }
    
    private static InteractionResult handleEnchantedBookRightClick(ItemStack stack, Player player, Level level) {
        // Get the entry for the most powerful enchantment on the book, if any
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().stream().min(Comparator.comparing(Object2IntMap.Entry::getIntValue)).ifPresent(
                    entry -> PacketHandler.sendToPlayer(new OpenEnchantedBookScreenPacket(entry.getKey(), player.registryAccess()), serverPlayer));
        }
        return InteractionResult.SUCCESS;
    }
}
