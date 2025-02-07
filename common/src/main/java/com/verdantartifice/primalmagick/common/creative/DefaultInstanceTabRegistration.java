package com.verdantartifice.primalmagick.common.creative;

import com.verdantartifice.primalmagick.common.items.IEnchantedByDefault;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

/**
 * Object that registers an item's default instance into a creative tab.
 * 
 * @author Daedalus4096
 */
public class DefaultInstanceTabRegistration implements ICreativeTabRegistration {
    protected final Supplier<? extends ItemLike> itemSupplier;
    
    public DefaultInstanceTabRegistration(Supplier<? extends ItemLike> itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    @Override
    public void register(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
        Item item = this.itemSupplier.get().asItem();
        ItemStack stack = item instanceof IEnchantedByDefault enchItem ? enchItem.getDefaultEnchantedInstance(params.holders()) : item.getDefaultInstance();
        output.accept(stack);
    }
}
