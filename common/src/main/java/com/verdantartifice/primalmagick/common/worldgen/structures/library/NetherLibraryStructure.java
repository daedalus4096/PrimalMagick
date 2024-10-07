package com.verdantartifice.primalmagick.common.worldgen.structures.library;

import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.books.Culture;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructureTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

/**
 * Definition of an ancient library structure located in the Nether.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure}
 * @see {@link net.minecraft.world.level.levelgen.structure.structures.NetherFossilStructure}
 */
public class NetherLibraryStructure extends Structure {
    public static final MapCodec<NetherLibraryStructure> CODEC = RecordCodecBuilder.<NetherLibraryStructure>mapCodec(instance -> instance.group(
            NetherLibraryStructure.settingsCodec(instance),
            ResourceKey.codec(RegistryKeysPM.CULTURES).fieldOf("culture_key").forGetter(library -> library.cultureKey),
            HeightProvider.CODEC.fieldOf("height").forGetter(library -> library.height)
        ).apply(instance, NetherLibraryStructure::new));

    private final HeightProvider height;
    private final ResourceKey<Culture> cultureKey;

    public NetherLibraryStructure(Structure.StructureSettings config, ResourceKey<Culture> cultureKey, HeightProvider heightProvider) {
        super(config);
        this.cultureKey = cultureKey;
        this.height = heightProvider;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {
        WorldgenRandom random = pContext.random();
        int x = pContext.chunkPos().getMinBlockX() + random.nextInt(16);
        int z = pContext.chunkPos().getMinBlockZ() + random.nextInt(16);
        
        // Search for a suitable y-level within the structure's height range
        int minY = pContext.chunkGenerator().getSeaLevel();
        int y = this.height.sample(random, new WorldGenerationContext(pContext.chunkGenerator(), pContext.heightAccessor()));
        NoiseColumn column = pContext.chunkGenerator().getBaseColumn(x, z, pContext.heightAccessor(), pContext.randomState());
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(x, y, z);
        while (y > minY) {
            BlockState above = column.getBlock(y);
            y--;
            BlockState below = column.getBlock(y);
            if (above.isAir() && !below.is(Blocks.BEDROCK) && (below.is(Blocks.SOUL_SAND) || below.isFaceSturdy(EmptyBlockGetter.INSTANCE, mutablePos.setY(y), Direction.UP))) {
                break;
            }
        }
        
        if (y <= minY) {
            return Optional.empty();
        } else {
            BlockPos pos = new BlockPos(x, y, z);
            return Optional.of(new Structure.GenerationStub(pos, builder -> {
                builder.addPiece(new NetherLibraryPiece(pContext.structureTemplateManager(), this.cultureKey, pos));
            }));
        }
    }

    @Override
    public StructureType<?> type() {
        return StructureTypesPM.NETHER_LIBRARY.get();
    }
}
