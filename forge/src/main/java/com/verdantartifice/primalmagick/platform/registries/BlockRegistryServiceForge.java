package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
import com.verdantartifice.primalmagick.platform.services.registries.IBlockRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Forge implementation of the block registry service.
 *
 * @author Daedalus4096
 */
public class BlockRegistryServiceForge extends AbstractBuiltInRegistryServiceForge<Block> implements IBlockRegistryService {
    @Override
    protected Supplier<DeferredRegister<Block>> getDeferredRegisterSupplier() {
        return BlockRegistration::getDeferredRegister;
    }

    @Override
    protected Registry<Block> getRegistry() {
        return BuiltInRegistries.BLOCK;
    }
}
