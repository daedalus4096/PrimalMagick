package com.verdantartifice.primalmagick.common.worldgen.structures.library;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
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
 * Definition of a piece of an ancient library structure in the nether style.
 * 
 * @author Daedalus4096
 * @see {@link net.minecraft.world.level.levelgen.structure.DesertPyramidPiece}
 */
public class NetherLibraryPiece extends AbstractLibraryPiece {
    protected static final ResourceLocation TEMPLATE = ResourceUtils.loc("library/nether");
    
    public NetherLibraryPiece(StructureTemplateManager templateManager, ResourceKey<Culture> cultureKey, BlockPos pos) {
        super(templateManager, TEMPLATE, cultureKey, pos);
    }

    public NetherLibraryPiece(StructureTemplateManager templateManager, CompoundTag nbt) {
        super(templateManager, nbt);
    }
    
    public NetherLibraryPiece(StructurePieceSerializationContext context, CompoundTag nbt) {
        super(context, nbt);
    }

    @Override
    protected BlockState getFillerBlockState() {
        return BlockRegistration.MARBLE_SMOKED.get().defaultBlockState();
    }

    @Override
    protected BlockState getBrickBlockState() {
        return BlockRegistration.MARBLE_SMOKED_BRICKS.get().defaultBlockState();
    }
}
