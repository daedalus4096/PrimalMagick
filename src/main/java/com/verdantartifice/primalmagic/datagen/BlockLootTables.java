package com.verdantartifice.primalmagic.datagen;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;

import net.minecraft.data.DataGenerator;

public class BlockLootTables extends BlockLootTableProvider {
    public BlockLootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        this.lootTables.put(BlocksPM.ANALYSIS_TABLE, this.createBasicTable(BlocksPM.ANALYSIS_TABLE));
    }
    
    @Override
    public String getName() {
        return "Primal Magic Block Loot Tables";
    }
}
