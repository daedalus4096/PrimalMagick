package com.verdantartifice.primalmagick.common.spells;

import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

public class SpellPropertiesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.SPELL_PROPERTIES_REGISTRY.init();
    }

    public static final IRegistryItem<SpellProperty, SpellProperty> POWER = register("power", id -> new SpellProperty(id, "spells.primalmagick.property.power", 1, 5));
    public static final IRegistryItem<SpellProperty, SpellProperty> AMPLIFY_POWER = register("amplify_power", id -> new SpellProperty(id, "spells.primalmagick.property.power", 1, 5));
    public static final IRegistryItem<SpellProperty, SpellProperty> BURST_POWER = register("burst_power", id -> new SpellProperty(id, "spells.primalmagick.property.power", 0, 5));
    public static final IRegistryItem<SpellProperty, SpellProperty> DURATION = register("duration", id -> new SpellProperty(id, "spells.primalmagick.property.duration", 0, 5));
    public static final IRegistryItem<SpellProperty, SpellProperty> RADIUS = register("radius", id -> new SpellProperty(id, "spells.primalmagick.property.radius", 1, 5));
    public static final IRegistryItem<SpellProperty, SpellProperty> FORKS = register("forks", id -> new SpellProperty(id, "spells.primalmagick.property.forks", 2, 5));
    public static final IRegistryItem<SpellProperty, SpellProperty> PRECISION = register("precision", id -> new SpellProperty(id, "spells.primalmagick.property.precision", 0, 5));
    public static final IRegistryItem<SpellProperty, SpellProperty> NON_ZERO_DURATION = register("non_zero_duration", id -> new SpellProperty(id, "spells.primalmagick.property.duration", 1, 5));
    public static final IRegistryItem<SpellProperty, SpellProperty> HASTE = register("haste", id -> new SpellProperty(id, "spells.primalmagick.property.haste", 1, 5));
    public static final IRegistryItem<SpellProperty, SpellProperty> RANGE = register("range", id -> new SpellProperty(id, "spells.primalmagick.property.range", 1, 5));
    public static final IRegistryItem<SpellProperty, SpellProperty> SILK_TOUCH = register("silk_touch", id -> new SpellProperty(id, "spells.primalmagick.property.silk_touch", 0, 1));
    
    protected static IRegistryItem<SpellProperty, SpellProperty> register(String id, Function<ResourceLocation, SpellProperty> propertySupplier) {
        return Services.SPELL_PROPERTIES_REGISTRY.register(id, () -> propertySupplier.apply(ResourceUtils.loc(id)));
    }
    
    @Nullable
    public static SpellProperty get(ResourceLocation id) {
        return Services.SPELL_PROPERTIES_REGISTRY.get(id);
    }
    
    public static Collection<SpellProperty> getAll() {
        return Services.SPELL_PROPERTIES_REGISTRY.getAll();
    }
}
