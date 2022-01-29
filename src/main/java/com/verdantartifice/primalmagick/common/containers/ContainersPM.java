package com.verdantartifice.primalmagick.common.containers;

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
public class ContainersPM {
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, PrimalMagick.MODID);
    
    public static void init() {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<MenuType<GrimoireContainer>> GRIMOIRE = CONTAINERS.register("grimoire", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new GrimoireContainer(windowId, ResearchTopicFactory.decode(data), ResearchTopicFactory.decodeHistory(data));
    }));
    public static final RegistryObject<MenuType<ArcaneWorkbenchContainer>> ARCANE_WORKBENCH = CONTAINERS.register("arcane_workbench", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new ArcaneWorkbenchContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<WandAssemblyTableContainer>> WAND_ASSEMBLY_TABLE = CONTAINERS.register("wand_assembly_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandAssemblyTableContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<AnalysisTableContainer>> ANALYSIS_TABLE = CONTAINERS.register("analysis_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new AnalysisTableContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<CalcinatorContainer>> CALCINATOR = CONTAINERS.register("calcinator", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new CalcinatorContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<WandInscriptionTableContainer>> WAND_INSCRIPTION_TABLE = CONTAINERS.register("wand_inscription_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandInscriptionTableContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<SpellcraftingAltarContainer>> SPELLCRAFTING_ALTAR = CONTAINERS.register("spellcrafting_altar", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new SpellcraftingAltarContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<WandChargerContainer>> WAND_CHARGER = CONTAINERS.register("wand_charger", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new WandChargerContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<ResearchTableContainer>> RESEARCH_TABLE = CONTAINERS.register("research_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new ResearchTableContainer(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<RunescribingAltarBasicContainer>> RUNESCRIBING_ALTAR_BASIC = CONTAINERS.register("runescribing_altar_basic", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarBasicContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<RunescribingAltarEnchantedContainer>> RUNESCRIBING_ALTAR_ENCHANTED = CONTAINERS.register("runescribing_altar_enchanted", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarEnchantedContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<RunescribingAltarForbiddenContainer>> RUNESCRIBING_ALTAR_FORBIDDEN = CONTAINERS.register("runescribing_altar_forbidden", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarForbiddenContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<RunescribingAltarHeavenlyContainer>> RUNESCRIBING_ALTAR_HEAVENLY = CONTAINERS.register("runescribing_altar_heavenly", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunescribingAltarHeavenlyContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<RunecarvingTableContainer>> RUNECARVING_TABLE = CONTAINERS.register("runecarving_table", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new RunecarvingTableContainer(windowId, inv, data.readBlockPos());
    }));
    public static final RegistryObject<MenuType<HoneyExtractorContainer>> HONEY_EXTRACTOR = CONTAINERS.register("honey_extractor", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new HoneyExtractorContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<ConcocterContainer>> CONCOCTER = CONTAINERS.register("concocter", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new ConcocterContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<EssenceTransmuterContainer>> ESSENCE_TRANSMUTER = CONTAINERS.register("essence_transmuter", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new EssenceTransmuterContainer(windowId, inv);
    }));
    public static final RegistryObject<MenuType<DissolutionChamberContainer>> DISSOLUTION_CHAMBER = CONTAINERS.register("dissolution_chamber", () -> IForgeMenuType.create((windowId, inv, data) -> {
        return new DissolutionChamberContainer(windowId, inv);
    }));
}
