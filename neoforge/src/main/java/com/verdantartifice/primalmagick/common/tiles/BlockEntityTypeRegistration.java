package com.verdantartifice.primalmagick.common.tiles;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Deferred registry for mod block entity types in Neoforge.
 *
 * @author Daedalus4096
 */
public class BlockEntityTypeRegistration {
    private static final DeferredRegister<BlockEntityType<?>> TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static DeferredRegister<BlockEntityType<?>> getDeferredRegister() {
        return TYPES;
    }

    public static void init() {
        TYPES.register(PrimalMagick.getEventBus());
    }
}
