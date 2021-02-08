package com.verdantartifice.primalmagic.common.worldgen.features;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.StructureFeature;

/**
 * Definition of configures mod structures.
 * 
 * @author Daedalus4096
 */
public class ConfiguredFeaturesPM {
	public static final StructureFeature<?, ?> CONFIGURED_EARTH_SHRINE = FeaturesPM.SHRINE.get().func_236391_a_(new ShrineConfig(ShrineStructure.Type.EARTH));
	public static final StructureFeature<?, ?> CONFIGURED_SEA_SHRINE = FeaturesPM.SHRINE.get().func_236391_a_(new ShrineConfig(ShrineStructure.Type.SEA));
	public static final StructureFeature<?, ?> CONFIGURED_SKY_SHRINE = FeaturesPM.SHRINE.get().func_236391_a_(new ShrineConfig(ShrineStructure.Type.SKY));
	public static final StructureFeature<?, ?> CONFIGURED_SUN_SHRINE = FeaturesPM.SHRINE.get().func_236391_a_(new ShrineConfig(ShrineStructure.Type.SUN));
	public static final StructureFeature<?, ?> CONFIGURED_MOON_SHRINE = FeaturesPM.SHRINE.get().func_236391_a_(new ShrineConfig(ShrineStructure.Type.MOON));
	
	public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        registerConfiguredStructure(registry, "configured_earth_shrine", CONFIGURED_EARTH_SHRINE);
        registerConfiguredStructure(registry, "configured_sea_shrine", CONFIGURED_SEA_SHRINE);
        registerConfiguredStructure(registry, "configured_sky_shrine", CONFIGURED_SKY_SHRINE);
        registerConfiguredStructure(registry, "configured_sun_shrine", CONFIGURED_SUN_SHRINE);
        registerConfiguredStructure(registry, "configured_moon_shrine", CONFIGURED_MOON_SHRINE);
        FlatGenerationSettings.STRUCTURES.put(FeaturesPM.SHRINE.get(), CONFIGURED_EARTH_SHRINE);
	}
	
	protected static void registerConfiguredStructure(Registry<StructureFeature<?, ?>> registry, String key, StructureFeature<?, ?> value) {
		Registry.register(registry, new ResourceLocation(PrimalMagic.MODID, key), value);
	}
}
