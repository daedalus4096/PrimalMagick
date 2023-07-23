package com.verdantartifice.primalmagick.common.worldgen.structures;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Holder for mod structure piece types.
 * 
 * @author Daedalus4096
 */
public class StructurePieceTypesPM {
    private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, PrimalMagick.MODID);
    
    public static void init() {
        STRUCTURE_PIECES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<StructurePieceType> SHRINE = STRUCTURE_PIECES.register("shrine", () -> ShrinePiece::new);
}
