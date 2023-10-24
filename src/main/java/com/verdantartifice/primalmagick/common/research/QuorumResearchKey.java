package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.world.entity.player.Player;

/**
 * Data object identifying multiple research entries/stages at once.  Comprised of multiple simple
 * research keys, and must be satisfied by completing a given number of them.
 * 
 * @author Daedalus4096
 */
public class QuorumResearchKey implements IResearchKey {
    public static final QuorumResearchKey EMPTY = new QuorumResearchKey();
    
    public static final Codec<QuorumResearchKey> CODEC = Codec.STRING.xmap(QuorumResearchKey::parse, QuorumResearchKey::toString);
    
    protected final List<SimpleResearchKey> keys;
    protected final int requiredCount;
    
    protected QuorumResearchKey() {
        this.requiredCount = 0;
        this.keys = List.of();
    }
    
    protected QuorumResearchKey(SimpleResearchKey key) {
        this.requiredCount = 1;
        this.keys = List.of(key);
    }
    
    protected QuorumResearchKey(int requiredCount, @Nonnull SimpleResearchKey... keys) {
        this.requiredCount = requiredCount;
        this.keys = ImmutableList.<SimpleResearchKey>builder().add(keys).build();
    }
    
    protected QuorumResearchKey(int requiredCount, @Nonnull List<SimpleResearchKey> keys) {
        this.requiredCount = requiredCount;
        this.keys = ImmutableList.<SimpleResearchKey>builder().addAll(keys).build();
    }
    
    public int getRequiredCount() {
        return this.requiredCount;
    }
    
    public List<SimpleResearchKey> getKeys() {
        return this.keys;
    }
    
    public static QuorumResearchKey parse(String keyStr) {
        if (keyStr == null) {
            throw new IllegalArgumentException("Key string may not be null");
        } else if (keyStr.isEmpty()) {
            return EMPTY;
        } else if (keyStr.startsWith("?") && keyStr.contains(":")) {
            String[] pieces = keyStr.split(":");
            if (pieces.length != 2) {
                throw new IllegalArgumentException("Key string '" + keyStr + "' is not a valid quorum research key");
            }
            
            int required;
            try {
                required = Integer.parseInt(pieces[0].substring(1));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Key string '" + keyStr + "' is not a valid quorum research key", e);
            }
            
            List<SimpleResearchKey> newKeys = Arrays.stream(pieces[1].split(",")).filter(Objects::nonNull).map(SimpleResearchKey::parse).toList();
            if (newKeys.isEmpty()) {
                throw new IllegalArgumentException("Key string '" + keyStr + "' is not a valid quorum research key");
            }
            
            return new QuorumResearchKey(required, newKeys);
        } else {
            throw new IllegalArgumentException("Key string '" + keyStr + "' is not a valid quorum research key");
        }
    }

    public static QuorumResearchKey from(@Nullable SimpleResearchKey simpleKey) {
        if (simpleKey == null) {
            throw new IllegalArgumentException("Inner key may not be null");
        } else {
            return new QuorumResearchKey(simpleKey);
        }
    }
    
    public static Optional<QuorumResearchKey> from(@Nonnull Optional<SimpleResearchKey> simpleKeyOpt) {
        return simpleKeyOpt.isPresent() ? Optional.of(new QuorumResearchKey(simpleKeyOpt.get())) : Optional.empty();
    }
    
    public static QuorumResearchKey from(int requiredCount, SimpleResearchKey... simpleKeys) {
        return new QuorumResearchKey(requiredCount, Arrays.stream(simpleKeys).filter(Objects::nonNull).toList());
    }
    
    public static QuorumResearchKey from(int requiredCount, List<SimpleResearchKey> simpleKeys) {
        return new QuorumResearchKey(requiredCount, simpleKeys.stream().filter(Objects::nonNull).toList());
    }
    
    public static QuorumResearchKey from(int requiredCount, String... keyStrs) {
        return new QuorumResearchKey(requiredCount, Arrays.stream(keyStrs).filter(Objects::nonNull).map(SimpleResearchKey::parse).toList());
    }
    
    @Nonnull
    public QuorumResearchKey copy() {
        // Make a deep copy of this quorum research key
        return new QuorumResearchKey(this.requiredCount, this.keys.stream().map(SimpleResearchKey::copy).toList());
    }
    
    @Override
    public boolean isEmpty() {
        return this.keys.isEmpty() || this.keys.stream().allMatch(SimpleResearchKey::isEmpty);
    }

    @Override
    public boolean isKnownBy(Player player) {
        return this.keys.stream().filter(k -> k.isKnownBy(player)).count() >= (long)this.requiredCount;
    }

    @Override
    public boolean isKnownByStrict(Player player) {
        return this.keys.stream().filter(k -> k.isKnownByStrict(player)).count() >= (long)this.requiredCount;
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
            // Return true if the given SRK is one of this QRK's keys, regardless of any stage requirements given in either
            return this.keys.stream().map((k) -> k.stripStage()).toList().contains(simpleKey.stripStage());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("?");
        sb.append(this.requiredCount);
        sb.append(":");
        sb.append(String.join(",", this.keys.stream().map(SimpleResearchKey::toString).toList()));
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(keys, requiredCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        QuorumResearchKey other = (QuorumResearchKey) obj;
        return Objects.equals(keys, other.keys) && requiredCount == other.requiredCount;
    }
    
    public static Builder builder(int requiredCount) {
        return new Builder(requiredCount);
    }
    
    public static class Builder {
        private final int requiredCount;
        private final List<SimpleResearchKey> simpleKeys = new ArrayList<>();
        
        protected Builder(int requiredCount) {
            this.requiredCount = requiredCount;
        }
        
        public Builder add(SimpleResearchKey key) {
            this.simpleKeys.add(key);
            return this;
        }
        
        public Builder add(Optional<SimpleResearchKey> keyOpt) {
            return this.add(keyOpt.orElseThrow());
        }
        
        public Builder add(String keyStr) {
            return this.add(SimpleResearchKey.find(keyStr));
        }
        
        public Builder add(String... keyStrs) {
            Arrays.stream(keyStrs).map(SimpleResearchKey::find).<SimpleResearchKey>map(Optional::orElseThrow).forEach(key -> this.simpleKeys.add(key));
            return this;
        }
        
        public QuorumResearchKey build() {
            return new QuorumResearchKey(this.requiredCount, this.simpleKeys);
        }
    }
}
