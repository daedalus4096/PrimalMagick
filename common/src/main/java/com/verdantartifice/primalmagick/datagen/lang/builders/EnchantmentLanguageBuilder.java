package com.verdantartifice.primalmagick.datagen.lang.builders;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Helper for specifying enchantment-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class EnchantmentLanguageBuilder extends AbstractLanguageBuilder<ResourceKey<Enchantment>, EnchantmentLanguageBuilder> {
    public EnchantmentLanguageBuilder(ResourceKey<Enchantment> enchKey, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(enchKey, () -> Util.makeDescriptionId("enchantment", enchKey.location()), untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(Registries.ENCHANTMENT, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(ResourceKey<Enchantment> base) {
        return Objects.requireNonNull(base).location();
    }

    public EnchantmentLanguageBuilder description(String value) {
        this.add(this.getKey("desc"), value);
        return this;
    }
    
    public EnchantmentLanguageBuilder fullRuneText(String value) {
        this.add(this.getKey("rune_enchantment", "text"), value);
        return this;
    }
    
    public EnchantmentLanguageBuilder partialRuneText(String value) {
        this.add(this.getKey("rune_enchantment", "partial_text"), value);
        return this;
    }
}
