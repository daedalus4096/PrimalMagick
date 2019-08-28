package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.items.base.BlockItemPM;
import com.verdantartifice.primalmagic.common.items.misc.DebugTabletItem;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class InitItems {
    public static void initBlockItems(IForgeRegistry<Item> registry) {
        registry.register(new BlockItemPM(BlocksPM.MARBLE_RAW));
    }
    
    public static void initItems(IForgeRegistry<Item> registry) {
        registry.register(new DebugTabletItem());
    }
}
