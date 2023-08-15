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
 * Deferred registry for mod containers.
 * 
 * @author Daedalus4096
 */
public class MenuTypesPM {
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PrimalMagick.MODID);
    
    public static void init() {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<MenuType<GrimoireMenu>> GRIMOIRE = CONTAINERS.register("grimoire", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new GrimoireMenu(windowId, ResearchTopicFactory.decode(data), ResearchTopicFactory.decodeHistory(data));
    }));
    public static final RegistryObject<MenuType<ArcaneWorkbenchMenu>> ARCANE_WORKBENCH = CONTAINERS.register("arcane_workbench", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new ArcaneWorkbenchMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<WandAssemblyTableMenu>> WAND_ASSEMBLY_TABLE = CONTAINERS.register("wand_assembly_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandAssemblyTableMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<AnalysisTableMenu>> ANALYSIS_TABLE = CONTAINERS.register("analysis_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new AnalysisTableMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<CalcinatorMenu>> CALCINATOR = CONTAINERS.register("calcinator", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new CalcinatorMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<WandInscriptionTableMenu>> WAND_INSCRIPTION_TABLE = CONTAINERS.register("wand_inscription_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandInscriptionTableMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<SpellcraftingAltarMenu>> SPELLCRAFTING_ALTAR = CONTAINERS.register("spellcrafting_altar", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new SpellcraftingAltarMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<WandChargerMenu>> WAND_CHARGER = CONTAINERS.register("wand_charger", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandChargerMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<ResearchTableMenu>> RESEARCH_TABLE = CONTAINERS.register("research_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new ResearchTableMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<RunescribingAltarBasicMenu>> RUNESCRIBING_ALTAR_BASIC = CONTAINERS.register("runescribing_altar_basic", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarBasicMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<RunescribingAltarEnchantedMenu>> RUNESCRIBING_ALTAR_ENCHANTED = CONTAINERS.register("runescribing_altar_enchanted", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarEnchantedMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<RunescribingAltarForbiddenMenu>> RUNESCRIBING_ALTAR_FORBIDDEN = CONTAINERS.register("runescribing_altar_forbidden", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarForbiddenMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<RunescribingAltarHeavenlyMenu>> RUNESCRIBING_ALTAR_HEAVENLY = CONTAINERS.register("runescribing_altar_heavenly", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarHeavenlyMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<RunecarvingTableMenu>> RUNECARVING_TABLE = CONTAINERS.register("runecarving_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunecarvingTableMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<HoneyExtractorMenu>> HONEY_EXTRACTOR = CONTAINERS.register("honey_extractor", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new HoneyExtractorMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<ConcocterMenu>> CONCOCTER = CONTAINERS.register("concocter", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new ConcocterMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<EssenceTransmuterMenu>> ESSENCE_TRANSMUTER = CONTAINERS.register("essence_transmuter", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new EssenceTransmuterMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<DissolutionChamberMenu>> DISSOLUTION_CHAMBER = CONTAINERS.register("dissolution_chamber", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new DissolutionChamberMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<EssenceCaskMenu>> ESSENCE_CASK = CONTAINERS.register("essence_cask", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new EssenceCaskMenu(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<WandGlamourTableMenu>> WAND_GLAMOUR_TABLE = CONTAINERS.register("wand_glamour_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandGlamourTableMenu(windowId, inv);
    }));
    public static final RegistryObject<MenuType<RunicGrindstoneMenu>> RUNIC_GRINDSTONE = CONTAINERS.register("runic_grindstone", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunicGrindstoneMenu(windowId, inv);
    }));
}
