package com.verdantartifice.primalmagick.common.worldgen.features;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

/**
 * Holder for mod structure piece types.
 * 
 * @author Daedalus4096
 */
public class StructurePieceTypesPM {
    public static final StructurePieceType SHRINE = register(ShrinePiece::new, new ResourceLocation(PrimalMagick.MODID, "shrine"));
    
    private static StructurePieceType register(StructurePieceType.StructureTemplateType spt, ResourceLocation key) {
        return Registry.register(Registry.STRUCTURE_PIECE, key, spt);
    }
}
