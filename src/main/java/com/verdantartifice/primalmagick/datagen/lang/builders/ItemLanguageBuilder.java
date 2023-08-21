package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
public class ItemLanguageBuilder extends AbstractLanguageBuilder<Item, ItemLanguageBuilder> {
    public ItemLanguageBuilder(Item item, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(item, item::getDescriptionId, untracker, adder);
    }

    @Override
    public ResourceKey<?> getKey() {
        return ResourceKey.create(Registries.ITEM, this.getBaseRegistryKey());
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(Item base) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(base));
    }
}
