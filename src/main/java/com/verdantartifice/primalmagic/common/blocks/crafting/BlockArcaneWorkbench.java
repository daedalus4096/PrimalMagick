package com.verdantartifice.primalmagic.common.blocks.crafting;

import com.verdantartifice.primalmagic.common.blocks.base.BlockPM;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockArcaneWorkbench extends BlockPM {
    public BlockArcaneWorkbench() {
        super("arcane_workbench", Block.Properties.create(Material.WOOD).hardnessAndResistance(1.5F, 6.0F));
    }
}
