package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.base.Preconditions;
import com.verdantartifice.primalmagick.common.sources.Source;

/**
 * Helper for specifying localizations for groups of objects that only differ in their
 * magickal source, in a structured way.
 * 
 * @author Daedalus4096
 */
public abstract class AbstractSourceMultipliedLanguageBuilder<T, U extends AbstractSourceMultipliedLanguageBuilder<T, U>> implements ILanguageBuilder {
    protected final String builderKey;
    protected final List<T> bases = new ArrayList<>();
    protected final Function<T, String> keyExtractor;
    protected final Function<T, Source> sourceExtractor;
    protected final Function<Source, String> sourceNameMapper;
    protected final Consumer<ILanguageBuilder> untracker;
    protected final BiConsumer<String, String> adder;
    protected final Map<String, String> data = new TreeMap<>();
    
    public AbstractSourceMultipliedLanguageBuilder(String builderKey, List<T> bases, Function<T, String> keyExtractor, Function<T, Source> sourceExtractor,
            Function<Source, String> sourceNameMapper, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        this.builderKey = Preconditions.checkNotNull(builderKey);
        this.bases.addAll(Preconditions.checkNotNull(bases));
        this.keyExtractor = Preconditions.checkNotNull(keyExtractor);
        this.sourceExtractor = Preconditions.checkNotNull(sourceExtractor);
        this.sourceNameMapper = Preconditions.checkNotNull(sourceNameMapper);
        this.untracker = Preconditions.checkNotNull(untracker);
        this.adder = Preconditions.checkNotNull(adder);
    }
    
    @SuppressWarnings("unchecked")
    private U self() {
        return (U)this;
    }

    protected void add(String key, String value) {
        if (this.data.containsKey(key)) {
            throw new IllegalStateException("Duplicate translation key " + key + " for builder " + this.getBuilderKey());
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
    
    protected String getKey(T item) {
        return this.keyExtractor.apply(item);
    }
    
    protected String getKey(T item, String... suffixes) {
        return this.getKey(item, List.of(suffixes));
    }
    
    protected String getKey(T item, List<String> suffixes) {
        List<String> tokens = new ArrayList<>();
        tokens.add(this.keyExtractor.apply(item));
        tokens.addAll(suffixes);
        return String.join(".", tokens);
    }

    public U namePattern(String pattern) {
        this.bases.stream().forEach(base -> this.add(this.getKey(base), String.format(pattern, this.sourceNameMapper.apply(this.sourceExtractor.apply(base)))));
        return self();
    }
}
