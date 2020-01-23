package com.verdantartifice.primalmagic.common.tiles;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.tiles.crafting.CalcinatorTileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.WandChargerTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * Object holder for mod tile entity types.  Actual values populated by Forge post-registration.
 * 
 * @author Daedalus4096
 */
@ObjectHolder(PrimalMagic.MODID)
public class TileEntityTypesPM {
    public static final TileEntityType<AncientManaFontTileEntity> ANCIENT_MANA_FONT = null;
    public static final TileEntityType<CalcinatorTileEntity> CALCINATOR = null;
    public static final TileEntityType<WandChargerTileEntity> WAND_CHARGER = null;
}
