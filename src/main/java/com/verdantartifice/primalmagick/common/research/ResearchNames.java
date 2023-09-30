package com.verdantartifice.primalmagick.common.research;

import java.util.function.Supplier;

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
    
    // Register Fundamentals research
    public static final RegistryObject<ResearchName> FIRST_STEPS = register("first_steps", () -> new ResearchName("FIRST_STEPS"));
}
