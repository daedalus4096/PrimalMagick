package com.verdantartifice.primalmagic.common.tiles;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.tiles.crafting.CalcinatorTileEntity;
import com.verdantartifice.primalmagic.common.tiles.devices.SunlampTileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.WandChargerTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.BloodletterTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.IncenseBrazierTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualAltarTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualBellTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualCandleTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualLecternTileEntity;

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
    public static final RegistryObject<TileEntityType<SunlampTileEntity>> SUNLAMP = TILE_ENTITIES.register("sunlamp", () -> TileEntityType.Builder.create(SunlampTileEntity::new, BlocksPM.SUNLAMP.get()).build(null));
    public static final RegistryObject<TileEntityType<OfferingPedestalTileEntity>> OFFERING_PEDESTAL = TILE_ENTITIES.register("offering_pedestal", () -> TileEntityType.Builder.create(OfferingPedestalTileEntity::new, BlocksPM.OFFERING_PEDESTAL.get()).build(null));
    public static final RegistryObject<TileEntityType<RitualCandleTileEntity>> RITUAL_CANDLE = TILE_ENTITIES.register("ritual_candle", () -> TileEntityType.Builder.create(RitualCandleTileEntity::new, BlocksPM.RITUAL_CANDLE_BLACK.get(), BlocksPM.RITUAL_CANDLE_BLUE.get(), BlocksPM.RITUAL_CANDLE_BROWN.get(), BlocksPM.RITUAL_CANDLE_CYAN.get(), BlocksPM.RITUAL_CANDLE_GRAY.get(), BlocksPM.RITUAL_CANDLE_GREEN.get(), BlocksPM.RITUAL_CANDLE_LIGHT_BLUE.get(), BlocksPM.RITUAL_CANDLE_LIGHT_GRAY.get(), BlocksPM.RITUAL_CANDLE_LIME.get(), BlocksPM.RITUAL_CANDLE_MAGENTA.get(), BlocksPM.RITUAL_CANDLE_ORANGE.get(), BlocksPM.RITUAL_CANDLE_PINK.get(), BlocksPM.RITUAL_CANDLE_PURPLE.get(), BlocksPM.RITUAL_CANDLE_RED.get(), BlocksPM.RITUAL_CANDLE_WHITE.get(), BlocksPM.RITUAL_CANDLE_YELLOW.get()).build(null));
    public static final RegistryObject<TileEntityType<IncenseBrazierTileEntity>> INCENSE_BRAZIER = TILE_ENTITIES.register("incense_brazier", () -> TileEntityType.Builder.create(IncenseBrazierTileEntity::new, BlocksPM.INCENSE_BRAZIER.get()).build(null));
    public static final RegistryObject<TileEntityType<RitualLecternTileEntity>> RITUAL_LECTERN = TILE_ENTITIES.register("ritual_lectern", () -> TileEntityType.Builder.create(RitualLecternTileEntity::new, BlocksPM.RITUAL_LECTERN.get()).build(null));
    public static final RegistryObject<TileEntityType<RitualBellTileEntity>> RITUAL_BELL = TILE_ENTITIES.register("ritual_bell", () -> TileEntityType.Builder.create(RitualBellTileEntity::new, BlocksPM.RITUAL_BELL.get()).build(null));
    public static final RegistryObject<TileEntityType<BloodletterTileEntity>> BLOODLETTER = TILE_ENTITIES.register("bloodletter", () -> TileEntityType.Builder.create(BloodletterTileEntity::new, BlocksPM.BLOODLETTER.get()).build(null));
}
