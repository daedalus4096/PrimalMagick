package com.verdantartifice.primalmagick.common.creative;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

/**
 * Object that registers one or more objects into a creative tab according to a given function.
 * Typically used for items that register multiple instances per item, such as potions.
 * 
 * @author Daedalus4096
 */
public class CustomTabRegistration implements ICreativeTabRegistration {
    protected final Supplier<? extends ItemLike> itemSupplier;
    protected final BiConsumer<BuildCreativeModeTabContentsEvent, Supplier<? extends ItemLike>> consumer;
    
    public CustomTabRegistration(Supplier<? extends ItemLike> itemSupplier, BiConsumer<BuildCreativeModeTabContentsEvent, Supplier<? extends ItemLike>> consumer) {
        this.itemSupplier = itemSupplier;
        this.consumer = consumer;
    }

    @Override
    public void register(BuildCreativeModeTabContentsEvent event) {
        consumer.accept(event, this.itemSupplier);
    }
}
