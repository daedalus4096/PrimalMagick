package com.verdantartifice.primalmagic.common.items.essence;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.item.Item;

public class EssenceItem extends Item {
    protected final EssenceType type;
    protected final Source source;

    public EssenceItem(@Nonnull EssenceType type, @Nonnull Source source) {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(type.getRarity()));
        this.setRegistryName(PrimalMagic.MODID, "essence_" + type.getName() + "_" + source.getTag());
        this.type = type;
        this.source = source;
    }

    public EssenceType getEssenceType() {
        return this.type;
    }
    
    public Source getSource() {
        return this.source;
    }
}
