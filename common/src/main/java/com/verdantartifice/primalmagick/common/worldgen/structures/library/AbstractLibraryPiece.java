package com.verdantartifice.primalmagick.common.worldgen.structures.library;

import com.verdantartifice.primalmagick.common.books.Culture;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;
import com.verdantartifice.primalmagick.common.tiles.base.IRandomizableContents;
import com.verdantartifice.primalmagick.common.worldgen.structures.StructurePieceTypesPM;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.NotNull;

/**
 * Base class definition of a piece of an ancient library structure.  Handles loot population.  Can
 * be overridden to account for multiple styles of library.
 * 
 * @author Daedalus4096
 * @see net.minecraft.world.level.levelgen.structure.structures.DesertPyramidPiece
 */
public abstract class AbstractLibraryPiece extends TemplateStructurePiece {
    protected final ResourceKey<Culture> cultureKey;

    public AbstractLibraryPiece(StructureTemplateManager templateManager, ResourceLocation template, ResourceKey<Culture> cultureKey, BlockPos pos) {
        super(StructurePieceTypesPM.LIBRARY.get(), 0, templateManager, template, template.toString(), makePlaceSettings(), pos);
        this.cultureKey = cultureKey;
    }

    public AbstractLibraryPiece(StructureTemplateManager templateManager, CompoundTag nbt) {
        super(StructurePieceTypesPM.LIBRARY.get(), nbt, templateManager, dummy -> makePlaceSettings());
        this.cultureKey = nbt.read("Culture", ResourceKey.codec(RegistryKeysPM.CULTURES)).orElseThrow();
    }
    
    public AbstractLibraryPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
        super(StructurePieceTypesPM.LIBRARY.get(), nbt, context.structureTemplateManager(), dummy -> makePlaceSettings());
        this.cultureKey = nbt.read("Culture", ResourceKey.codec(RegistryKeysPM.CULTURES)).orElseThrow();
    }
    
    protected static StructurePlaceSettings makePlaceSettings() {
        return new StructurePlaceSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }
    
    @Override
    protected void addAdditionalSaveData(@NotNull StructurePieceSerializationContext pContext, @NotNull CompoundTag pTag) {
        super.addAdditionalSaveData(pContext, pTag);
        pTag.store("Culture", ResourceKey.codec(RegistryKeysPM.CULTURES), this.cultureKey);
    }
    
    protected abstract BlockState getFillerBlockState();
    
    protected abstract BlockState getBrickBlockState();

    @Override
    protected void handleDataMarker(@NotNull String pName, @NotNull BlockPos pPos, @NotNull ServerLevelAccessor pLevel, @NotNull RandomSource pRandom, @NotNull BoundingBox pBox) {
        // Process data markers, populating bookshelves and replacing blocks as appropriate
        Culture culture = pLevel.registryAccess().lookupOrThrow(RegistryKeysPM.CULTURES).getValueOrThrow(this.cultureKey);
        switch (pName) {
            case "shelf_low" -> {
                // Populate bookshelf above
                if (pLevel.getBlockEntity(pPos.above()) instanceof IRandomizableContents container) {
                    container.setLootTable(culture.shelfLootTable(), pRandom.nextLong());
                }
                pLevel.setBlock(pPos, this.getFillerBlockState(), Block.UPDATE_ALL);
            }
            case "shelf_high" -> {
                // Populate bookshelf below
                if (pLevel.getBlockEntity(pPos.below()) instanceof IRandomizableContents container) {
                    container.setLootTable(culture.shelfLootTable(), pRandom.nextLong());
                }
                pLevel.setBlock(pPos, this.getBrickBlockState(), Block.UPDATE_ALL);
            }
            case "welcome" -> {
                // Populate lectern above
                if (pLevel.getBlockEntity(pPos.above()) instanceof IRandomizableContents container) {
                    container.setLootTable(culture.welcomeLootTable(), pRandom.nextLong());
                }
                pLevel.setBlock(pPos, this.getFillerBlockState(), Block.UPDATE_ALL);
            }
            case "hidden" -> {
                if (pRandom.nextDouble() < 0.25D && pLevel.getBlockEntity(pPos.below()) instanceof RandomizableContainerBlockEntity container) {
                    // Populate chest below
                    container.setLootTable(culture.hiddenLootTable(), pRandom.nextLong());
                } else {
                    pLevel.setBlock(pPos, this.getFillerBlockState(), Block.UPDATE_ALL);
                    pLevel.setBlock(pPos.below(), this.getFillerBlockState(), Block.UPDATE_ALL);
                }
            }
            case "accent" -> {
                // Populate accent blocks
                pLevel.setBlock(pPos, culture.accentBlockState(), Block.UPDATE_ALL);
            }
        }
    }
}
