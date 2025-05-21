package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.items.IHasCustomRenderer;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.misc.ManaFontBlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Neoforge listeners for item related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ItemModBusEventListeners {
    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        registerCustomRenderer(event::registerItem, ItemsPM.ARCANOMETER.get());
        ManaFontBlockItem.getAllFonts().forEach(font -> registerCustomRenderer(event::registerItem, font));
        registerCustomRenderer(event::registerItem, ItemsPM.SPELLCRAFTING_ALTAR.get());
        registerCustomRenderer(event::registerItem, ItemsPM.PRIMALITE_SHIELD.get());
        registerCustomRenderer(event::registerItem, ItemsPM.HEXIUM_SHIELD.get());
        registerCustomRenderer(event::registerItem, ItemsPM.HALLOWSTEEL_SHIELD.get());
        registerCustomRenderer(event::registerItem, ItemsPM.PRIMALITE_TRIDENT.get());
        registerCustomRenderer(event::registerItem, ItemsPM.HEXIUM_TRIDENT.get());
        registerCustomRenderer(event::registerItem, ItemsPM.HALLOWSTEEL_TRIDENT.get());
        registerCustomRenderer(event::registerItem, ItemsPM.FORBIDDEN_TRIDENT.get());
        registerCustomRenderer(event::registerItem, ItemsPM.SPELLTOME_APPRENTICE.get());
        registerCustomRenderer(event::registerItem, ItemsPM.SPELLTOME_ADEPT.get());
        registerCustomRenderer(event::registerItem, ItemsPM.SPELLTOME_WIZARD.get());
        registerCustomRenderer(event::registerItem, ItemsPM.SPELLTOME_ARCHMAGE.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MUNDANE_WAND.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MODULAR_WAND.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MODULAR_STAFF.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MANA_RELAY_BASIC.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MANA_RELAY_ENCHANTED.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MANA_RELAY_FORBIDDEN.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MANA_RELAY_HEAVENLY.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MANA_INJECTOR_BASIC.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MANA_INJECTOR_ENCHANTED.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MANA_INJECTOR_FORBIDDEN.get());
        registerCustomRenderer(event::registerItem, ItemsPM.MANA_INJECTOR_HEAVENLY.get());
        registerCustomRenderer(event::registerItem, ItemsPM.PIXIE_HOUSE.get());
    }

    protected static <T extends Item & IHasCustomRenderer> void registerCustomRenderer(BiConsumer<IClientItemExtensions, T> registrar, T item) {
        registrar.accept(makeCustomRendererExtension(item.getCustomRendererSupplier()), item);
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
