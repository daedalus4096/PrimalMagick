package com.verdantartifice.primalmagic.common.init;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;

public class InitTileEntities {
    public static void initTileEntityTypes(IForgeRegistry<TileEntityType<?>> registry) {
        registry.register(TileEntityType.Builder.create(AncientManaFontTileEntity::new, BlocksPM.ANCIENT_FONT_EARTH, BlocksPM.ANCIENT_FONT_SEA, BlocksPM.ANCIENT_FONT_SKY, BlocksPM.ANCIENT_FONT_SUN, BlocksPM.ANCIENT_FONT_MOON).build(null).setRegistryName(PrimalMagic.MODID, "ancient_mana_font"));
    }
}
