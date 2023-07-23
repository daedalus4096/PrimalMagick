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
    private static final List<ICreativeTabRegistration> TAB_REGISTRATIONS = new ArrayList<>();
    
    public static void init() {
        TABS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static void registerSupplier(Supplier<? extends ItemLike> itemSupplier) {
        TAB_REGISTRATIONS.add(new ItemSupplierTabRegistration(itemSupplier));
    }
    
    public static void registerDefaultInstance(Supplier<? extends ItemLike> itemSupplier) {
        TAB_REGISTRATIONS.add(new DefaultInstanceTabRegistration(itemSupplier));
    }
    
    public static void registerCustom(Supplier<? extends ItemLike> itemSupplier, CustomTabRegistrar registrar) {
        TAB_REGISTRATIONS.add(new CustomTabRegistration(itemSupplier, registrar));
    }
    
    public static List<ICreativeTabRegistration> getTabRegistrationEntries() {
        return Collections.unmodifiableList(TAB_REGISTRATIONS);
    }
    
    // Register mod creative tab
    public static final RegistryObject<CreativeModeTab> TAB = TABS.register(PrimalMagick.MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.primalmagick"))
            .icon(() -> new ItemStack(ItemsPM.GRIMOIRE.get()))
            .displayItems((params, output) -> {
                CreativeModeTabsPM.getTabRegistrationEntries().forEach(entry -> entry.register(params, output));
            })
            .build());
    
    @FunctionalInterface
    public interface CustomTabRegistrar {
        void accept(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, Supplier<? extends ItemLike> itemSupplier);
    }
}
