package com.verdantartifice.primalmagick.common.research;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod research names.
 * 
 * @author Daedalus4096
 */
public class ResearchNames {
    private static final DeferredRegister<ResearchName> DEFERRED_NAMES = DeferredRegister.create(RegistryKeysPM.RESEARCH_NAMES, PrimalMagick.MODID);
    
    public static final Supplier<IForgeRegistry<ResearchName>> NAMES = DEFERRED_NAMES.makeRegistry(() -> new RegistryBuilder<ResearchName>().hasTags());
    
    public static void init() {
        DEFERRED_NAMES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    protected static RegistryObject<ResearchName> register(String id, Supplier<ResearchName> nameSupplier) {
        return DEFERRED_NAMES.register(id, nameSupplier);
    }
    
    @Nonnull
    public static ResearchName find(String name) {
        return NAMES.get().getValues().stream().filter(rn -> rn.matches(name)).findFirst().orElseThrow(() -> new IllegalArgumentException("Unrecognized research name"));
    }
    
    // Register Fundamentals research
    public static final RegistryObject<ResearchName> FIRST_STEPS = register("first_steps", () -> new ResearchName("FIRST_STEPS"));
    public static final RegistryObject<ResearchName> THEORYCRAFTING = register("theorycrafting", () -> new ResearchName("THEORYCRAFTING"));
    public static final RegistryObject<ResearchName> ATTUNEMENTS = register("attunements", () -> new ResearchName("ATTUNEMENTS"));
    public static final RegistryObject<ResearchName> UNLOCK_MANAWEAVING = register("unlock_manaweaving", () -> new ResearchName("UNLOCK_MANAWEAVING"));
    public static final RegistryObject<ResearchName> UNLOCK_ALCHEMY = register("unlock_alchemy", () -> new ResearchName("UNLOCK_ALCHEMY"));
    public static final RegistryObject<ResearchName> UNLOCK_SORCERY = register("unlock_sorcery", () -> new ResearchName("UNLOCK_SORCERY"));
    public static final RegistryObject<ResearchName> UNLOCK_RUNEWORKING = register("unlock_runeworking", () -> new ResearchName("UNLOCK_RUNEWORKING"));
    public static final RegistryObject<ResearchName> UNLOCK_RITUAL = register("unlock_ritual", () -> new ResearchName("UNLOCK_RITUAL"));
    public static final RegistryObject<ResearchName> UNLOCK_MAGITECH = register("unlock_magitech", () -> new ResearchName("UNLOCK_MAGITECH"));
    public static final RegistryObject<ResearchName> UNLOCK_SCANS = register("unlock_scans", () -> new ResearchName("UNLOCK_SCANS"));
    public static final RegistryObject<ResearchName> TERRESTRIAL_MAGICK = register("terrestrial_magick", () -> new ResearchName("TERRESTRIAL_MAGICK"));
    public static final RegistryObject<ResearchName> FORBIDDEN_MAGICK = register("forbidden_magick", () -> new ResearchName("FORBIDDEN_MAGICK"));
    public static final RegistryObject<ResearchName> HEAVENLY_MAGICK = register("heavenly_magick", () -> new ResearchName("HEAVENLY_MAGICK"));
    public static final RegistryObject<ResearchName> SOURCE_EARTH = register("source_earth", () -> new ResearchName("SOURCE_EARTH"));
    public static final RegistryObject<ResearchName> SOURCE_SEA = register("source_sea", () -> new ResearchName("SOURCE_SEA"));
    public static final RegistryObject<ResearchName> SOURCE_SKY = register("source_sky", () -> new ResearchName("SOURCE_SKY"));
    public static final RegistryObject<ResearchName> SOURCE_SUN = register("source_sun", () -> new ResearchName("SOURCE_SUN"));
    public static final RegistryObject<ResearchName> SOURCE_MOON = register("source_moon", () -> new ResearchName("SOURCE_MOON"));
    public static final RegistryObject<ResearchName> SOURCE_BLOOD = register("source_blood", () -> new ResearchName("SOURCE_BLOOD"));
    public static final RegistryObject<ResearchName> SOURCE_INFERNAL = register("source_infernal", () -> new ResearchName("SOURCE_INFERNAL"));
    public static final RegistryObject<ResearchName> SOURCE_VOID = register("source_void", () -> new ResearchName("SOURCE_VOID"));
    public static final RegistryObject<ResearchName> SOURCE_HALLOWED = register("source_hallowed", () -> new ResearchName("SOURCE_HALLOWED"));
    public static final RegistryObject<ResearchName> SECRETS_OF_THE_UNIVERSE = register("secrets_of_the_universe", () -> new ResearchName("SECRETS_OF_THE_UNIVERSE"));
    public static final RegistryObject<ResearchName> COMPLETE_BASICS = register("complete_basics", () -> new ResearchName("COMPLETE_BASICS"));
    public static final RegistryObject<ResearchName> THEORY_OF_EVERYTHING = register("theory_of_everything", () -> new ResearchName("THEORY_OF_EVERYTHING"));
}
