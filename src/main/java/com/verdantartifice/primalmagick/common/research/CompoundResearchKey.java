package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.mojang.serialization.Codec;

import net.minecraft.world.entity.player.Player;

/**
 * Data object identifying multiple research entries/stages at once.  Comprised of multiple simple
 * research keys, and may be satisfied by either all of them or just one.
 * 
 * @author Daedalus4096
 */
public class CompoundResearchKey implements IResearchKey {
    public static final CompoundResearchKey EMPTY = new CompoundResearchKey(true);
    
    public static final Codec<CompoundResearchKey> CODEC = Codec.STRING.xmap(CompoundResearchKey::parse, CompoundResearchKey::toString);

    protected final List<SimpleResearchKey> keys;
    protected final boolean requireAll;
    
    protected CompoundResearchKey(boolean requireAll) {
        this.requireAll = requireAll;
        this.keys = ImmutableList.of();
    }
    
    protected CompoundResearchKey(boolean requireAll, @Nonnull SimpleResearchKey key) {
        this.requireAll = requireAll;
        this.keys = ImmutableList.of(key);
    }
    
    protected CompoundResearchKey(boolean requireAll, @Nonnull SimpleResearchKey... keys) {
        this.requireAll = requireAll;
        this.keys = ImmutableList.<SimpleResearchKey>builder().add(keys).build();
    }
    
    protected CompoundResearchKey(boolean requireAll, @Nonnull List<SimpleResearchKey> keys) {
        this.requireAll = requireAll;
        this.keys = ImmutableList.<SimpleResearchKey>builder().addAll(keys).build();
    }
    
    public static CompoundResearchKey parse(String keyStr) {
        if (keyStr == null) {
            throw new IllegalArgumentException("Key string may not be null");
        } else if (keyStr.isEmpty()) {
            return EMPTY;
        } else if (keyStr.contains("&&")) {
            if (keyStr.contains("||")) {
                throw new IllegalArgumentException("Research key may contain && or || but not both");
            }
            
            // Parse all tokens of the string into simple research keys, filtering out failures, then collect them into a compound research key
            return CompoundResearchKey.from(true, parseKeys(keyStr, "&&"));
        } else if (keyStr.contains("||")) {
            // Parse all tokens of the string into simple research keys, filtering out failures, then collect them into a compound research key
            return CompoundResearchKey.from(false, parseKeys(keyStr, "||"));
        } else {
            return CompoundResearchKey.from(SimpleResearchKey.parse(keyStr));
        }
    }
    
    protected static List<SimpleResearchKey> parseKeys(String keyStr, String glue) {
        return Arrays.asList(keyStr.split(glue)).stream().filter(Objects::nonNull).map(SimpleResearchKey::parse).toList();
    }
    
    public static Builder builder(boolean requireAll) {
        return new Builder(requireAll);
    }
    
    public static CompoundResearchKey parse(@Nullable JsonArray jsonArray) throws Exception {
        // When parsing from a JSON file instead of an ad-hoc string, always require that all SRKs be satisfied
        if (jsonArray == null) {
            throw new IllegalArgumentException("JSON data may not be null");
        }
        ImmutableList.Builder<SimpleResearchKey> keyBuilder = ImmutableList.builder();
        jsonArray.asList().stream().map(e -> SimpleResearchKey.parse(e.getAsString())).forEach(keyBuilder::add);
        return new CompoundResearchKey(true, keyBuilder.build());
    }
    
    public static CompoundResearchKey from(@Nullable SimpleResearchKey simpleKey) {
        if (simpleKey == null) {
            throw new IllegalArgumentException("Inner key may not be null");
        } else {
            return new CompoundResearchKey(true, simpleKey);
        }
    }
    
    public static Optional<CompoundResearchKey> from(@Nonnull Optional<SimpleResearchKey> simpleKeyOpt) {
        return simpleKeyOpt.isPresent() ? Optional.of(new CompoundResearchKey(true, simpleKeyOpt.get())) : Optional.empty();
    }
    
