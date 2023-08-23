package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.client.util.TooltipHelper;
import com.verdantartifice.primalmagick.common.concoctions.ConcoctionType;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
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
    public String getBuilderKey() {
        return ResourceKey.create(Registries.ITEM, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(Item base) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(base));
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
    
    public ItemLanguageBuilder coloredName(DyeColor color, String value) {
        this.add(this.getKey(color.getName()), value);
        return this;
    }
    
    public ItemLanguageBuilder concoctionName(ConcoctionType type, Potion potion, String name) {
        return this.concoctionName(type, ForgeRegistries.POTIONS.getKey(potion).getPath(), name);
    }
    
    public ItemLanguageBuilder concoctionName(ConcoctionType type, String effectName, String name) {
        this.add(this.getKey(type.getSerializedName(), "effect", effectName), name);
        return this;
    }
}
