package com.verdantartifice.primalmagick.common.creative;

import java.util.function.Supplier;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

/**
 * Object that registers one or more objects into a creative tab according to a given function.
 * Typically used for items that register multiple instances per item, such as potions.
 * 
 * @author Daedalus4096
 */
public class CustomTabRegistration implements ICreativeTabRegistration {
    protected final Supplier<? extends ItemLike> itemSupplier;
    protected final CreativeModeTabsPM.CustomTabRegistrar registrar;
    
    public CustomTabRegistration(Supplier<? extends ItemLike> itemSupplier, CreativeModeTabsPM.CustomTabRegistrar registrar) {
        this.itemSupplier = itemSupplier;
        this.registrar = registrar;
    }

    @Override
    public void register(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        registrar.accept(params, output, this.itemSupplier);
    }
}
