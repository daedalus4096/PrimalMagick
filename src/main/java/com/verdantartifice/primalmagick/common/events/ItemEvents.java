package com.verdantartifice.primalmagick.common.events;

import java.util.Comparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.misc.OpenEnchantedBookScreenPacket;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for item related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class ItemEvents {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        InteractionResult result = InteractionResult.PASS;
        ItemStack stack = event.getItemStack();
        
        if (stack.is(Items.ENCHANTED_BOOK)) {
            result = handleEnchantedBookRightClick(stack, event.getEntity(), event.getLevel());
        }
        
        if (result.consumesAction()) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
    
    private static InteractionResult handleEnchantedBookRightClick(ItemStack stack, Player player, Level level) {
        // Get the entry for the most powerful enchantment on the book, if any
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            stack.getEnchantments().entrySet().stream().sorted(Comparator.comparing(Object2IntMap.Entry::getIntValue)).findFirst().ifPresent(entry -> {
                PacketHandler.sendToPlayer(new OpenEnchantedBookScreenPacket(entry.getKey(), player.registryAccess()), serverPlayer);
            });
        }
        return InteractionResult.SUCCESS;
    }
}
