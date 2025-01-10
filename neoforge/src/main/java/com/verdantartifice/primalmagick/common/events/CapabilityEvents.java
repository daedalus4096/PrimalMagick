package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.capabilities.CapabilitiesNeoforge;
import com.verdantartifice.primalmagick.common.capabilities.IItemHandlerPM;
import com.verdantartifice.primalmagick.common.tiles.BlockEntityTypesPM;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class CapabilityEvents {
    @SubscribeEvent
    public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.AUTO_CHARGER.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.CALCINATOR.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(CapabilitiesNeoforge.RESEARCH_CACHE, BlockEntityTypesPM.CALCINATOR.get(), (be, context) -> be.getUncachedTileResearchCache());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.CARVED_BOOKSHELF.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.CONCOCTER.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(CapabilitiesNeoforge.RESEARCH_CACHE, BlockEntityTypesPM.CONCOCTER.get(), (be, context) -> be.getUncachedTileResearchCache());
        event.registerBlockEntity(CapabilitiesNeoforge.MANA_STORAGE, BlockEntityTypesPM.CONCOCTER.get(), (be, context) -> be.getUncachedManaStorage());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.DISSOLUTION_CHAMBER.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(CapabilitiesNeoforge.MANA_STORAGE, BlockEntityTypesPM.DISSOLUTION_CHAMBER.get(), (be, context) -> be.getUncachedManaStorage());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.ESSENCE_CASK.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.ESSENCE_FURNACE.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.ESSENCE_TRANSMUTER.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(CapabilitiesNeoforge.RESEARCH_CACHE, BlockEntityTypesPM.ESSENCE_TRANSMUTER.get(), (be, context) -> be.getUncachedTileResearchCache());
        event.registerBlockEntity(CapabilitiesNeoforge.MANA_STORAGE, BlockEntityTypesPM.ESSENCE_TRANSMUTER.get(), (be, context) -> be.getUncachedManaStorage());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.HONEY_EXTRACTOR.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(CapabilitiesNeoforge.MANA_STORAGE, BlockEntityTypesPM.HONEY_EXTRACTOR.get(), (be, context) -> be.getUncachedManaStorage());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.INFERNAL_FURNACE.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(CapabilitiesNeoforge.MANA_STORAGE, BlockEntityTypesPM.INFERNAL_FURNACE.get(), (be, context) -> be.getUncachedManaStorage());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.MANA_BATTERY.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.OFFERING_PEDESTAL.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.RESEARCH_TABLE.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.RITUAL_ALTAR.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.RITUAL_LECTERN.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.SANGUINE_CRUCIBLE.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.SCRIBE_TABLE.get(), (be, face) -> cast(be.getRawItemHandler(face)));
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BlockEntityTypesPM.WAND_CHARGER.get(), (be, face) -> cast(be.getRawItemHandler(face)));
    }

    private static IItemHandler cast(final IItemHandlerPM handler) {
        if (handler instanceof IItemHandler nfHandler) {
            return nfHandler;
        } else {
            return null;
        }
    }
}
