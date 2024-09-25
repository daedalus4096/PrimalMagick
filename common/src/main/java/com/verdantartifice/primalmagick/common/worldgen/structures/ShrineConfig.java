package com.verdantartifice.primalmagick.common.worldgen.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * Definition for the configuration data of a primal shrine feature.
 * 
 * @author Daedalus4096
 */
public class ShrineConfig implements FeatureConfiguration {
    public static final Codec<ShrineConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(ShrineStructure.Type.CODEC.fieldOf("type").forGetter((c) -> { return c.type; })).apply(instance, ShrineConfig::new);
    });
    
    public final ShrineStructure.Type type;
    
    public ShrineConfig(ShrineStructure.Type type) {
        this.type = type;
    }
}
