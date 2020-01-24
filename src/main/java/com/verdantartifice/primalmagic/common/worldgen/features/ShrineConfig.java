package com.verdantartifice.primalmagic.common.worldgen.features;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.world.gen.feature.IFeatureConfig;

/**
 * Definition for the configuration data of a primal shrine feature.
 * 
 * @author Daedalus4096
 */
public class ShrineConfig implements IFeatureConfig {
    public final Source source;
    
    public ShrineConfig(Source source) {
        this.source = source;
    }
    
    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("source"), ops.createString(this.source.getTag()))));
    }

    public static <T> ShrineConfig deserialize(Dynamic<T> params) {
        Source source = Source.getSource(params.get("source").asString(""));
        return new ShrineConfig(source);
    }
}
