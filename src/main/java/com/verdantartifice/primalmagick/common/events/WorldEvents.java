package com.verdantartifice.primalmagick.common.events;

import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagick.PrimalMagic;
import com.verdantartifice.primalmagick.common.worldgen.features.FeaturesPM;

import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
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
        if (event.getWorld() instanceof ServerLevel) {
            ServerLevel serverWorld = (ServerLevel)event.getWorld();
            ServerChunkCache chunkProvider = serverWorld.getChunkSource();
            
            // Prevent spawning structures in vanilla superflat
            if (chunkProvider.getGenerator() instanceof FlatLevelSource && serverWorld.dimension().equals(Level.OVERWORLD)) {
                return;
            }
            
            // Add structure spacing to the world's chunk generator
            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(chunkProvider.generator.getSettings().structureConfig());
            tempMap.putIfAbsent(FeaturesPM.SHRINE.get(), StructureSettings.DEFAULTS.get(FeaturesPM.SHRINE.get()));
            chunkProvider.generator.getSettings().structureConfig = tempMap;
        }
    }
}
