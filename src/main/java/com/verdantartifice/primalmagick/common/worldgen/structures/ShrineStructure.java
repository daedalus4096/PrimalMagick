package com.verdantartifice.primalmagick.common.worldgen.structures;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

/**
 * Definition of a primal shrine structure.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure}
 * @see {@link net.minecraft.world.level.levelgen.structure.structures.IglooStructure}
 */
public class ShrineStructure extends Structure {
    public static final Codec<ShrineStructure> CODEC = RecordCodecBuilder.<ShrineStructure>mapCodec(instance -> 
            instance.group(ShrineStructure.settingsCodec(instance),
                    ShrineStructure.Type.CODEC.fieldOf("shrine_type").forGetter(shrine -> shrine.shrineType)
            ).apply(instance, ShrineStructure::new)).codec();
    
    private final ShrineStructure.Type shrineType;
    
    public ShrineStructure(Structure.StructureSettings config, ShrineStructure.Type shrineType) {
        super(config);
        this.shrineType = shrineType;
    }
    
    @Override
    public Optional<GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> {
            this.generatePieces(builder, context);
        });
    }

    @Override
    public StructureType<?> type() {
        return StructureFeaturesPM.SHRINE.get();
    }

    protected void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        int surfaceY = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos pos = new BlockPos(x, surfaceY, z);
        builder.addPiece(new ShrinePiece(context.structureTemplateManager(), this.shrineType, pos));
    }

    public static enum Type implements StringRepresentable {
        EARTH("earth"),
        SEA("sea"),
        SKY("sky"),
        SUN("sun"),
        MOON("moon");
        
        private final String name;

        public static final Codec<ShrineStructure.Type> CODEC = StringRepresentable.fromEnum(ShrineStructure.Type::values);
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
