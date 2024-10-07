package com.verdantartifice.primalmagick.common.worldgen.structures.library;

import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.books.Culture;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructureTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
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
    public static final MapCodec<LibraryStructure> CODEC = RecordCodecBuilder.<LibraryStructure>mapCodec(instance -> instance.group(
            LibraryStructure.settingsCodec(instance),
            ResourceKey.codec(RegistryKeysPM.CULTURES).fieldOf("culture_key").forGetter(library -> library.cultureKey)
        ).apply(instance, LibraryStructure::new));

    private final ResourceKey<Culture> cultureKey;
    
    public LibraryStructure(Structure.StructureSettings config, ResourceKey<Culture> cultureKey) {
        super(config);
        this.cultureKey = cultureKey;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {
        return onTopOfChunkCenter(pContext, Heightmap.Types.WORLD_SURFACE_WG, builder -> {
            this.generatePieces(builder, pContext);
        });
    }

    @Override
    public StructureType<?> type() {
        return StructureTypesPM.LIBRARY.get();
    }
    
    protected void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        int surfaceY = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
        BlockPos pos = new BlockPos(x, surfaceY, z);
        builder.addPiece(new LibraryPiece(context.structureTemplateManager(), this.cultureKey, pos));
    }
}
