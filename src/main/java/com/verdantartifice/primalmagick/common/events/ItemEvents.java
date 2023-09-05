package com.verdantartifice.primalmagick.common.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

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
            // Get the entry for the most powerful enchantment on the book, if any
            EnchantmentHelper.getEnchantments(stack).entrySet().stream().sorted((e1, e2) -> -Integer.compare(e1.getValue(), e2.getValue())).findFirst().ifPresent(entry -> {
                ResourceLocation enchantmentLoc = ForgeRegistries.ENCHANTMENTS.getKey(entry.getKey());
                LOGGER.debug("Attempting to read enchanted book for {}", enchantmentLoc.toString());
            });
        }
        
        if (result.consumesAction()) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }
}
