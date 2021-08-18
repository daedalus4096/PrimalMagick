package com.verdantartifice.primalmagic.common.tiles;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.blocks.BlocksPM;
import com.verdantartifice.primalmagic.common.tiles.crafting.CalcinatorTileEntity;
import com.verdantartifice.primalmagic.common.tiles.crafting.ConcocterTileEntity;
import com.verdantartifice.primalmagic.common.tiles.crafting.EssenceFurnaceTileEntity;
import com.verdantartifice.primalmagic.common.tiles.crafting.RunescribingAltarTileEntity;
import com.verdantartifice.primalmagic.common.tiles.devices.HoneyExtractorTileEntity;
import com.verdantartifice.primalmagic.common.tiles.devices.SanguineCrucibleTileEntity;
import com.verdantartifice.primalmagic.common.tiles.devices.SunlampTileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.AncientManaFontTileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.ArtificialManaFontTileEntity;
import com.verdantartifice.primalmagic.common.tiles.mana.WandChargerTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.BloodletterTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.CelestialHarpTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.IncenseBrazierTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.OfferingPedestalTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualAltarTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualBellTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualCandleTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.RitualLecternTileEntity;
import com.verdantartifice.primalmagic.common.tiles.rituals.SoulAnvilTileEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod tile entity types.
 * 
 * @author Daedalus4096
 */
