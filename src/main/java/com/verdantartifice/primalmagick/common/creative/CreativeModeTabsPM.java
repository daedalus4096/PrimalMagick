package com.verdantartifice.primalmagick.common.creative;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemsPM;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod creative tab.  Also tracks items to be populated into the tab during subsequent events.
 * 
 * @author Daedalus4096
 */
public class CreativeModeTabsPM {
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PrimalMagick.MODID);
    private static final List<Supplier<? extends ItemLike>> TAB_CONTENTS = new ArrayList<>();
    
    public static void init() {
        TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static void registerTabItem(Supplier<? extends ItemLike> itemSupplier) {
        TAB_CONTENTS.add(itemSupplier);
    }
    
    public static List<Supplier<? extends ItemLike>> getRegisteredTabItems() {
        return Collections.unmodifiableList(TAB_CONTENTS);
    }
    
    // Register mod creative tab
    public static final RegistryObject<CreativeModeTab> TAB = TABS.register(PrimalMagick.MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.primalmagick"))
            .icon(() -> new ItemStack(ItemsPM.GRIMOIRE.get()))
            .build());
}
