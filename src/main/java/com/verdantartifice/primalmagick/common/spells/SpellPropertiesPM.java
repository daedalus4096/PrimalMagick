package com.verdantartifice.primalmagick.common.spells;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.resources.ResourceLocation;
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
    
    public static final RegistryObject<SpellProperty> POWER = register("power", id -> new SpellProperty(id, "spells.primalmagick.property.power", 1, 5));
    public static final RegistryObject<SpellProperty> AMPLIFY_POWER = register("amplify_power", id -> new SpellProperty(id, "spells.primalmagick.property.power", 1, 5));
    public static final RegistryObject<SpellProperty> BURST_POWER = register("burst_power", id -> new SpellProperty(id, "spells.primalmagick.property.power", 0, 5));
    public static final RegistryObject<SpellProperty> DURATION = register("duration", id -> new SpellProperty(id, "spells.primalmagick.property.duration", 0, 5));
    
    protected static RegistryObject<SpellProperty> register(String id, Function<ResourceLocation, SpellProperty> propertySupplier) {
        return DEFERRED_PROPERTIES.register(id, () -> propertySupplier.apply(PrimalMagick.resource(id)));
    }
    
    @Nullable
    public static SpellProperty get(ResourceLocation id) {
        return PROPERTIES.get().getValue(id);
    }
    
    public static Collection<SpellProperty> getAll() {
        return PROPERTIES.get().getValues();
    }
}
