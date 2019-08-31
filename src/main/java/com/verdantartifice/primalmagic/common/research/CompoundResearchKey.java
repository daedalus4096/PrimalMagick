package com.verdantartifice.primalmagic.common.research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.entity.player.PlayerEntity;

public class CompoundResearchKey {
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
                PrimalMagic.LOGGER.error("Research key may contain && or || but not both: {}", keyStr);
                return null;
            }
            List<SimpleResearchKey> keys = Arrays.asList(keyStr.split("&&")).stream()
                                                .map(k -> SimpleResearchKey.parse(k))
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toList());
            return CompoundResearchKey.from(true, keys.toArray(new SimpleResearchKey[keys.size()]));
        } else if (keyStr.contains("||")) {
            List<SimpleResearchKey> keys = Arrays.asList(keyStr.split("||")).stream()
                                                .map(k -> SimpleResearchKey.parse(k))
                                                .filter(Objects::nonNull)
                                                .collect(Collectors.toList());
            return CompoundResearchKey.from(false, keys.toArray(new SimpleResearchKey[keys.size()]));
        } else {
            return CompoundResearchKey.from(SimpleResearchKey.parse(keyStr));
        }
    }
    
    @Nonnull
    public static CompoundResearchKey parse(@Nullable JsonArray jsonArray) throws Exception {
        CompoundResearchKey retVal = new CompoundResearchKey(true);
        for (JsonElement element : jsonArray) {
            SimpleResearchKey simpleKey = SimpleResearchKey.parse(element.getAsString());
            if (simpleKey != null) {
                retVal.keys.add(simpleKey);
            }
        }
        return retVal;
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
    
    @Nonnull
    public List<SimpleResearchKey> getKeys() {
        return Collections.unmodifiableList(this.keys);
    }
    
    public boolean getRequireAll() {
        return this.requireAll;
    }
    
    public boolean isKnownBy(@Nullable PlayerEntity player) {
        if (this.requireAll) {
            for (SimpleResearchKey simpleKey : this.keys) {
                if (!simpleKey.isKnownBy(player)) {
                    return false;
                }
            }
            return true;
        } else {
            for (SimpleResearchKey simpleKey : this.keys) {
                if (simpleKey.isKnownBy(player)) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public boolean isKnownByStrict(@Nullable PlayerEntity player) {
        if (this.requireAll) {
            for (SimpleResearchKey simpleKey : this.keys) {
                if (!simpleKey.isKnownByStrict(player)) {
                    return false;
                }
            }
            return true;
        } else {
            for (SimpleResearchKey simpleKey : this.keys) {
                if (simpleKey.isKnownByStrict(player)) {
                    return true;
                }
            }
            return false;
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
