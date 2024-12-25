package com.verdantartifice.primalmagick.common.worldgen.structures.library;

import com.verdantartifice.primalmagick.common.books.Culture;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.tiles.base.IRandomizableContents;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructurePieceTypesPM;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

/**
 * Base class definition of a piece of an ancient library structure.  Handles loot population.  Can
 * be overridden to account for multiple styles of library.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.level.levelgen.structure.DesertPyramidPiece}
 */
public abstract class AbstractLibraryPiece extends TemplateStructurePiece {
    protected final ResourceKey<Culture> cultureKey;

    public AbstractLibraryPiece(StructureTemplateManager templateManager, ResourceLocation template, ResourceKey<Culture> cultureKey, BlockPos pos) {
        super(StructurePieceTypesPM.LIBRARY.get(), 0, templateManager, template, template.toString(), makePlaceSettings(), pos);
        this.cultureKey = cultureKey;
    }

    public AbstractLibraryPiece(StructureTemplateManager templateManager, CompoundTag nbt) {
        super(StructurePieceTypesPM.LIBRARY.get(), nbt, templateManager, (dummy) -> {
            return makePlaceSettings();
        });
        this.cultureKey = ResourceKey.create(RegistryKeysPM.CULTURES, ResourceLocation.parse(nbt.getString("Culture")));
    }
    
    public AbstractLibraryPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
        super(StructurePieceTypesPM.LIBRARY.get(), nbt, context.structureTemplateManager(), (dummy) -> {
            return makePlaceSettings();
        });
        this.cultureKey = ResourceKey.create(RegistryKeysPM.CULTURES, ResourceLocation.parse(nbt.getString("Culture")));
    }
    
    protected static StructurePlaceSettings makePlaceSettings() {
        return new StructurePlaceSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }
    
    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
        super.addAdditionalSaveData(pContext, pTag);
        pTag.putString("Culture", this.cultureKey.location().toString());
    }
    
    protected abstract BlockState getFillerBlockState();
    
    protected abstract BlockState getBrickBlockState();

    @Override
    protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {
        // Process data markers, populating bookshelves and replacing blocks as appropriate
        Holder.Reference<Culture> culture = pLevel.registryAccess().registryOrThrow(RegistryKeysPM.CULTURES).getHolderOrThrow(this.cultureKey);
        if ("shelf_low".equals(pName)) {
            // Populate bookshelf above
            if (pLevel.getBlockEntity(pPos.above()) instanceof IRandomizableContents container) {
                container.setLootTable(culture.value().shelfLootTable(), pRandom.nextLong());
            }
            pLevel.setBlock(pPos, this.getFillerBlockState(), Block.UPDATE_ALL);
        } else if ("shelf_high".equals(pName)) {
            // Populate bookshelf below
            if (pLevel.getBlockEntity(pPos.below()) instanceof IRandomizableContents container) {
                container.setLootTable(culture.value().shelfLootTable(), pRandom.nextLong());
            }
            pLevel.setBlock(pPos, this.getBrickBlockState(), Block.UPDATE_ALL);
        } else if ("welcome".equals(pName)) {
            // Populate lectern above
            if (pLevel.getBlockEntity(pPos.above()) instanceof IRandomizableContents container) {
                container.setLootTable(culture.value().welcomeLootTable(), pRandom.nextLong());
            }
            pLevel.setBlock(pPos, this.getFillerBlockState(), Block.UPDATE_ALL);
        } else if ("hidden".equals(pName)) {
            if (pRandom.nextDouble() < 0.25D && pLevel.getBlockEntity(pPos.below()) instanceof RandomizableContainerBlockEntity container) {
                // Populate chest below
                container.setLootTable(culture.value().hiddenLootTable(), pRandom.nextLong());
            } else {
                pLevel.setBlock(pPos, this.getFillerBlockState(), Block.UPDATE_ALL);
                pLevel.setBlock(pPos.below(), this.getFillerBlockState(), Block.UPDATE_ALL);
            }
        } else if ("accent".equals(pName)) {
            // Populate accent blocks
            pLevel.setBlock(pPos, culture.value().accentBlockState(), Block.UPDATE_ALL);
        }
    }
}
