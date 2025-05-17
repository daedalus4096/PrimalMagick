package com.verdantartifice.primalmagick.platform.services;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface IBlockPrototypeService {
    <T extends Block> Supplier<FlowerPotBlock> flowerPot(@Nullable Supplier<FlowerPotBlock> emptyBlock, Supplier<T> flowerSupplier, BlockBehaviour.Properties properties);
}
