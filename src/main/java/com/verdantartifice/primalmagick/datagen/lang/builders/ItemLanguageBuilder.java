package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.common.util.TooltipHelper;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Helper for specifying item-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class ItemLanguageBuilder extends AbstractLanguageBuilder<Item> {
    public ItemLanguageBuilder(Item item, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(item, item::getDescriptionId, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(Registries.ITEM, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(Item base) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(base));
    }

    public ItemLanguageBuilder name(String value) {
        this.add(this.getKey(), value);
        return this;
    }
    
    public ItemLanguageBuilder tooltip(String value) {
        this.add(this.getKey(TooltipHelper.SUFFIX), value);
        return this;
    }
    
    public ItemLanguageBuilder tooltip(String... values) {
        int index = 1;
        for (String value : values) {
            this.add(this.getKey(TooltipHelper.SUFFIX, Integer.toString(index++)), value);
        }
        return this;
    }
}
