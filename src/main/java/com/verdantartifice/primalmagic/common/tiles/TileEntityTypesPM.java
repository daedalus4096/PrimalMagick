package com.verdantartifice.primalmagic.common.tiles;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.tiles.crafting.CalcinatorTileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.WandChargerTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualAltarTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod tile entity types.
 * 
 * @author Daedalus4096
 */
public class TileEntityTypesPM {
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, PrimalMagic.MODID);
    
    public static void init() {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<TileEntityType<AncientManaFontTileEntity>> ANCIENT_MANA_FONT = TILE_ENTITIES.register("ancient_mana_font", () -> TileEntityType.Builder.create(AncientManaFontTileEntity::new, BlocksPM.ANCIENT_FONT_EARTH.get(), BlocksPM.ANCIENT_FONT_SEA.get(), BlocksPM.ANCIENT_FONT_SKY.get(), BlocksPM.ANCIENT_FONT_SUN.get(), BlocksPM.ANCIENT_FONT_MOON.get()).build(null));
    public static final RegistryObject<TileEntityType<CalcinatorTileEntity>> CALCINATOR = TILE_ENTITIES.register("calcinator", () -> TileEntityType.Builder.create(CalcinatorTileEntity::new, BlocksPM.CALCINATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<WandChargerTileEntity>> WAND_CHARGER = TILE_ENTITIES.register("wand_charger", () -> TileEntityType.Builder.create(WandChargerTileEntity::new, BlocksPM.WAND_CHARGER.get()).build(null));
    public static final RegistryObject<TileEntityType<RitualAltarTileEntity>> RITUAL_ALTAR = TILE_ENTITIES.register("ritual_altar", () -> TileEntityType.Builder.create(RitualAltarTileEntity::new, BlocksPM.RITUAL_ALTAR.get()).build(null));
}
