package com.verdantartifice.primalmagick.common.creative;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

/**
 * Object that registers an item's supplier into a creative tab.
 * 
 * @author Daedalus4096
 */
public class ItemSupplierTabRegistration implements ICreativeTabRegistration {
    protected final Supplier<? extends ItemLike> itemSupplier;
    
    public ItemSupplierTabRegistration(Supplier<? extends ItemLike> itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    @Override
    public void register(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        output.accept(this.itemSupplier.get());
    }
}
