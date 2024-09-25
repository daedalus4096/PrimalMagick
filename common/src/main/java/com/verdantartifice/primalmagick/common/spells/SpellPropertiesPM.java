package com.verdantartifice.primalmagick.common.spells;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class SpellPropertiesPM {
    private static final DeferredRegister<SpellProperty> DEFERRED_PROPERTIES = DeferredRegister.create(RegistryKeysPM.SPELL_PROPERTIES, Constants.MOD_ID);
    public static final Supplier<IForgeRegistry<SpellProperty>> PROPERTIES = DEFERRED_PROPERTIES.makeRegistry(() -> new RegistryBuilder<SpellProperty>().hasTags());
    
    public static void init() {
        DEFERRED_PROPERTIES.register(PrimalMagick.getModLoadingContext().getModEventBus());
    }
    
    public static final RegistryObject<SpellProperty> POWER = register("power", id -> new SpellProperty(id, "spells.primalmagick.property.power", 1, 5));
    public static final RegistryObject<SpellProperty> AMPLIFY_POWER = register("amplify_power", id -> new SpellProperty(id, "spells.primalmagick.property.power", 1, 5));
    public static final RegistryObject<SpellProperty> BURST_POWER = register("burst_power", id -> new SpellProperty(id, "spells.primalmagick.property.power", 0, 5));
    public static final RegistryObject<SpellProperty> DURATION = register("duration", id -> new SpellProperty(id, "spells.primalmagick.property.duration", 0, 5));
    public static final RegistryObject<SpellProperty> RADIUS = register("radius", id -> new SpellProperty(id, "spells.primalmagick.property.radius", 1, 5));
    public static final RegistryObject<SpellProperty> FORKS = register("forks", id -> new SpellProperty(id, "spells.primalmagick.property.forks", 2, 5));
    public static final RegistryObject<SpellProperty> PRECISION = register("precision", id -> new SpellProperty(id, "spells.primalmagick.property.precision", 0, 5));
    public static final RegistryObject<SpellProperty> NON_ZERO_DURATION = register("non_zero_duration", id -> new SpellProperty(id, "spells.primalmagick.property.duration", 1, 5));
    public static final RegistryObject<SpellProperty> HASTE = register("haste", id -> new SpellProperty(id, "spells.primalmagick.property.haste", 1, 5));
    public static final RegistryObject<SpellProperty> RANGE = register("range", id -> new SpellProperty(id, "spells.primalmagick.property.range", 1, 5));
    public static final RegistryObject<SpellProperty> SILK_TOUCH = register("silk_touch", id -> new SpellProperty(id, "spells.primalmagick.property.silk_touch", 0, 1));
    
    protected static RegistryObject<SpellProperty> register(String id, Function<ResourceLocation, SpellProperty> propertySupplier) {
        return DEFERRED_PROPERTIES.register(id, () -> propertySupplier.apply(ResourceUtils.loc(id)));
    }
    
    @Nullable
    public static SpellProperty get(ResourceLocation id) {
        return PROPERTIES.get().getValue(id);
    }
    
    public static Collection<SpellProperty> getAll() {
        return PROPERTIES.get().getValues();
    }
}
