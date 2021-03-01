package com.verdantartifice.primalmagic.common.affinities;

import java.util.function.BiFunction;

import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.util.ResourceLocation;

public abstract class AbstractAffinity implements IAffinity {
    protected static final BiFunction<AffinityType, ResourceLocation, IAffinity> DUMMY_LOOKUP = (type, loc) -> {
        return null;
    };
    
    protected ResourceLocation targetId;
    protected BiFunction<AffinityType, ResourceLocation, IAffinity> lookupFunc;
    protected SourceList totalCache;

    protected AbstractAffinity(ResourceLocation target, BiFunction<AffinityType, ResourceLocation, IAffinity> lookupFunc) {
        this.targetId = target;
        this.lookupFunc = lookupFunc;
    }
    
    @Override
    public ResourceLocation getTarget() {
        return this.targetId;
    }

    @Override
    public SourceList getTotal() {
        if (this.totalCache == null) {
            this.totalCache = this.calculateTotal();
        }
        return this.totalCache.copy();
    }
    
    protected abstract SourceList calculateTotal();
}
