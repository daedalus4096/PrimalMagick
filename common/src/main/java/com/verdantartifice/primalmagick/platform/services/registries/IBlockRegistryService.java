package com.verdantartifice.primalmagick.platform.services.registries;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IBlockRegistryService extends IRegistryService<Block> {
    IRegistryItem<Block, Block> registerSimpleBlock(String name, Supplier<BlockBehaviour.Properties> propertiesSupplier);
    <T extends Block> IRegistryItem<Block, T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> blockFactory, Supplier<BlockBehaviour.Properties> propertiesSupplier);
}
