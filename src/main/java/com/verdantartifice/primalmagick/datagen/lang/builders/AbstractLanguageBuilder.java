package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.base.Preconditions;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper for specifying localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractLanguageBuilder<T, U extends AbstractLanguageBuilder<T, U>> implements ILanguageBuilder {
    protected final T base;
    protected final Supplier<String> keyExtractor;
    protected final Consumer<ILanguageBuilder> untracker;
    protected final BiConsumer<String, String> adder;
    protected final Map<String, String> data = new TreeMap<>();
    
    public AbstractLanguageBuilder(T base, Supplier<String> keyExtractor, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        this.base = Preconditions.checkNotNull(base);
        this.keyExtractor = Preconditions.checkNotNull(keyExtractor);
        this.untracker = Preconditions.checkNotNull(untracker);
        this.adder = Preconditions.checkNotNull(adder);
    }
    
    @SuppressWarnings("unchecked")
    private U self() {
        return (U)this;
    }

    protected ResourceLocation getBaseRegistryKey() {
        return this.getBaseRegistryKey(this.base);
    }
    
    protected abstract ResourceLocation getBaseRegistryKey(T base);
    
    protected void add(String key, String value) {
        if (this.data.containsKey(key)) {
            throw new IllegalStateException("Duplicate translation key " + key + " for registered entry " + this.getBaseRegistryKey());
        } else {
            this.data.put(key, value);
        }
    }
    
    @Override
    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    @Override
    public void build() {
        this.data.forEach((key, value) -> this.adder.accept(key, value));
        this.untracker.accept(this);
    }
    
    protected String getKey() {
        return this.keyExtractor.get();
    }
    
    protected String getKey(String... suffixes) {
        return this.getKey(List.of(suffixes));
    }
    
    protected String getKey(List<String> suffixes) {
        List<String> tokens = new ArrayList<>();
        tokens.add(this.keyExtractor.get());
        tokens.addAll(suffixes);
        return String.join(".", tokens);
    }

    public U name(String value) {
        this.add(this.getKey(), value);
        return self();
    }
}
