package com.verdantartifice.primalmagick.common.worldgen.features;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

/**
 * Definition of a primal shrine structure.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.level.levelgen.feature.DesertPyramidFeature}
 */
public class ShrineStructure extends StructureFeature<ShrineConfig> {
    public ShrineStructure(Codec<ShrineConfig> codec) {
        super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), ShrineStructure::generatePieces));
    }
    
    protected static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<ShrineConfig> context) {
        int x = (context.chunkPos().x << 4) + 7;
        int z = (context.chunkPos().z << 4) + 7;
        int surfaceY = context.chunkGenerator().getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());
        BlockPos pos = new BlockPos(x, surfaceY, z);
        builder.addPiece(new ShrinePiece(context.structureManager(), context.config().type, pos));
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
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
