package com.verdantartifice.primalmagic.common.worldgen.features;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;

public class StructurePieceTypesPM {
    public static final IStructurePieceType SHRINE = register(ShrinePiece::new, new ResourceLocation(PrimalMagic.MODID, "shrine"));
    
    private static IStructurePieceType register(IStructurePieceType spt, ResourceLocation key) {
        return Registry.register(Registry.STRUCTURE_PIECE, key, spt);
    }
}
