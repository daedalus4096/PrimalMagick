package com.verdantartifice.primalmagic.common.events;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
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
        // Add raw marble, rock salt, and quartz seams to all non-Nether, non-End biomes
		if (isOverworldBiome(event.getName(), event.getCategory())) {
			event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, FeaturesPM.ORE_MARBLE_RAW);
			event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, FeaturesPM.ORE_ROCK_SALT);
			event.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, FeaturesPM.ORE_QUARTZ);
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
