package com.verdantartifice.primalmagick.platform;

import com.verdantartifice.primalmagick.platform.services.IBlockPrototypeService;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockPrototypeServiceNeoforge implements IBlockPrototypeService {
    @Override
    public <T extends Block> Function<BlockBehaviour.Properties, FlowerPotBlock> flowerPot(@Nullable Supplier<FlowerPotBlock> emptyBlock, Supplier<T> flowerSupplier) {
        return props -> new FlowerPotBlock(emptyBlock, flowerSupplier, props);
    }
}
