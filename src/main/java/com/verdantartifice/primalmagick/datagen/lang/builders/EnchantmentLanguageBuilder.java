package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Helper for specifying enchantment-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class EnchantmentLanguageBuilder extends AbstractLanguageBuilder<Enchantment, EnchantmentLanguageBuilder> {
    public EnchantmentLanguageBuilder(Enchantment ench, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(ench, ench::getDescriptionId, untracker, adder);
    }

    @Override
    public String getBuilderKey() {
        return ResourceKey.create(Registries.ENCHANTMENT, this.getBaseRegistryKey()).toString();
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(Enchantment base) {
        return Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(base));
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
