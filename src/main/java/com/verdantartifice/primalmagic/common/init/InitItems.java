package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.AncientManaFontTEISR;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagic.common.items.essence.EssenceType;
import com.verdantartifice.primalmagic.common.items.misc.ArcanometerItem;
import com.verdantartifice.primalmagic.common.items.misc.GrimoireItem;
import com.verdantartifice.primalmagic.common.items.wands.ModularWandItem;
import com.verdantartifice.primalmagic.common.items.wands.MundaneWandItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCapItem;
import com.verdantartifice.primalmagic.common.items.wands.WandCoreItem;
import com.verdantartifice.primalmagic.common.items.wands.WandGemItem;
import com.verdantartifice.primalmagic.common.sources.Source;
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
        registry.register(new BlockItem(BlocksPM.MARBLE_SLAB, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SLAB.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_STAIRS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_STAIRS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_WALL, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_WALL.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_BRICKS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_BRICKS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_BRICK_SLAB, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_BRICK_SLAB.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_BRICK_STAIRS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_BRICK_STAIRS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_BRICK_WALL, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_BRICK_WALL.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_PILLAR, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_PILLAR.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_CHISELED, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_CHISELED.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_RUNED, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_RUNED.getRegistryName()));

        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED_SLAB, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED_SLAB.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED_STAIRS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED_STAIRS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED_WALL, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED_WALL.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICKS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED_BRICKS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED_BRICK_SLAB.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED_BRICK_STAIRS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED_BRICK_WALL.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED_PILLAR, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED_PILLAR.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED_CHISELED, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED_CHISELED.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_ENCHANTED_RUNED, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_ENCHANTED_RUNED.getRegistryName()));

        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED_SLAB, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED_SLAB.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED_STAIRS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED_STAIRS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED_WALL, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED_WALL.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED_BRICKS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED_BRICKS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_SLAB, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED_BRICK_SLAB.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED_BRICK_STAIRS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED_BRICK_WALL, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED_BRICK_WALL.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED_PILLAR, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED_PILLAR.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED_CHISELED, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED_CHISELED.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MARBLE_SMOKED_RUNED, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MARBLE_SMOKED_RUNED.getRegistryName()));

        registry.register(new BlockItem(BlocksPM.SUNWOOD_LOG, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.SUNWOOD_LOG.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.STRIPPED_SUNWOOD_LOG, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.STRIPPED_SUNWOOD_LOG.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.SUNWOOD_WOOD, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.SUNWOOD_WOOD.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.STRIPPED_SUNWOOD_WOOD, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.STRIPPED_SUNWOOD_WOOD.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.SUNWOOD_LEAVES, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.SUNWOOD_LEAVES.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.SUNWOOD_SAPLING, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.SUNWOOD_SAPLING.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.SUNWOOD_PLANKS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.SUNWOOD_PLANKS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.SUNWOOD_SLAB, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.SUNWOOD_SLAB.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.SUNWOOD_STAIRS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.SUNWOOD_STAIRS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.SUNWOOD_PILLAR, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.SUNWOOD_PILLAR.getRegistryName()));

        registry.register(new BlockItem(BlocksPM.MOONWOOD_LOG, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MOONWOOD_LOG.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.STRIPPED_MOONWOOD_LOG, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.STRIPPED_MOONWOOD_LOG.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MOONWOOD_WOOD, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MOONWOOD_WOOD.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.STRIPPED_MOONWOOD_WOOD, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.STRIPPED_MOONWOOD_WOOD.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MOONWOOD_LEAVES, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MOONWOOD_LEAVES.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MOONWOOD_SAPLING, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MOONWOOD_SAPLING.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MOONWOOD_PLANKS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MOONWOOD_PLANKS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MOONWOOD_SLAB, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MOONWOOD_SLAB.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MOONWOOD_STAIRS, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MOONWOOD_STAIRS.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.MOONWOOD_PILLAR, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.MOONWOOD_PILLAR.getRegistryName()));

        registry.register(new BlockItem(BlocksPM.ARCANE_WORKBENCH, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.ARCANE_WORKBENCH.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANCIENT_FONT_EARTH, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())).setRegistryName(BlocksPM.ANCIENT_FONT_EARTH.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANCIENT_FONT_SEA, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())).setRegistryName(BlocksPM.ANCIENT_FONT_SEA.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANCIENT_FONT_SKY, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())).setRegistryName(BlocksPM.ANCIENT_FONT_SKY.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANCIENT_FONT_SUN, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())).setRegistryName(BlocksPM.ANCIENT_FONT_SUN.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANCIENT_FONT_MOON, new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).rarity(Rarity.RARE).setTEISR(()->()->new AncientManaFontTEISR())).setRegistryName(BlocksPM.ANCIENT_FONT_MOON.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.WAND_ASSEMBLY_TABLE, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.WAND_ASSEMBLY_TABLE.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.WOOD_TABLE, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.WOOD_TABLE.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.ANALYSIS_TABLE, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.ANALYSIS_TABLE.getRegistryName()));
        registry.register(new BlockItem(BlocksPM.CALCINATOR, new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(BlocksPM.CALCINATOR.getRegistryName()));
    }
    
    public static void initItems(IForgeRegistry<Item> registry) {
        registry.register(new GrimoireItem());
        registry.register(new ArcanometerItem());
        for (Source source : Source.SORTED_SOURCES) {
            for (EssenceType type : EssenceType.values()) {
                registry.register(new EssenceItem(type, source));
            }
        }
        registry.register(new MundaneWandItem());
        registry.register(new ModularWandItem());
        registry.register(new WandCoreItem(WandCore.HEARTWOOD, new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(Rarity.COMMON)));
        registry.register(new WandCapItem(WandCap.IRON, new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(Rarity.COMMON)));
        registry.register(new WandGemItem(WandGem.APPRENTICE, new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(Rarity.COMMON)));
        registry.register(new Item(new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(PrimalMagic.MODID, "magnifying_glass"));
        registry.register(new Item(new Item.Properties().group(PrimalMagic.ITEM_GROUP)).setRegistryName(PrimalMagic.MODID, "alchemical_waste"));
    }
}