public class TileEntityTypesPM {
    private static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, PrimalMagic.MODID);
    
    public static void init() {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<BlockEntityType<AncientManaFontTileEntity>> ANCIENT_MANA_FONT = TILE_ENTITIES.register("ancient_mana_font", () -> BlockEntityType.Builder.of(AncientManaFontTileEntity::new, BlocksPM.ANCIENT_FONT_EARTH.get(), BlocksPM.ANCIENT_FONT_SEA.get(), BlocksPM.ANCIENT_FONT_SKY.get(), BlocksPM.ANCIENT_FONT_SUN.get(), BlocksPM.ANCIENT_FONT_MOON.get()).build(null));
    public static final RegistryObject<BlockEntityType<ArtificialManaFontTileEntity>> ARTIFICIAL_MANA_FONT = TILE_ENTITIES.register("artificial_mana_font", () -> BlockEntityType.Builder.of(ArtificialManaFontTileEntity::new, BlocksPM.ARTIFICIAL_FONT_EARTH.get(), BlocksPM.ARTIFICIAL_FONT_SEA.get(), BlocksPM.ARTIFICIAL_FONT_SKY.get(), BlocksPM.ARTIFICIAL_FONT_SUN.get(), BlocksPM.ARTIFICIAL_FONT_MOON.get(), BlocksPM.ARTIFICIAL_FONT_BLOOD.get(), BlocksPM.ARTIFICIAL_FONT_INFERNAL.get(), BlocksPM.ARTIFICIAL_FONT_VOID.get(), BlocksPM.ARTIFICIAL_FONT_HALLOWED.get(), BlocksPM.FORBIDDEN_FONT_EARTH.get(), BlocksPM.FORBIDDEN_FONT_SEA.get(), BlocksPM.FORBIDDEN_FONT_SKY.get(), BlocksPM.FORBIDDEN_FONT_SUN.get(), BlocksPM.FORBIDDEN_FONT_MOON.get(), BlocksPM.FORBIDDEN_FONT_BLOOD.get(), BlocksPM.FORBIDDEN_FONT_INFERNAL.get(), BlocksPM.FORBIDDEN_FONT_VOID.get(), BlocksPM.FORBIDDEN_FONT_HALLOWED.get()).build(null));
    public static final RegistryObject<BlockEntityType<EssenceFurnaceTileEntity>> ESSENCE_FURNACE = TILE_ENTITIES.register("essence_furnace", () -> BlockEntityType.Builder.of(EssenceFurnaceTileEntity::new, BlocksPM.ESSENCE_FURNACE.get()).build(null));
    public static final RegistryObject<BlockEntityType<CalcinatorTileEntity>> CALCINATOR = TILE_ENTITIES.register("calcinator", () -> BlockEntityType.Builder.of(CalcinatorTileEntity::new, BlocksPM.CALCINATOR_BASIC.get(), BlocksPM.CALCINATOR_ENCHANTED.get(), BlocksPM.CALCINATOR_FORBIDDEN.get(), BlocksPM.CALCINATOR_HEAVENLY.get()).build(null));
    public static final RegistryObject<BlockEntityType<WandChargerTileEntity>> WAND_CHARGER = TILE_ENTITIES.register("wand_charger", () -> BlockEntityType.Builder.of(WandChargerTileEntity::new, BlocksPM.WAND_CHARGER.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualAltarTileEntity>> RITUAL_ALTAR = TILE_ENTITIES.register("ritual_altar", () -> BlockEntityType.Builder.of(RitualAltarTileEntity::new, BlocksPM.RITUAL_ALTAR.get()).build(null));
    public static final RegistryObject<BlockEntityType<SunlampTileEntity>> SUNLAMP = TILE_ENTITIES.register("sunlamp", () -> BlockEntityType.Builder.of(SunlampTileEntity::new, BlocksPM.SUNLAMP.get(), BlocksPM.SPIRIT_LANTERN.get()).build(null));
    public static final RegistryObject<BlockEntityType<OfferingPedestalTileEntity>> OFFERING_PEDESTAL = TILE_ENTITIES.register("offering_pedestal", () -> BlockEntityType.Builder.of(OfferingPedestalTileEntity::new, BlocksPM.OFFERING_PEDESTAL.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualCandleTileEntity>> RITUAL_CANDLE = TILE_ENTITIES.register("ritual_candle", () -> BlockEntityType.Builder.of(RitualCandleTileEntity::new, BlocksPM.RITUAL_CANDLE_BLACK.get(), BlocksPM.RITUAL_CANDLE_BLUE.get(), BlocksPM.RITUAL_CANDLE_BROWN.get(), BlocksPM.RITUAL_CANDLE_CYAN.get(), BlocksPM.RITUAL_CANDLE_GRAY.get(), BlocksPM.RITUAL_CANDLE_GREEN.get(), BlocksPM.RITUAL_CANDLE_LIGHT_BLUE.get(), BlocksPM.RITUAL_CANDLE_LIGHT_GRAY.get(), BlocksPM.RITUAL_CANDLE_LIME.get(), BlocksPM.RITUAL_CANDLE_MAGENTA.get(), BlocksPM.RITUAL_CANDLE_ORANGE.get(), BlocksPM.RITUAL_CANDLE_PINK.get(), BlocksPM.RITUAL_CANDLE_PURPLE.get(), BlocksPM.RITUAL_CANDLE_RED.get(), BlocksPM.RITUAL_CANDLE_WHITE.get(), BlocksPM.RITUAL_CANDLE_YELLOW.get()).build(null));
    public static final RegistryObject<BlockEntityType<IncenseBrazierTileEntity>> INCENSE_BRAZIER = TILE_ENTITIES.register("incense_brazier", () -> BlockEntityType.Builder.of(IncenseBrazierTileEntity::new, BlocksPM.INCENSE_BRAZIER.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualLecternTileEntity>> RITUAL_LECTERN = TILE_ENTITIES.register("ritual_lectern", () -> BlockEntityType.Builder.of(RitualLecternTileEntity::new, BlocksPM.RITUAL_LECTERN.get()).build(null));
    public static final RegistryObject<BlockEntityType<RitualBellTileEntity>> RITUAL_BELL = TILE_ENTITIES.register("ritual_bell", () -> BlockEntityType.Builder.of(RitualBellTileEntity::new, BlocksPM.RITUAL_BELL.get()).build(null));
    public static final RegistryObject<BlockEntityType<BloodletterTileEntity>> BLOODLETTER = TILE_ENTITIES.register("bloodletter", () -> BlockEntityType.Builder.of(BloodletterTileEntity::new, BlocksPM.BLOODLETTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<SoulAnvilTileEntity>> SOUL_ANVIL = TILE_ENTITIES.register("soul_anvil", () -> BlockEntityType.Builder.of(SoulAnvilTileEntity::new, BlocksPM.SOUL_ANVIL.get()).build(null));
    public static final RegistryObject<BlockEntityType<RunescribingAltarTileEntity>> RUNESCRIBING_ALTAR = TILE_ENTITIES.register("runescribing_altar", () -> BlockEntityType.Builder.of(RunescribingAltarTileEntity::new, BlocksPM.RUNESCRIBING_ALTAR_BASIC.get(), BlocksPM.RUNESCRIBING_ALTAR_ENCHANTED.get(), BlocksPM.RUNESCRIBING_ALTAR_FORBIDDEN.get(), BlocksPM.RUNESCRIBING_ALTAR_HEAVENLY.get()).build(null));
    public static final RegistryObject<BlockEntityType<HoneyExtractorTileEntity>> HONEY_EXTRACTOR = TILE_ENTITIES.register("honey_extractor", () -> BlockEntityType.Builder.of(HoneyExtractorTileEntity::new, BlocksPM.HONEY_EXTRACTOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<SanguineCrucibleTileEntity>> SANGUINE_CRUCIBLE = TILE_ENTITIES.register("sanguine_crucible", () -> BlockEntityType.Builder.of(SanguineCrucibleTileEntity::new, BlocksPM.SANGUINE_CRUCIBLE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ConcocterTileEntity>> CONCOCTER = TILE_ENTITIES.register("concocter", () -> BlockEntityType.Builder.of(ConcocterTileEntity::new, BlocksPM.CONCOCTER.get()).build(null));
    public static final RegistryObject<BlockEntityType<CelestialHarpTileEntity>> CELESTIAL_HARP = TILE_ENTITIES.register("celestial_harp", () -> BlockEntityType.Builder.of(CelestialHarpTileEntity::new, BlocksPM.CELESTIAL_HARP.get()).build(null));
}
