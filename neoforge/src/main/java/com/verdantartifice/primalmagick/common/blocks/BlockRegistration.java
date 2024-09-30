package com.verdantartifice.primalmagick.common.blocks;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Deferred registry for mod blocks in Neoforge.
 *
 * @author Daedalus4096
 */
public class BlockRegistration {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Constants.MOD_ID);

    public static DeferredRegister<Block> getDeferredRegister() {
        return BLOCKS;
    }

    public static void init() {
        BLOCKS.register(PrimalMagick.getEventBus());
    }
}
