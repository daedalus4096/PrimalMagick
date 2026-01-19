package com.verdantartifice.primalmagick.common.armortrim;

import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import java.util.HashMap;
import java.util.Map;

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

    private static void register(BootstrapContext<TrimMaterial> pContext, ResourceKey<TrimMaterial> pKey, Style pStyle, MaterialAssetGroup pAssets) {
        Component component = Component.translatable(Util.makeDescriptionId("trim_material", pKey.identifier())).withStyle(pStyle);
        pContext.register(pKey, new TrimMaterial(pAssets, component));
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
        register(context, RUNE_EARTH, getStyle(Sources.EARTH), MaterialAssetGroup.EMERALD);         // Use emerald model index
        register(context, RUNE_SEA, getStyle(Sources.SEA), MaterialAssetGroup.LAPIS);               // Use lapis model index
        register(context, RUNE_SKY, getStyle(Sources.SKY), MaterialAssetGroup.DIAMOND);             // Use diamond model index
        register(context, RUNE_SUN, getStyle(Sources.SUN), MaterialAssetGroup.GOLD);                // Use gold model index
        register(context, RUNE_MOON, getStyle(Sources.MOON), MaterialAssetGroup.IRON);              // Use iron model index
        register(context, RUNE_BLOOD, getStyle(Sources.BLOOD), MaterialAssetGroup.REDSTONE);        // Use redstone model index
        register(context, RUNE_INFERNAL, getStyle(Sources.INFERNAL), MaterialAssetGroup.COPPER);    // Use copper model index
        register(context, RUNE_VOID, getStyle(Sources.VOID), MaterialAssetGroup.AMETHYST);          // Use amethyst model index
        register(context, RUNE_HALLOWED, getStyle(Sources.HALLOWED), MaterialAssetGroup.QUARTZ);    // Use quartz model index
    }
}
