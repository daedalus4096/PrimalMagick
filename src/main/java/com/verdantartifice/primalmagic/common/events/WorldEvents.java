package com.verdantartifice.primalmagic.common.events;

import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.worldgen.features.FeaturesPM;

import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for world-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class WorldEvents {
	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if (event.getWorld() instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld)event.getWorld();
			ServerChunkProvider chunkProvider = serverWorld.getChunkProvider();
			
			// Prevent spawning structures in vanilla superflat
			if (chunkProvider.getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
				return;
			}
			
			// Add structure spacing to the world's chunk generator
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(chunkProvider.generator.func_235957_b_().func_236195_a_());
            tempMap.putIfAbsent(FeaturesPM.SHRINE.get(), DimensionStructuresSettings.field_236191_b_.get(FeaturesPM.SHRINE.get()));
            chunkProvider.generator.func_235957_b_().field_236193_d_ = tempMap;
		}
	}
}
