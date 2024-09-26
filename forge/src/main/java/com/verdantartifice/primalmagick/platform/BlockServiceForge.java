package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.common.blocks.BlockRegistration;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Forge implementation of the block registry service.
 *
 * @author Daedalus4096
 */
public class BlockServiceForge extends AbstractRegistryServiceForge<Block> {
    @Override
    protected Supplier<DeferredRegister<Block>> getDeferredRegisterSupplier() {
        return BlockRegistration::getDeferredRegister;
    }

    @Override
    protected IForgeRegistry<Block> getRegistry() {
        return ForgeRegistries.BLOCKS;
    }
}
