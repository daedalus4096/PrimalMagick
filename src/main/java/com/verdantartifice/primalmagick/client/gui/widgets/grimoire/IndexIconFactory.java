package com.verdantartifice.primalmagick.client.gui.widgets.grimoire;

import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Factory class for research entry index icons.
 * 
 * @author Daedalus4096
 */
public class IndexIconFactory {
    public static AbstractIndexIcon fromEntryIcon(ResearchEntry.Icon data, boolean large) {
        if (data == null) {
            return ItemIndexIcon.of(ItemsPM.GRIMOIRE.get(), large);
        } else if (data.isItem()) {
            Item item = ForgeRegistries.ITEMS.getValue(data.getLocation());
            return ItemIndexIcon.of(item == null ? ItemsPM.GRIMOIRE.get() : item, large);
        } else {
            return GenericIndexIcon.of(data.getLocation(), large);
        }
    }
}
