package com.verdantartifice.primalmagick.common.worldgen.structures;

import java.util.Map;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.books.Culture;
import com.verdantartifice.primalmagick.common.books.CulturesPM;
import com.verdantartifice.primalmagick.common.tags.BiomeTagsPM;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.common.worldgen.structures.library.LibraryStructure;
import com.verdantartifice.primalmagick.common.worldgen.structures.library.NetherLibraryStructure;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

/**
 * Registry of mod structures, backed by datapack JSON.
 * 
 * @author Daedalus4096
 */
public class StructuresPM {
    public static final ResourceKey<Structure> EARTH_SHRINE = registryKey("earth_shrine");
    public static final ResourceKey<Structure> SEA_SHRINE = registryKey("sea_shrine");
    public static final ResourceKey<Structure> SKY_SHRINE = registryKey("sky_shrine");
    public static final ResourceKey<Structure> SUN_SHRINE = registryKey("sun_shrine");
    public static final ResourceKey<Structure> MOON_SHRINE = registryKey("moon_shrine");
    
    public static final ResourceKey<Structure> EARTH_LIBRARY = registryKey("earth_library");
    public static final ResourceKey<Structure> SEA_LIBRARY = registryKey("sea_library");
    public static final ResourceKey<Structure> SKY_LIBRARY = registryKey("sky_library");
    public static final ResourceKey<Structure> SUN_LIBRARY = registryKey("sun_library");
    public static final ResourceKey<Structure> MOON_LIBRARY = registryKey("moon_library");
    public static final ResourceKey<Structure> FORBIDDEN_LIBRARY = registryKey("forbidden_library");
    
    private static ResourceKey<Structure> registryKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, ResourceUtils.loc(name));
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes, Map<MobCategory, StructureSpawnOverride> pSpawnOverrides, GenerationStep.Decoration pStep, TerrainAdjustment pTerrainAdaptation) {
        return new Structure.StructureSettings(pBiomes, pSpawnOverrides, pStep, pTerrainAdaptation);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes) {
        return structure(pBiomes, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN);
    }
    
    private static void registerShrine(BootstrapContext<Structure> context, ResourceKey<Structure> structureKey, HolderSet<Biome> biomes, ShrineStructure.Type shrineType) {
        context.register(structureKey, new ShrineStructure(structure(biomes), shrineType));
    }
    
    private static void registerLibrary(BootstrapContext<Structure> context, ResourceKey<Structure> structureKey, HolderSet<Biome> biomes, ResourceKey<Culture> cultureKey) {
        context.register(structureKey, new LibraryStructure(structure(biomes), cultureKey));
    }
    
    public static void bootstrap(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
        
        registerShrine(context, StructuresPM.EARTH_SHRINE, biomeGetter.getOrThrow(BiomeTagsPM.HAS_EARTH_SHRINE), ShrineStructure.Type.EARTH);
        registerShrine(context, StructuresPM.SEA_SHRINE, biomeGetter.getOrThrow(BiomeTagsPM.HAS_SEA_SHRINE), ShrineStructure.Type.SEA);
        registerShrine(context, StructuresPM.SKY_SHRINE, biomeGetter.getOrThrow(BiomeTagsPM.HAS_SKY_SHRINE), ShrineStructure.Type.SKY);
        registerShrine(context, StructuresPM.SUN_SHRINE, biomeGetter.getOrThrow(BiomeTagsPM.HAS_SUN_SHRINE), ShrineStructure.Type.SUN);
        registerShrine(context, StructuresPM.MOON_SHRINE, biomeGetter.getOrThrow(BiomeTagsPM.HAS_MOON_SHRINE), ShrineStructure.Type.MOON);
        
        registerLibrary(context, StructuresPM.EARTH_LIBRARY, biomeGetter.getOrThrow(BiomeTagsPM.HAS_EARTH_LIBRARY), CulturesPM.EARTH);
        registerLibrary(context, StructuresPM.SEA_LIBRARY, biomeGetter.getOrThrow(BiomeTagsPM.HAS_SEA_LIBRARY), CulturesPM.SEA);
        registerLibrary(context, StructuresPM.SKY_LIBRARY, biomeGetter.getOrThrow(BiomeTagsPM.HAS_SKY_LIBRARY), CulturesPM.SKY);
        registerLibrary(context, StructuresPM.SUN_LIBRARY, biomeGetter.getOrThrow(BiomeTagsPM.HAS_SUN_LIBRARY), CulturesPM.SUN);
        registerLibrary(context, StructuresPM.MOON_LIBRARY, biomeGetter.getOrThrow(BiomeTagsPM.HAS_MOON_LIBRARY), CulturesPM.MOON);
        
        context.register(StructuresPM.FORBIDDEN_LIBRARY, new NetherLibraryStructure(
                new Structure.StructureSettings(biomeGetter.getOrThrow(BiomeTagsPM.HAS_FORBIDDEN_LIBRARY), Map.of(), GenerationStep.Decoration.UNDERGROUND_DECORATION, TerrainAdjustment.BEARD_THIN), 
                CulturesPM.FORBIDDEN, 
                UniformHeight.of(VerticalAnchor.absolute(32), VerticalAnchor.belowTop(10))));
    }
}
