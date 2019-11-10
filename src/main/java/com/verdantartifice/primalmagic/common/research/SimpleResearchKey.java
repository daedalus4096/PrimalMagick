package com.verdantartifice.primalmagic.common.research;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class SimpleResearchKey {
    protected String rootKey;
    protected Integer stage;
    
    protected SimpleResearchKey(@Nonnull String rootKey, @Nullable Integer stage) {
        this.rootKey = rootKey;
        this.stage = stage;
    }
    
    @Nullable
    public static SimpleResearchKey parse(@Nullable String keyStr) {
        if (keyStr == null) {
            return null;
        } else if (keyStr.contains("@")) {
            String[] tokens = keyStr.split("@");
            return new SimpleResearchKey(tokens[0], MathHelper.getInt(tokens[1], 0));
        } else {
            return new SimpleResearchKey(keyStr, null);
        }
    }
    
    @Nonnull
    public SimpleResearchKey copy() {
        return new SimpleResearchKey(this.rootKey, this.stage);
    }
    
    @Nonnull
    public String getRootKey() {
        return this.rootKey;
    }
    
    public boolean hasStage() {
        return (this.stage != null);
    }
    
    public int getStage() {
        return this.hasStage() ? this.stage.intValue() : -1;
    }
    
    @Nonnull
    public SimpleResearchKey stripStage() {
        return new SimpleResearchKey(this.rootKey, null);
    }
    
    public boolean isKnownBy(@Nullable PlayerEntity player) {
        if (player == null) {
            return false;
        }
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        } else {
            return knowledge.isResearchKnown(this);
        }
    }
    
    public boolean isKnownByStrict(@Nullable PlayerEntity player) {
        if (player == null) {
            return false;
        }
        IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
        if (knowledge == null) {
            return false;
        }
        if (this.hasStage()) {
            if (!knowledge.isResearchKnown(this)) {
                return false;
            }
        } else if (!knowledge.isResearchComplete(this)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.rootKey);
        if (this.hasStage()) {
            builder.append('@');
            builder.append(this.getStage());
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rootKey == null) ? 0 : rootKey.hashCode());
        result = prime * result + ((stage == null) ? 0 : stage.hashCode());
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
        SimpleResearchKey other = (SimpleResearchKey) obj;
        if (rootKey == null) {
            if (other.rootKey != null)
                return false;
        } else if (!rootKey.equals(other.rootKey))
            return false;
        if (stage == null) {
            if (other.stage != null)
                return false;
        } else if (!stage.equals(other.stage))
            return false;
        return true;
    }
}
