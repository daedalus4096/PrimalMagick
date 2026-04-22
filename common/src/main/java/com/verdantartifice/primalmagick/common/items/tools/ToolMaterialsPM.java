package com.verdantartifice.primalmagick.common.items.tools;

import com.verdantartifice.primalmagick.common.tags.ItemTagsPM;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public class ToolMaterialsPM {
    public static final ToolMaterial PRIMALITE = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 800, 7.5F, 2.5F, 18, ItemTagsPM.INGOTS_PRIMALITE);
    public static final ToolMaterial HEXIUM = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1600, 9.5F, 4.0F, 23, ItemTagsPM.INGOTS_HEXIUM);
    public static final ToolMaterial HALLOWSTEEL = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2400, 11.5F, 5.5F, 28, ItemTagsPM.INGOTS_HALLOWSTEEL);
}
