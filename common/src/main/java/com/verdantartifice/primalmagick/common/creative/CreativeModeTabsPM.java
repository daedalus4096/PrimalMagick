package com.verdantartifice.primalmagick.common.creative;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Deferred registry for mod creative tab.  Also tracks items to be populated into the tab during subsequent events.
 * 
 * @author Daedalus4096
 */
public class CreativeModeTabsPM {
    private static final List<ICreativeTabRegistration> TAB_REGISTRATIONS = new ArrayList<>();
    
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

    private static <T extends CreativeModeTab> IRegistryItem<CreativeModeTab, T> register(String name, Supplier<T> tabSupplier) {
        return Services.CREATIVE_MODE_TABS_REGISTRY.register(name, tabSupplier);
    }
    
    // Register mod creative tab
    public static final IRegistryItem<CreativeModeTab, CreativeModeTab> TAB = register(Constants.MOD_ID, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
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
