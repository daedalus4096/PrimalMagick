package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.research.topics.ResearchTopicFactory;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod menu types.
 * 
 * @author Daedalus4096
 */
public class MenuTypesPM {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PrimalMagick.MODID);
    
    public static void init() {
        MENU_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    /**
     * @deprecated
     */
    @SuppressWarnings("removal")
    @Deprecated(since = "4.0.2", forRemoval = true)
    public static final RegistryObject<MenuType<GrimoireMenu>> GRIMOIRE = MENU_TYPES.register("grimoire", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new GrimoireMenu(windowId, ResearchTopicFactory.decode(data), ResearchTopicFactory.decodeHistory(data));
    }));
    public static final RegistryObject<MenuType<ArcaneWorkbenchMenu>> ARCANE_WORKBENCH = MENU_TYPES.register("arcane_workbench", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new ArcaneWorkbenchMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<WandAssemblyTableMenu>> WAND_ASSEMBLY_TABLE = MENU_TYPES.register("wand_assembly_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandAssemblyTableMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<AnalysisTableMenu>> ANALYSIS_TABLE = MENU_TYPES.register("analysis_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new AnalysisTableMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<CalcinatorMenu>> CALCINATOR = MENU_TYPES.register("calcinator", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new CalcinatorMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<WandInscriptionTableMenu>> WAND_INSCRIPTION_TABLE = MENU_TYPES.register("wand_inscription_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandInscriptionTableMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<SpellcraftingAltarMenu>> SPELLCRAFTING_ALTAR = MENU_TYPES.register("spellcrafting_altar", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new SpellcraftingAltarMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<WandChargerMenu>> WAND_CHARGER = MENU_TYPES.register("wand_charger", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandChargerMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<ResearchTableMenu>> RESEARCH_TABLE = MENU_TYPES.register("research_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new ResearchTableMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<RunescribingAltarBasicMenu>> RUNESCRIBING_ALTAR_BASIC = MENU_TYPES.register("runescribing_altar_basic", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarBasicMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<RunescribingAltarEnchantedMenu>> RUNESCRIBING_ALTAR_ENCHANTED = MENU_TYPES.register("runescribing_altar_enchanted", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarEnchantedMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<RunescribingAltarForbiddenMenu>> RUNESCRIBING_ALTAR_FORBIDDEN = MENU_TYPES.register("runescribing_altar_forbidden", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarForbiddenMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<RunescribingAltarHeavenlyMenu>> RUNESCRIBING_ALTAR_HEAVENLY = MENU_TYPES.register("runescribing_altar_heavenly", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarHeavenlyMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<RunecarvingTableMenu>> RUNECARVING_TABLE = MENU_TYPES.register("runecarving_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunecarvingTableMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<HoneyExtractorMenu>> HONEY_EXTRACTOR = MENU_TYPES.register("honey_extractor", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new HoneyExtractorMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<ConcocterMenu>> CONCOCTER = MENU_TYPES.register("concocter", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new ConcocterMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<EssenceTransmuterMenu>> ESSENCE_TRANSMUTER = MENU_TYPES.register("essence_transmuter", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new EssenceTransmuterMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<DissolutionChamberMenu>> DISSOLUTION_CHAMBER = MENU_TYPES.register("dissolution_chamber", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new DissolutionChamberMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<EssenceCaskMenu>> ESSENCE_CASK = MENU_TYPES.register("essence_cask", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new EssenceCaskMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<WandGlamourTableMenu>> WAND_GLAMOUR_TABLE = MENU_TYPES.register("wand_glamour_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandGlamourTableMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<RunicGrindstoneMenu>> RUNIC_GRINDSTONE = MENU_TYPES.register("runic_grindstone", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunicGrindstoneMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<InfernalFurnaceMenu>> INFERNAL_FURNACE = MENU_TYPES.register("infernal_furnace", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new InfernalFurnaceMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<ManaBatteryMenu>> MANA_BATTERY = MENU_TYPES.register("mana_battery", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new ManaBatteryMenu(windowId, inv, data.readBlockPos());
    }));
}
