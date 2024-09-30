package com.verdantartifice.primalmagick.common.armortrim;

import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.items.ItemRegistration;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;

/**
 * Registry of mod armor trim materials, backed by datapack JSON.
 * 
 * @author Daedalus4096
 */
public class TrimMaterialsPM {
    protected static final Map<ResourceKey<TrimMaterial>, Source> SOURCE_MAPPING = new HashMap<>();
    
    public static final ResourceKey<TrimMaterial> RUNE_EARTH = registryKey("rune_earth", Sources.EARTH);
    public static final ResourceKey<TrimMaterial> RUNE_SEA = registryKey("rune_sea", Sources.SEA);
    public static final ResourceKey<TrimMaterial> RUNE_SKY = registryKey("rune_sky", Sources.SKY);
    public static final ResourceKey<TrimMaterial> RUNE_SUN = registryKey("rune_sun", Sources.SUN);
    public static final ResourceKey<TrimMaterial> RUNE_MOON = registryKey("rune_moon", Sources.MOON);
    public static final ResourceKey<TrimMaterial> RUNE_BLOOD = registryKey("rune_blood", Sources.BLOOD);
    public static final ResourceKey<TrimMaterial> RUNE_INFERNAL = registryKey("rune_infernal", Sources.INFERNAL);
    public static final ResourceKey<TrimMaterial> RUNE_VOID = registryKey("rune_void", Sources.VOID);
    public static final ResourceKey<TrimMaterial> RUNE_HALLOWED = registryKey("rune_hallowed", Sources.HALLOWED);
    
    private static ResourceKey<TrimMaterial> registryKey(String name, Source source) {
        ResourceKey<TrimMaterial> key = ResourceKey.create(Registries.TRIM_MATERIAL, ResourceUtils.loc(name));
        if (SOURCE_MAPPING.containsKey(key)) {
            throw new IllegalStateException("Source mapping already set for trim material " + name);
        }
        SOURCE_MAPPING.put(key, source);
        return key;
    }
    
    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> materialKey, Item item, Style textStyle, float itemModelIndex) {
        context.register(materialKey, TrimMaterial.create(materialKey.location().getPath(), item, itemModelIndex, Component.translatable(Util.makeDescriptionId("trim_material", materialKey.location())).withStyle(textStyle), Map.of()));
    }
    
    private static Style getStyle(Source source) {
        return Style.EMPTY.withColor(source.getColor());
    }
    
    public static Source getSource(ResourceKey<TrimMaterial> key) {
        if (!SOURCE_MAPPING.containsKey(key)) {
            throw new IllegalArgumentException("No source mapping found for trim material " + key.toString());
        } else {
            return SOURCE_MAPPING.get(key);
        }
    }
    
    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, RUNE_EARTH, ItemsPM.RUNE_EARTH.get(), getStyle(Sources.EARTH), 0.7F);          // Use emerald model index
        register(context, RUNE_SEA, ItemsPM.RUNE_SEA.get(), getStyle(Sources.SEA), 0.9F);                // Use lapis model index
        register(context, RUNE_SKY, ItemsPM.RUNE_SKY.get(), getStyle(Sources.SKY), 0.8F);                // Use diamond model index
        register(context, RUNE_SUN, ItemsPM.RUNE_SUN.get(), getStyle(Sources.SUN), 0.6F);                // Use gold model index
        register(context, RUNE_MOON, ItemsPM.RUNE_MOON.get(), getStyle(Sources.MOON), 0.2F);             // Use iron model index
        register(context, RUNE_BLOOD, ItemsPM.RUNE_BLOOD.get(), getStyle(Sources.BLOOD), 0.4F);          // Use redstone model index
        register(context, RUNE_INFERNAL, ItemsPM.RUNE_INFERNAL.get(), getStyle(Sources.INFERNAL), 0.5F); // Use copper model index
        register(context, RUNE_VOID, ItemsPM.RUNE_VOID.get(), getStyle(Sources.VOID), 1.0F);             // Use amethyst model index
        register(context, RUNE_HALLOWED, ItemsPM.RUNE_HALLOWED.get(), getStyle(Sources.HALLOWED), 0.1F); // Use quartz model index
    }
}
