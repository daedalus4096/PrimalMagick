package com.verdantartifice.primalmagick.common.blocks;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod blocks.
 * 
 * @author Daedalus4096
 */
public class BlockRegistration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);

    public static DeferredRegister<Block> getDeferredRegister() {
        return BLOCKS;
    }
    
    public static void init() {
        BLOCKS.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
}
