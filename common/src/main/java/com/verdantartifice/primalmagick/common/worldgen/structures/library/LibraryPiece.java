package com.verdantartifice.primalmagick.common.worldgen.structures.library;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.books.Culture;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

/**
 * Definition of a piece of an ancient library structure in the default style.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.level.levelgen.structure.DesertPyramidPiece}
 */
public class LibraryPiece extends AbstractLibraryPiece {
    protected static final ResourceLocation TEMPLATE = ResourceUtils.loc("library/default");
    
    public LibraryPiece(StructureTemplateManager templateManager, ResourceKey<Culture> cultureKey, BlockPos pos) {
        super(templateManager, TEMPLATE, cultureKey, pos);
    }

    public LibraryPiece(StructureTemplateManager templateManager, CompoundTag nbt) {
        super(templateManager, nbt);
    }
    
    public LibraryPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
        super(context, nbt);
    }

    @Override
    protected BlockState getFillerBlockState() {
        return BlocksPM.MARBLE_RAW.get().defaultBlockState();
    }

    @Override
    protected BlockState getBrickBlockState() {
        return BlocksPM.MARBLE_BRICKS.get().defaultBlockState();
    }
}
