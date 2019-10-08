package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.AncientManaFontTEISR;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.items.misc.DebugTabletItem;
import com.verdantartifice.primalmagic.common.items.misc.GrimoireItem;
import com.verdantartifice.primalmagic.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagic.common.items.wands.MundaneWandItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagic.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.registries.IForgeRegistry;

public class InitItems {
    public static void initBlockItems(IForgeRegistry<Item> registry) {
        registry.register(new BlockItem(BlocksPM.MARBLE_RAW, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_RAW.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ARCANE_WORKBENCH, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.ARCANE_WORKBENCH.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANCIENT_FONT_EARTH, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())).setRegistryName(BlocksPM.ANCIENT_FONT_EARTH.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANCIENT_FONT_SEA, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())).setRegistryName(BlocksPM.ANCIENT_FONT_SEA.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANCIENT_FONT_SKY, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())).setRegistryName(BlocksPM.ANCIENT_FONT_SKY.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANCIENT_FONT_SUN, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())).setRegistryName(BlocksPM.ANCIENT_FONT_SUN.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANCIENT_FONT_MOON, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())).setRegistryName(BlocksPM.ANCIENT_FONT_MOON.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.WAND_ASSEMBLY_TABLE, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.WAND_ASSEMBLY_TABLE.getRegistryName()));
    }
    
    public static void initItems(IForgeRegistry<Item> registry) {
        registry.register(new DebugTabletItem());
        registry.register(new GrimoireItem());
        registry.register(new MundaneWandItem());
        registry.register(new ModularWandItem());
        registry.register(new WandCoreItem(WandCore.HEARTWOOD, new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(Rarity.COMMON)));
        registry.register(new WandCapItem(WandCap.IRON, new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(Rarity.COMMON)));
        registry.register(new WandGemItem(WandGem.APPRENTICE, new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(Rarity.COMMON)));
    }
}
