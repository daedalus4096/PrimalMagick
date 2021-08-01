package com.verdantartifice.primalmagic.common.worldgen.features;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;

/**
 * Holder for mod structure piece types.
 * 
 * @author Daedalus4096
 */
public class StructurePieceTypesPM {
    public static final StructurePieceType SHRINE = register(ShrinePiece::new, new ResourceLocation(PrimalMagic.MODID, "shrine"));
    
    private static StructurePieceType register(StructurePieceType spt, ResourceLocation key) {
        return Registry.register(Registry.STRUCTURE_PIECE, key, spt);
    }
}
