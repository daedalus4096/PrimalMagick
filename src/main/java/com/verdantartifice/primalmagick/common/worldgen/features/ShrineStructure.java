package com.verdantartifice.primalmagick.common.worldgen.features;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

/**
 * Definition of a primal shrine structure.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.level.levelgen.feature.DesertPyramidFeature}
 */
public class ShrineStructure extends StructureFeature<ShrineConfig> {
    public ShrineStructure(Codec<ShrineConfig> codec) {
        super(codec);
    }

    @Override
    public StructureFeature.StructureStartFactory<ShrineConfig> getStartFactory() {
        return ShrineStructure.Start::new;
    }
    
    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public String getFeatureName() {
        return PrimalMagick.MODID + ":shrine";
    }

    public static class Start extends StructureStart<ShrineConfig> {
        public Start(StructureFeature<ShrineConfig> structure, ChunkPos chunkPos, int referenceIn, long seed) {
            super(structure, chunkPos, referenceIn, seed);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistryManager, ChunkGenerator generator, StructureManager templateManagerIn, ChunkPos chunkPos, Biome biomeIn, ShrineConfig config, LevelHeightAccessor levelHeightAccessor) {
            int x = (chunkPos.x << 4) + 7;
            int z = (chunkPos.z << 4) + 7;
            int surfaceY = generator.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor);
            BlockPos pos = new BlockPos(x, surfaceY, z);
            this.addPiece(new ShrinePiece(templateManagerIn, config.type, pos));
        }
    }
    
    public static enum Type implements StringRepresentable {
        EARTH("earth"),
        SEA("sea"),
        SKY("sky"),
        SUN("sun"),
        MOON("moon");
        
        private final String name;

        public static final Codec<ShrineStructure.Type> CODEC = StringRepresentable.fromEnum(ShrineStructure.Type::values, ShrineStructure.Type::byName);
        private static final Map<String, ShrineStructure.Type> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(ShrineStructure.Type::getSerializedName, (type) -> {
            return type;
        }));

        private Type(String name) {
            this.name = name;
        }
        
        public static ShrineStructure.Type byName(String name) {
            return BY_NAME.get(name);
        }
        
        public String getSerializedName() {
            return this.name;
        }
    }
}
