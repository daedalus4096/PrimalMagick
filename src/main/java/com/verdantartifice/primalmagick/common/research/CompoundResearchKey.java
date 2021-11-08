package com.verdantartifice.primalmagick.common.research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.minecraft.world.entity.player.Player;

/**
 * Data object identifying multiple research entries/stages at once.  Comprised of multiple simple
 * research keys, and may be satisfied by either all of them or just one.
 * 
 * @author Daedalus4096
 */
public class CompoundResearchKey {
    private static final Logger LOGGER = LogManager.getLogger();

    protected List<SimpleResearchKey> keys = new ArrayList<>();
    protected boolean requireAll;
    
    protected CompoundResearchKey(boolean requireAll) {
        this.requireAll = requireAll;
    }
    
    protected CompoundResearchKey(boolean requireAll, @Nonnull SimpleResearchKey key) {
        this(requireAll);
        this.keys.add(key);
    }
    
    @Nullable
    public static CompoundResearchKey parse(@Nullable String keyStr) {
        if (keyStr == null || keyStr.isEmpty()) {
            return null;
        } else if (keyStr.contains("&&")) {
            if (keyStr.contains("||")) {
                LOGGER.error("Research key may contain && or || but not both: {}", keyStr);
                return null;
            }
            
            // Parse all tokens of the string into simple research keys, filtering out failures, then collect them into a compound research key
            List<SimpleResearchKey> keys = Arrays.asList(keyStr.split("&&")).stream()
                                                .map(k -> SimpleResearchKey.parse(k))
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toList());
            return CompoundResearchKey.from(true, keys.toArray(new SimpleResearchKey[keys.size()]));
        } else if (keyStr.contains("||")) {
            // Parse all tokens of the string into simple research keys, filtering out failures, then collect them into a compound research key
            List<SimpleResearchKey> keys = Arrays.asList(keyStr.split("||")).stream()
                                                .map(k -> SimpleResearchKey.parse(k))
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toList());
            return CompoundResearchKey.from(false, keys.toArray(new SimpleResearchKey[keys.size()]));
        } else {
            return CompoundResearchKey.from(SimpleResearchKey.parse(keyStr));
        }
    }
    
    @Nullable
    public static CompoundResearchKey parse(@Nullable JsonArray jsonArray) throws Exception {
        // When parsing from a JSON file instead of an ad-hoc string, always require that all SRKs be satisfied
        CompoundResearchKey retVal = new CompoundResearchKey(true);
        for (JsonElement element : jsonArray) {
            SimpleResearchKey simpleKey = SimpleResearchKey.parse(element.getAsString());
            if (simpleKey != null) {
                retVal.keys.add(simpleKey);
            }
        }
        return retVal.keys.isEmpty() ? null : retVal;
    }
    
    @Nullable
    public static CompoundResearchKey from(@Nullable SimpleResearchKey simpleKey) {
        return (simpleKey == null) ? null : new CompoundResearchKey(true, simpleKey);
    }
    
    @Nullable
    public static CompoundResearchKey from(boolean requireAll, SimpleResearchKey... simpleKeys) {
        CompoundResearchKey compound = new CompoundResearchKey(requireAll);
        for (SimpleResearchKey simple : simpleKeys) {
            if (simple != null) {
                compound.keys.add(simple);
            }
        }
        return (compound.keys.size() > 0) ? compound : null;
    }
    
    @Nullable
    public static CompoundResearchKey from(boolean requireAll, String... keyStrs) {
        CompoundResearchKey compound = new CompoundResearchKey(requireAll);
        for (String keyStr : keyStrs) {
            SimpleResearchKey key = SimpleResearchKey.parse(keyStr);
            if (key != null) {
                compound.keys.add(key);
            }
        }
        return (compound.keys.size() > 0) ? compound : null;
    }
    
    @Nonnull
    public CompoundResearchKey copy() {
        // Make a deep copy of the compound research key
        CompoundResearchKey key = new CompoundResearchKey(this.requireAll);
        for (SimpleResearchKey simpleKey : this.keys) {
            key.keys.add(simpleKey.copy());
        }
        return key;
    }
    
    @Nonnull
    public List<SimpleResearchKey> getKeys() {
        return Collections.unmodifiableList(this.keys);
    }
    
    public boolean getRequireAll() {
        return this.requireAll;
    }
    
    public boolean isKnownBy(@Nullable Player player) {
        if (this.requireAll) {
            // If requireAll is true, the CRK is only known if all contained SRKs are known
            for (SimpleResearchKey simpleKey : this.keys) {
                if (!simpleKey.isKnownBy(player)) {
                    return false;
                }
            }
            return true;
        } else {
            // Otherwise, the CRK  is known if any of the contained SRKs are known
            for (SimpleResearchKey simpleKey : this.keys) {
                if (simpleKey.isKnownBy(player)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean isKnownByStrict(@Nullable Player player) {
        if (this.requireAll) {
            // If requireAll is true, the CRK is only known if all contained SRKs are known
            for (SimpleResearchKey simpleKey : this.keys) {
                if (!simpleKey.isKnownByStrict(player)) {
                    return false;
                }
            }
            return true;
        } else {
            // Otherwise, the CRK  is known if any of the contained SRKs are known
            for (SimpleResearchKey simpleKey : this.keys) {
                if (simpleKey.isKnownByStrict(player)) {
                    return true;
                }
            }
            return false;
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
            return this.keys.stream().map((k) -> k.stripStage()).collect(Collectors.toList()).contains(simpleKey.stripStage());
        }
    }
    
    @Override
    public String toString() {
        String glue = this.requireAll ? "&&" : "||";
        List<String> simpleStrs = this.keys.stream()
                                    .map(k -> k.toString())
                                    .collect(Collectors.toList());
        return String.join(glue, simpleStrs);
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
}
