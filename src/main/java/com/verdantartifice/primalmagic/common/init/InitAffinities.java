package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.sources.AffinityManager;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

public class InitAffinities {
    public static void initAffinities() {
        initItemAffinities();
        // TODO init entity affinities
    }
    
    protected static void initItemAffinities() {
        AffinityManager.registerAffinities(new ItemStack(Blocks.MYCELIUM), new SourceList().add(Source.EARTH, 5).add(Source.MOON, 10));
    }
}
