package com.verdantartifice.primalmagick.common.worldgen.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Definition of a primal shrine structure.
 * 
 * @author Daedalus4096
 * @see net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure
 * @see net.minecraft.world.level.levelgen.structure.structures.IglooStructure
 */
public class ShrineStructure extends Structure {
    public static final MapCodec<ShrineStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ShrineStructure.settingsCodec(instance),
            ShrineStructure.Type.CODEC.fieldOf("shrine_type").forGetter(shrine -> shrine.shrineType)
        ).apply(instance, ShrineStructure::new));
    
    private final ShrineStructure.Type shrineType;
    
    public ShrineStructure(Structure.StructureSettings config, ShrineStructure.Type shrineType) {
        super(config);
        this.shrineType = shrineType;
    }
    
    @Override
    @NotNull
    public Optional<GenerationStub> findGenerationPoint(Structure.@NotNull GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, builder -> this.generatePieces(builder, context));
    }

    @Override
    @NotNull
    public StructureType<?> type() {
        return StructureTypesPM.SHRINE.get();
    }

    protected void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        int surfaceY = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos pos = new BlockPos(x, surfaceY, z);
        builder.addPiece(new ShrinePiece(context.structureTemplateManager(), this.shrineType, pos));
    }

    public enum Type implements StringRepresentable {
        EARTH("earth"),
        SEA("sea"),
        SKY("sky"),
        SUN("sun"),
        MOON("moon");
        
        private final String name;

        public static final Codec<ShrineStructure.Type> CODEC = StringRepresentable.fromEnum(ShrineStructure.Type::values);

        Type(String name) {
            this.name = name;
        }
        
        @NotNull
        public String getSerializedName() {
            return this.name;
        }
    }
}
