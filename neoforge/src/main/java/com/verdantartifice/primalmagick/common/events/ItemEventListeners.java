package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ArcanometerISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HallowsteelShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.HexiumShieldISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.ManaFontISTER;
import com.verdantartifice.primalmagick.client.renderers.itemstack.PrimaliteShieldISTER;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.function.Supplier;

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
        event.registerItem(makeCustomRendererExtension(ArcanometerISTER::new), ItemsPM.ARCANOMETER.get());
        event.registerItem(makeCustomRendererExtension(ManaFontISTER::new), ManaFontBlockItem.getAllFonts().toArray(ManaFontBlockItem[]::new));
        event.registerItem(makeCustomRendererExtension(PrimaliteShieldISTER::new), ItemsPM.PRIMALITE_SHIELD.get());
        event.registerItem(makeCustomRendererExtension(HexiumShieldISTER::new), ItemsPM.HEXIUM_SHIELD.get());
        event.registerItem(makeCustomRendererExtension(HallowsteelShieldISTER::new), ItemsPM.HALLOWSTEEL_SHIELD.get());
    }

    protected static IClientItemExtensions makeCustomRendererExtension(Supplier<BlockEntityWithoutLevelRenderer> supplier) {
        return new IClientItemExtensions() {
            final BlockEntityWithoutLevelRenderer renderer = supplier.get();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        };
    }
}
