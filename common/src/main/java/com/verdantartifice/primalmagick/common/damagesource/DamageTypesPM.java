package com.verdantartifice.primalmagick.common.damagesource;

import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;

import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

/**
 * Registry of mod damage types, backed by datapack JSON.
 * 
 * @author Daedalus4096
 */
public class DamageTypesPM {
    private static final Map<Source, ResourceKey<DamageType>> SORCERY_MAP = new HashMap<>();
    
    public static final ResourceKey<DamageType> BLEEDING = create("bleeding");
    public static final ResourceKey<DamageType> BLOOD_ROSE = create("blood_rose");
    public static final ResourceKey<DamageType> HELLISH_CHAIN = create("hellish_chain");
    public static final ResourceKey<DamageType> SORCERY_EARTH = createSorcery("sorcery_earth", Sources.EARTH);
    public static final ResourceKey<DamageType> SORCERY_SEA = createSorcery("sorcery_sea", Sources.SEA);
    public static final ResourceKey<DamageType> SORCERY_SKY = createSorcery("sorcery_sky", Sources.SKY);
    public static final ResourceKey<DamageType> SORCERY_SUN = createSorcery("sorcery_sun", Sources.SUN);
    public static final ResourceKey<DamageType> SORCERY_MOON = createSorcery("sorcery_moon", Sources.MOON);
    public static final ResourceKey<DamageType> SORCERY_BLOOD = createSorcery("sorcery_blood", Sources.BLOOD);
    public static final ResourceKey<DamageType> SORCERY_INFERNAL = createSorcery("sorcery_infernal", Sources.INFERNAL);
    public static final ResourceKey<DamageType> SORCERY_VOID = createSorcery("sorcery_void", Sources.VOID);
    public static final ResourceKey<DamageType> SORCERY_HALLOWED = createSorcery("sorcery_hallowed", Sources.HALLOWED);
    
    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceUtils.loc(name));
    }
    
    private static ResourceKey<DamageType> createSorcery(String name, Source source) {
        if (source == null || SORCERY_MAP.containsKey(source)) {
            // Don't allow null or duplicate sources in the registry
            return null;
        }
        ResourceKey<DamageType> resourceKey = create(name);
        SORCERY_MAP.put(source, resourceKey);
        return resourceKey;
    }
    
    public static ResourceKey<DamageType> getSorceryType(Source source) {
        return SORCERY_MAP.get(source);
    }
    
    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(BLEEDING, new DamageType(String.join(".", Constants.MOD_ID, "bleeding"), 0.1F));
        context.register(BLOOD_ROSE, new DamageType(String.join(".", Constants.MOD_ID, "blood_rose"), 0.1F));
        context.register(HELLISH_CHAIN, new DamageType(String.join(".", Constants.MOD_ID, "hellish_chain"), 0F));
        context.register(SORCERY_EARTH, new DamageType(String.join(".", Constants.MOD_ID, "sorcery_earth"), 0F));
        context.register(SORCERY_SEA, new DamageType(String.join(".", Constants.MOD_ID, "sorcery_sea"), 0F));
        context.register(SORCERY_SKY, new DamageType(String.join(".", Constants.MOD_ID, "sorcery_sky"), 0F));
        context.register(SORCERY_SUN, new DamageType(String.join(".", Constants.MOD_ID, "sorcery_sun"), 0F));
        context.register(SORCERY_MOON, new DamageType(String.join(".", Constants.MOD_ID, "sorcery_moon"), 0F));
        context.register(SORCERY_BLOOD, new DamageType(String.join(".", Constants.MOD_ID, "sorcery_blood"), 0F));
        context.register(SORCERY_INFERNAL, new DamageType(String.join(".", Constants.MOD_ID, "sorcery_infernal"), 0F));
        context.register(SORCERY_VOID, new DamageType(String.join(".", Constants.MOD_ID, "sorcery_void"), 0F));
        context.register(SORCERY_HALLOWED, new DamageType(String.join(".", Constants.MOD_ID, "sorcery_hallowed"), 0F));
    }
}
