package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.items.base.BlockItemPM;
import com.verdantartifice.primalmagic.common.items.misc.DebugTabletItem;
import com.verdantartifice.primalmagic.common.items.misc.GrimoireItem;
import com.verdantartifice.primalmagic.common.items.wands.MundaneWandItem;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class InitItems {
    public static void initBlockItems(IForgeRegistry<Item> registry) {
        registry.register(new BlockItemPM(BlocksPM.MARBLE_RAW));
        registry.register(new BlockItemPM(BlocksPM.ARCANE_WORKBENCH));
    }
    
    public static void initItems(IForgeRegistry<Item> registry) {
        registry.register(new DebugTabletItem());
        registry.register(new GrimoireItem());
        registry.register(new MundaneWandItem());
    }
}
