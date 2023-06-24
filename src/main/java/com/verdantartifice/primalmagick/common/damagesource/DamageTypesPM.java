package com.verdantartifice.primalmagick.common.damagesource;

import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

/**
 * Registry of mod damage types, backed by datapack JSON.
 * 
 * @author Daedalus4096
 */
public class DamageTypesPM {
    public static final ResourceKey<DamageType> BLEEDING = create("bleeding");
    public static final ResourceKey<DamageType> HELLISH_CHAIN = create("hellish_chain");
    public static final ResourceKey<DamageType> SORCERY = create("sorcery");
    
    public static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(PrimalMagick.MODID, name));
    }
    
    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(BLEEDING, new DamageType(String.join(".", PrimalMagick.MODID, "bleeding"), 0.1F));
        context.register(HELLISH_CHAIN, new DamageType(String.join(".", PrimalMagick.MODID, "hellish_chain"), 0F));
        context.register(SORCERY, new DamageType(String.join(".", PrimalMagick.MODID, "sorcery"), 0F));
    }
}
