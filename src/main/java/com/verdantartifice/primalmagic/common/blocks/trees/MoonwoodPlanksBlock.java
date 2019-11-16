package com.verdantartifice.primalmagic.common.blocks.trees;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blockstates.properties.TimePhase;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.world.IWorld;

public class MoonwoodPlanksBlock extends AbstractPhasingBlock {
    public MoonwoodPlanksBlock() {
        super(Block.Properties.create(Material.WOOD, MaterialColor.IRON).hardnessAndResistance(2.0F, 3.0F).tickRandomly().sound(SoundType.WOOD));
        this.setRegistryName(PrimalMagic.MODID, "moonwood_planks");
    }

    @Override
    protected TimePhase getCurrentPhase(IWorld world) {
        return TimePhase.getMoonPhase(world);
    }
}
