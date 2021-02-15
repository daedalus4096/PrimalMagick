package com.verdantartifice.primalmagic.common.events;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.worldgen.features.ConfiguredFeaturesPM;
import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for biome related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class BiomeEvents {
	@SubscribeEvent(priority=EventPriority.HIGH)
	public static void loadBiome(BiomeLoadingEvent event) {
		Biome.Category cat = event.getCategory();
		
        // Add raw marble, rock salt, and quartz seams to all non-Nether, non-End biomes
		if (isOverworldBiome(event.getName(), cat)) {
			event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, FeaturesPM.ORE_MARBLE_RAW);
			event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, FeaturesPM.ORE_ROCK_SALT);
			event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, FeaturesPM.ORE_QUARTZ);
		}
		
		// Add earth shrines to flatlands
		if (Biome.Category.PLAINS.equals(cat) || Biome.Category.SAVANNA.equals(cat)) {
			event.getGeneration().getStructures().add(() -> ConfiguredFeaturesPM.CONFIGURED_EARTH_SHRINE);
		}
		
		// Add sea shrines to wet biomes
		if (Biome.Category.RIVER.equals(cat) || Biome.Category.BEACH.equals(cat) || Biome.Category.SWAMP.equals(cat)) {
			event.getGeneration().getStructures().add(() -> ConfiguredFeaturesPM.CONFIGURED_SEA_SHRINE);
		}
		
		// Add sky shrines to mountains
		if (Biome.Category.EXTREME_HILLS.equals(cat)) {
			event.getGeneration().getStructures().add(() -> ConfiguredFeaturesPM.CONFIGURED_SKY_SHRINE);
		}
		
		// Add sun shrines to deserts
		if (Biome.Category.DESERT.equals(cat)) {
			event.getGeneration().getStructures().add(() -> ConfiguredFeaturesPM.CONFIGURED_SUN_SHRINE);
		}
		
		// Add moon shrines to forests
		if (Biome.Category.FOREST.equals(cat)) {
			// TODO Phase sunwood and moonwood trees appropriately
		    event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeaturesPM.TREE_SUNWOOD_FULL);
		    event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, FeaturesPM.TREE_MOONWOOD_FULL);
			event.getGeneration().getStructures().add(() -> ConfiguredFeaturesPM.CONFIGURED_MOON_SHRINE);
		}
	}

    private static boolean isOverworldBiome(@Nonnull ResourceLocation biomeName, @Nonnull Biome.Category biomeCategory) {
        if (biomeName.equals(Biomes.STONE_SHORE.getRegistryName())) {
            // Stone Shore has a category of None, but it still exists in the Overworld
            return true;
        } else {
            return !Biome.Category.NONE.equals(biomeCategory) && !Biome.Category.NETHER.equals(biomeCategory) && !Biome.Category.THEEND.equals(biomeCategory);
        }
    }
}
