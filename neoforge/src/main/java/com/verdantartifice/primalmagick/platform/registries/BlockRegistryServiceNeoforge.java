package com.verdantartifice.primalmagick.platform.registries;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.registries.RegistryItemNeoforge;
import com.verdantartifice.primalmagick.platform.services.registries.IBlockRegistryService;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Neoforge implementation of the block registry service.
 *
 * @author Daedalus4096
 */
public class BlockRegistryServiceNeoforge extends AbstractRegistryServiceNeoforge<Block> implements IBlockRegistryService {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);

    @Override
    protected Supplier<DeferredRegister<Block>> getDeferredRegisterSupplier() {
        return () -> BLOCKS;
    }

    @Override
    protected Registry<Block> getRegistry() {
        return BuiltInRegistries.BLOCK;
    }

    @Override
    public IRegistryItem<Block, Block> registerSimpleBlock(String name, Supplier<BlockBehaviour.Properties> propertiesSupplier) {
        return new RegistryItemNeoforge<>(BLOCKS.registerSimpleBlock(name, propertiesSupplier));
    }

    @Override
    public <T extends Block> IRegistryItem<Block, T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> blockFactory, Supplier<BlockBehaviour.Properties> propertiesSupplier) {
        return new RegistryItemNeoforge<>(BLOCKS.registerBlock(name, blockFactory, propertiesSupplier));
    }
}
