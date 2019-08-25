package com.verdantartifice.primalmagic.common.blocks.base;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.Block;

public class BlockPM extends Block {
    public BlockPM(String name, Properties properties) {
        super(properties);
        this.setRegistryName(PrimalMagic.MODID, name);
    }
}