    public static CompoundResearchKey from(boolean requireAll, SimpleResearchKey... simpleKeys) {
        return new CompoundResearchKey(requireAll, Arrays.stream(simpleKeys).filter(Objects::nonNull).toList());
    }
    
    public static CompoundResearchKey from(boolean requireAll, List<SimpleResearchKey> simpleKeys) {
        return new CompoundResearchKey(requireAll, simpleKeys.stream().filter(Objects::nonNull).toList());
    }
    
    public static CompoundResearchKey from(boolean requireAll, String... keyStrs) {
        return new CompoundResearchKey(requireAll, Arrays.stream(keyStrs).filter(Objects::nonNull).map(SimpleResearchKey::parse).toList());
    }
    
    @Nonnull
    public CompoundResearchKey copy() {
        // Make a deep copy of the compound research key
        return new CompoundResearchKey(this.requireAll, this.keys.stream().map(SimpleResearchKey::copy).toList());
    }
    
    @Nonnull
    public List<SimpleResearchKey> getKeys() {
        return this.keys;
    }
    
    public boolean getRequireAll() {
        return this.requireAll;
    }
    
    @Override
    public boolean isEmpty() {
        return this.keys.isEmpty() || this.keys.stream().allMatch(SimpleResearchKey::isEmpty);
    }
    
    @Override
    public boolean isKnownBy(@Nullable Player player) {
        if (this.equals(EMPTY)) {
            return true;
        }
        if (this.requireAll) {
            // If requireAll is true, the CRK is only known if all contained SRKs are known
            return this.keys.stream().allMatch(simpleKey -> simpleKey.isKnownBy(player));
        } else {
            // Otherwise, the CRK is known if any of the contained SRKs are known
            return this.keys.stream().anyMatch(simpleKey -> simpleKey.isKnownBy(player));
        }
    }
    
    @Override
    public boolean isKnownByStrict(@Nullable Player player) {
        if (this.equals(EMPTY)) {
            return true;
        }
        if (this.requireAll) {
            // If requireAll is true, the CRK is only known if all contained SRKs are known
            return this.keys.stream().allMatch(simpleKey -> simpleKey.isKnownByStrict(player));
        } else {
            // Otherwise, the CRK  is known if any of the contained SRKs are known
            return this.keys.stream().anyMatch(simpleKey -> simpleKey.isKnownByStrict(player));
        }
    }
    
    public boolean contains(@Nullable SimpleResearchKey simpleKey) {
        if (simpleKey == null) {
            return false;
        } else {
            return this.keys.contains(simpleKey);
        }
    }
    
    public boolean containsStripped(@Nullable SimpleResearchKey simpleKey) {
        if (simpleKey == null) {
            return false;
        } else {
            // Return true if the given SRK is one of this CRK's keys, regardless of any stage requirements given in either
            return this.keys.stream().map((k) -> k.stripStage()).toList().contains(simpleKey.stripStage());
        }
    }
    
    @Override
    public String toString() {
        return String.join(this.requireAll ? "&&" : "||", this.keys.stream().map(k -> k.toString()).toList());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((keys == null) ? 0 : keys.hashCode());
        result = prime * result + (requireAll ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CompoundResearchKey other = (CompoundResearchKey) obj;
        if (keys == null) {
            if (other.keys != null)
                return false;
        } else if (!keys.equals(other.keys))
            return false;
        if (requireAll != other.requireAll)
            return false;
        return true;
    }
    
    public static class Builder {
        private final boolean requireAll;
        private final List<SimpleResearchKey> simpleKeys = new ArrayList<>();
        
        protected Builder(boolean requireAll) {
            this.requireAll = requireAll;
        }
        
        public Builder add(SimpleResearchKey key) {
            this.simpleKeys.add(key);
            return this;
        }
        
        public Builder add(Optional<SimpleResearchKey> keyOpt) {
            return this.add(keyOpt.orElseThrow());
        }
        
        public CompoundResearchKey build() {
            return new CompoundResearchKey(this.requireAll, this.simpleKeys);
        }
    }
}
