package com.verdantartifice.primalmagick.common.worldgen.structures.library;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.loot.LootTablesPM;
import com.verdantartifice.primalmagick.common.tiles.base.IRandomizableContents;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructurePieceTypesPM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

/**
 * Definition of a piece of an ancient library structure.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.level.levelgen.structure.DesertPyramidPiece}
 */
public class LibraryPiece extends TemplateStructurePiece {
    protected static final ResourceLocation TEMPLATE = PrimalMagick.resource("library/default");
    
    protected final LibraryStructure.Type type;

    public LibraryPiece(StructureTemplateManager templateManager, LibraryStructure.Type type, BlockPos pos) {
        super(StructurePieceTypesPM.LIBRARY.get(), 0, templateManager, TEMPLATE, TEMPLATE.toString(), makePlaceSettings(), pos);
        this.type = type;
    }

    public LibraryPiece(StructureTemplateManager templateManager, CompoundTag nbt) {
        super(StructurePieceTypesPM.LIBRARY.get(), nbt, templateManager, (dummy) -> {
            return makePlaceSettings();
        });
        this.type = LibraryStructure.Type.byName(nbt.getString("Culture"));
    }
    
    public LibraryPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
        super(StructurePieceTypesPM.LIBRARY.get(), nbt, context.structureTemplateManager(), (dummy) -> {
            return makePlaceSettings();
        });
        this.type = LibraryStructure.Type.byName(nbt.getString("Culture"));
    }
    
    protected static StructurePlaceSettings makePlaceSettings() {
        return new StructurePlaceSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }
    
    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
        super.addAdditionalSaveData(pContext, pTag);
        pTag.putString("Culture", this.type.getSerializedName());
    }

    @Override
    protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {
        // Process data markers, populating bookshelves and replacing blocks as appropriate
        if ("shelf_low".equals(pName)) {
            // Populate bookshelf above
            if (pLevel.getBlockEntity(pPos.above()) instanceof IRandomizableContents container) {
                container.setLootTable(this.type.getLootTable(), pRandom.nextLong());
            }
            pLevel.setBlock(pPos, BlocksPM.MARBLE_RAW.get().defaultBlockState(), Block.UPDATE_ALL);
        } else if ("shelf_high".equals(pName)) {
            // Populate bookshelf below
            if (pLevel.getBlockEntity(pPos.below()) instanceof IRandomizableContents container) {
                container.setLootTable(this.type.getLootTable(), pRandom.nextLong());
            }
            pLevel.setBlock(pPos, BlocksPM.MARBLE_BRICKS.get().defaultBlockState(), Block.UPDATE_ALL);
        } else if ("welcome".equals(pName)) {
            // Populate lectern above
            if (pLevel.getBlockEntity(pPos.above()) instanceof IRandomizableContents container) {
                container.setLootTable(LootTablesPM.LIBRARY_WELCOME, pRandom.nextLong());
            }
            pLevel.setBlock(pPos, BlocksPM.MARBLE_RAW.get().defaultBlockState(), Block.UPDATE_ALL);
        } else if ("hidden".equals(pName)) {
            if (pRandom.nextDouble() < 0.25D && pLevel.getBlockEntity(pPos.below()) instanceof RandomizableContainerBlockEntity container) {
                // Populate chest below
                container.setLootTable(LootTablesPM.LIBRARY_HIDDEN, pRandom.nextLong());
            } else {
                pLevel.setBlock(pPos, BlocksPM.MARBLE_TILES.get().defaultBlockState(), Block.UPDATE_ALL);
                pLevel.setBlock(pPos.below(), BlocksPM.MARBLE_TILES.get().defaultBlockState(), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    public void postProcess(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator pGenerator, RandomSource pRandom, BoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
        int i = pLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, this.templatePosition.getX(), this.templatePosition.getZ());
        this.templatePosition = new BlockPos(this.templatePosition.getX(), i, this.templatePosition.getZ());
        super.postProcess(pLevel, pStructureManager, pGenerator, pRandom, pBox, pChunkPos, pPos);
    }

}
