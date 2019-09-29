package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.AncientManaFontTEISR;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.items.base.BlockItemPM;
import com.verdantartifice.primalmagic.common.items.misc.DebugTabletItem;
import com.verdantartifice.primalmagic.common.items.misc.GrimoireItem;
import com.verdantartifice.primalmagic.common.items.wands.MundaneWandItem;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.registries.IForgeRegistry;

public class InitItems {
    public static void initBlockItems(IForgeRegistry<Item> registry) {
        registry.register(new BlockItemPM(BlocksPM.MARBLE_RAW));
        registry.register(new BlockItemPM(BlocksPM.MARBLE_ENCHANTED));
        registry.register(new BlockItemPM(BlocksPM.ARCANE_WORKBENCH));
        registry.register(new BlockItemPM(BlocksPM.ANCIENT_FONT_EARTH, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())));
        registry.register(new BlockItemPM(BlocksPM.ANCIENT_FONT_SEA, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())));
        registry.register(new BlockItemPM(BlocksPM.ANCIENT_FONT_SKY, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())));
        registry.register(new BlockItemPM(BlocksPM.ANCIENT_FONT_SUN, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())));
        registry.register(new BlockItemPM(BlocksPM.ANCIENT_FONT_MOON, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())));
    }
    
    public static void initItems(IForgeRegistry<Item> registry) {
        registry.register(new DebugTabletItem());
        registry.register(new GrimoireItem());
        registry.register(new MundaneWandItem());
    }
}
