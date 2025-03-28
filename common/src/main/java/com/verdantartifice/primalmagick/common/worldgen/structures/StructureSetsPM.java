package com.verdantartifice.primalmagick.common.worldgen.structures;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

/**
 * Registry of mod structure sets, backed by datapack JSON.
 * 
 * @author Daedalus4096
 */
public class StructureSetsPM {
    public static final ResourceKey<StructureSet> SHRINES = registryKey("shrines");
    public static final ResourceKey<StructureSet> LIBRARIES = registryKey("libraries");
    
    private static ResourceKey<StructureSet> registryKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, ResourceUtils.loc(name));
    }
    
    public static void bootstrap(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structureGetter = context.lookup(Registries.STRUCTURE);
        context.register(StructureSetsPM.SHRINES, new StructureSet(
                List.of(StructureSet.entry(structureGetter.getOrThrow(StructuresPM.EARTH_SHRINE)), 
                        StructureSet.entry(structureGetter.getOrThrow(StructuresPM.SEA_SHRINE)), 
                        StructureSet.entry(structureGetter.getOrThrow(StructuresPM.SKY_SHRINE)), 
                        StructureSet.entry(structureGetter.getOrThrow(StructuresPM.SUN_SHRINE)), 
                        StructureSet.entry(structureGetter.getOrThrow(StructuresPM.MOON_SHRINE))),
                new RandomSpreadStructurePlacement(20, 10, RandomSpreadType.LINEAR, 11893192)));
        context.register(StructureSetsPM.LIBRARIES, new StructureSet(
                List.of(StructureSet.entry(structureGetter.getOrThrow(StructuresPM.EARTH_LIBRARY)), 
                        StructureSet.entry(structureGetter.getOrThrow(StructuresPM.SEA_LIBRARY)), 
                        StructureSet.entry(structureGetter.getOrThrow(StructuresPM.SKY_LIBRARY)), 
                        StructureSet.entry(structureGetter.getOrThrow(StructuresPM.SUN_LIBRARY)), 
                        StructureSet.entry(structureGetter.getOrThrow(StructuresPM.MOON_LIBRARY)),
                        StructureSet.entry(structureGetter.getOrThrow(StructuresPM.FORBIDDEN_LIBRARY))),
                new RandomSpreadStructurePlacement(30, 15, RandomSpreadType.LINEAR, 11893193)));
    }
}
