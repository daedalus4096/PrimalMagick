package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IBlockService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Neoforge implementation of the block registry service.
 *
 * @author Daedalus4096
 */
public class BlockServiceNeoforge extends AbstractRegistryServiceNeoforge<Block> implements IBlockService {
    @Override
    protected Supplier<DeferredRegister<Block>> getDeferredRegisterSupplier() {
        return BlockRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<Block> getRegistry() {
        return BuiltInRegistries.BLOCK;
    }
}
