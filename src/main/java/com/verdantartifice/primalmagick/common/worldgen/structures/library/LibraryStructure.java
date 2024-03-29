package com.verdantartifice.primalmagick.common.worldgen.structures.library;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructureFeaturesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

/**
 * Definition of an ancient library structure.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure}
 * @see {@link net.minecraft.world.level.levelgen.structure.structures.IglooStructure}
 */
public class LibraryStructure extends Structure {
    public static final Codec<LibraryStructure> CODEC = RecordCodecBuilder.<LibraryStructure>mapCodec(instance -> 
            instance.group(LibraryStructure.settingsCodec(instance),
                    LibraryStructure.Type.CODEC.fieldOf("library_type").forGetter(library -> library.libraryType)
            ).apply(instance, LibraryStructure::new)).codec();

    private final LibraryStructure.Type libraryType;
    
    public LibraryStructure(Structure.StructureSettings config, LibraryStructure.Type libraryType) {
        super(config);
        this.libraryType = libraryType;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {
        return onTopOfChunkCenter(pContext, Heightmap.Types.WORLD_SURFACE_WG, builder -> {
            this.generatePieces(builder, pContext);
        });
    }

    @Override
    public StructureType<?> type() {
        return StructureFeaturesPM.LIBRARY.get();
    }
    
    protected void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        int surfaceY = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos pos = new BlockPos(x, surfaceY, z);
        builder.addPiece(new LibraryPiece(context.structureTemplateManager(), this.libraryType, pos));
    }

    public static enum Type implements StringRepresentable {
        EARTH("earth"),
        SEA("sea"),
        SKY("sky"),
        SUN("sun"),
        MOON("moon");
        
        private final String name;

        public static final Codec<LibraryStructure.Type> CODEC = StringRepresentable.fromEnum(LibraryStructure.Type::values);
        private static final Map<String, LibraryStructure.Type> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(LibraryStructure.Type::getSerializedName, t -> t));

        private Type(String name) {
            this.name = name;
        }
        
        public static LibraryStructure.Type byName(String name) {
            return BY_NAME.get(name);
        }
        
        public String getSerializedName() {
            return this.name;
        }
    }
}
