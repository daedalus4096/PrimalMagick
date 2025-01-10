package com.verdantartifice.primalmagick.common.menus;

import com.verdantartifice.primalmagick.common.menus.base.IMenuFactory;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

/**
 * Deferred registry for mod menu types.
 * 
 * @author Daedalus4096
 */
public class MenuTypesPM {
    public static final IRegistryItem<MenuType<?>, MenuType<ArcaneWorkbenchMenu>> ARCANE_WORKBENCH = register("arcane_workbench", (windowId, inv, data) -> new ArcaneWorkbenchMenu(windowId, inv));
    public static final IRegistryItem<MenuType<?>, MenuType<WandAssemblyTableMenu>> WAND_ASSEMBLY_TABLE = register("wand_assembly_table", (windowId, inv, data) -> new WandAssemblyTableMenu(windowId, inv));
    public static final IRegistryItem<MenuType<?>, MenuType<AnalysisTableMenu>> ANALYSIS_TABLE = register("analysis_table", (windowId, inv, data) -> new AnalysisTableMenu(windowId, inv));
    public static final IRegistryItem<MenuType<?>, MenuType<CalcinatorMenu>> CALCINATOR = register("calcinator", (windowId, inv, data) -> new CalcinatorMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<WandInscriptionTableMenu>> WAND_INSCRIPTION_TABLE = register("wand_inscription_table", (windowId, inv, data) -> new WandInscriptionTableMenu(windowId, inv));
    public static final IRegistryItem<MenuType<?>, MenuType<SpellcraftingAltarMenu>> SPELLCRAFTING_ALTAR = register("spellcrafting_altar", (windowId, inv, data) -> new SpellcraftingAltarMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<WandChargerMenu>> WAND_CHARGER = register("wand_charger", (windowId, inv, data) -> new WandChargerMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<ResearchTableMenu>> RESEARCH_TABLE = register("research_table", (windowId, inv, data) -> new ResearchTableMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<RunescribingAltarBasicMenu>> RUNESCRIBING_ALTAR_BASIC = register("runescribing_altar_basic", (windowId, inv, data) -> new RunescribingAltarBasicMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<RunescribingAltarEnchantedMenu>> RUNESCRIBING_ALTAR_ENCHANTED = register("runescribing_altar_enchanted", (windowId, inv, data) -> new RunescribingAltarEnchantedMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<RunescribingAltarForbiddenMenu>> RUNESCRIBING_ALTAR_FORBIDDEN = register("runescribing_altar_forbidden", (windowId, inv, data) -> new RunescribingAltarForbiddenMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<RunescribingAltarHeavenlyMenu>> RUNESCRIBING_ALTAR_HEAVENLY = register("runescribing_altar_heavenly", (windowId, inv, data) -> new RunescribingAltarHeavenlyMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<RunecarvingTableMenu>> RUNECARVING_TABLE = register("runecarving_table", (windowId, inv, data) -> new RunecarvingTableMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<HoneyExtractorMenu>> HONEY_EXTRACTOR = register("honey_extractor", (windowId, inv, data) -> new HoneyExtractorMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<ConcocterMenu>> CONCOCTER = register("concocter", (windowId, inv, data) -> new ConcocterMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<EssenceTransmuterMenu>> ESSENCE_TRANSMUTER = register("essence_transmuter", (windowId, inv, data) -> new EssenceTransmuterMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<DissolutionChamberMenu>> DISSOLUTION_CHAMBER = register("dissolution_chamber", (windowId, inv, data) -> new DissolutionChamberMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<EssenceCaskMenu>> ESSENCE_CASK = register("essence_cask", (windowId, inv, data) -> new EssenceCaskMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<WandGlamourTableMenu>> WAND_GLAMOUR_TABLE = register("wand_glamour_table", (windowId, inv, data) -> new WandGlamourTableMenu(windowId, inv));
    public static final IRegistryItem<MenuType<?>, MenuType<RunicGrindstoneMenu>> RUNIC_GRINDSTONE = register("runic_grindstone", (windowId, inv, data) -> new RunicGrindstoneMenu(windowId, inv));
    public static final IRegistryItem<MenuType<?>, MenuType<InfernalFurnaceMenu>> INFERNAL_FURNACE = register("infernal_furnace", (windowId, inv, data) -> new InfernalFurnaceMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<ManaBatteryMenu>> MANA_BATTERY = register("mana_battery", (windowId, inv, data) -> new ManaBatteryMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<ScribeStudyVocabularyMenu>> SCRIBE_STUDY_VOCABULARY = register("scribe_study_vocabulary", (windowId, inv, data) -> new ScribeStudyVocabularyMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<ScribeGainComprehensionMenu>> SCRIBE_GAIN_COMPREHENSION = register("scribe_gain_comprehension", (windowId, inv, data) -> new ScribeGainComprehensionMenu(windowId, inv, data.readBlockPos()));
    public static final IRegistryItem<MenuType<?>, MenuType<ScribeTranscribeWorksMenu>> SCRIBE_TRANSCRIBE_WORKS = register("scribe_transcribe_works", (windowId, inv, data) -> new ScribeTranscribeWorksMenu(windowId, inv, data.readBlockPos()));

    private static <T extends AbstractContainerMenu> IRegistryItem<MenuType<?>, MenuType<T>> register(String name, IMenuFactory<T> factory) {
        return Services.MENU_TYPES_REGISTRY.register(name, factory);
    }
}
