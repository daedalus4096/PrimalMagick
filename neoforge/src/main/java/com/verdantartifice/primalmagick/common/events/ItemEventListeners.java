package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ArcanometerISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaFontISTER;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Neoforge listeners for item related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class ItemEventListeners {
    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        InteractionResult result = ItemEvents.onItemRightClick(event.getItemStack(), event.getEntity(), event.getLevel());
        if (result.consumesAction()) {
            event.setCanceled(true);
            event.setCancellationResult(result);
        }
    }

    // TODO Does this need to subscribe to the mod event bus?
    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            final BlockEntityWithoutLevelRenderer renderer = new ArcanometerISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        }, ItemsPM.ARCANOMETER.get());
        event.registerItem(new IClientItemExtensions() {
            final BlockEntityWithoutLevelRenderer renderer = new ManaFontISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        }, ManaFontBlockItem.getAllFonts().toArray(ManaFontBlockItem[]::new));
    }
}
