package com.verdantartifice.primalmagic.client.fx.particles;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Deferred registry for mod particle types.
 * 
 * @author Daedalus4096
 */
@OnlyIn(Dist.CLIENT)
public class ParticleTypesPM {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, PrimalMagic.MODID);

    public static void init() {
        PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<BasicParticleType> WAND_POOF = PARTICLE_TYPES.register("wand_poof", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> MANA_SPARKLE = PARTICLE_TYPES.register("mana_sparkle", () -> new BasicParticleType(true));
    public static final RegistryObject<BasicParticleType> SPELL_SPARKLE = PARTICLE_TYPES.register("spell_sparkle", () -> new BasicParticleType(true));
    public static final RegistryObject<ParticleType<SpellBoltParticleData>> SPELL_BOLT = PARTICLE_TYPES.register("spell_bolt", () -> new ParticleType<SpellBoltParticleData>(false, SpellBoltParticleData.DESERIALIZER) {
        @Override
        public Codec<SpellBoltParticleData> func_230522_e_() {
            return SpellBoltParticleData.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<ItemParticleData>> OFFERING = PARTICLE_TYPES.register("offering", () -> new ParticleType<ItemParticleData>(false, ItemParticleData.DESERIALIZER) {
        @Override
        public Codec<ItemParticleData> func_230522_e_() {
            return ItemParticleData.func_239809_a_(this);
        }
    });
    public static final RegistryObject<BasicParticleType> PROP_MARKER = PARTICLE_TYPES.register("prop_marker", () -> new BasicParticleType(true));
    public static final RegistryObject<ParticleType<PotionExplosionParticleData>> POTION_EXPLOSION = PARTICLE_TYPES.register("potion_explosion", () -> new ParticleType<PotionExplosionParticleData>(false, PotionExplosionParticleData.DESERIALIZER) {
        @Override
        public Codec<PotionExplosionParticleData> func_230522_e_() {
            return PotionExplosionParticleData.CODEC;
        }
    });
}
