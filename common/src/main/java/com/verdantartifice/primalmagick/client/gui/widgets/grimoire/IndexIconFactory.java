package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import net.minecraft.world.item.Item;

/**
 * Factory class for research entry index icons.
 * 
 * @author Daedalus4096
 */
public class IndexIconFactory {
    public static AbstractIndexIcon fromEntryIcon(IconDefinition data, boolean large) {
        if (data == null) {
            return ItemIndexIcon.of(ItemsPM.GRIMOIRE.get(), large);
        } else if (data.isItem()) {
            Item item = data.asItem();
            return ItemIndexIcon.of(item == null ? ItemsPM.GRIMOIRE.get() : item, large);
        } else {
            return GenericIndexIcon.of(data.getLocation(), large);
        }
    }
}
