package com.verdantartifice.primalmagick.common.worldgen.structures.library;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.books.Culture;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

/**
 * Definition of a piece of an ancient library structure in the nether style.
 * 
 * @author Daedalus4096
 * @see net.minecraft.world.level.levelgen.structure.structures.DesertPyramidPiece
 */
public class NetherLibraryPiece extends AbstractLibraryPiece {
    protected static final Identifier TEMPLATE = ResourceUtils.loc("library/nether");
    
    public NetherLibraryPiece(StructureTemplateManager templateManager, ResourceKey<Culture> cultureKey, BlockPos pos) {
        super(templateManager, TEMPLATE, cultureKey, pos);
    }

    @Override
    protected BlockState getFillerBlockState() {
        return BlocksPM.MARBLE_SMOKED.get().defaultBlockState();
    }

    @Override
    protected BlockState getBrickBlockState() {
        return BlocksPM.MARBLE_SMOKED_BRICKS.get().defaultBlockState();
    }
}
