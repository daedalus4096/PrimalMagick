package com.verdantartifice.primalmagick.common.spells;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class SpellPropertiesPM {
    private static final DeferredRegister<SpellProperty> DEFERRED_PROPERTIES = DeferredRegister.create(RegistryKeysPM.SPELL_PROPERTIES, PrimalMagick.MODID);
    public static final Supplier<IForgeRegistry<SpellProperty>> PROPERTIES = DEFERRED_PROPERTIES.makeRegistry(() -> new RegistryBuilder<SpellProperty>().hasTags());
    
    public static void init() {
        DEFERRED_PROPERTIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<SpellProperty> POWER = register("power", () -> new SpellProperty("power", "spells.primalmagick.property.power", 1, 5));
    public static final RegistryObject<SpellProperty> BURST_POWER = register("burst_power", () -> new SpellProperty("power", "spells.primalmagick.property.power", 0, 5));
    public static final RegistryObject<SpellProperty> DURATION = register("duration", () -> new SpellProperty("duration", "spells.primalmagick.property.duration", 0, 5));
    
    protected static RegistryObject<SpellProperty> register(String id, Supplier<SpellProperty> propertySupplier) {
        return DEFERRED_PROPERTIES.register(id, propertySupplier);
    }
}
