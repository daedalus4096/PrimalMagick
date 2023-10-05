package com.verdantartifice.primalmagick.client.fx.particles;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Deferred registry for mod particle types.
 * 
 * @author Daedalus4096
 */
public class ParticleTypesPM {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, PrimalMagick.MODID);

    public static void init() {
        PARTICLE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    public static final RegistryObject<SimpleParticleType> WAND_POOF = PARTICLE_TYPES.register("wand_poof", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> MANA_SPARKLE = PARTICLE_TYPES.register("mana_sparkle", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SPELL_SPARKLE = PARTICLE_TYPES.register("spell_sparkle", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<SpellBoltParticleData>> SPELL_BOLT = PARTICLE_TYPES.register("spell_bolt", () -> new ParticleType<SpellBoltParticleData>(false, SpellBoltParticleData.DESERIALIZER) {
        @Override
        public Codec<SpellBoltParticleData> codec() {
            return SpellBoltParticleData.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<ItemParticleOption>> OFFERING = PARTICLE_TYPES.register("offering", () -> new ParticleType<ItemParticleOption>(false, ItemParticleOption.DESERIALIZER) {
        @Override
        public Codec<ItemParticleOption> codec() {
            return ItemParticleOption.codec(this);
        }
    });
    public static final RegistryObject<SimpleParticleType> PROP_MARKER = PARTICLE_TYPES.register("prop_marker", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<PotionExplosionParticleData>> POTION_EXPLOSION = PARTICLE_TYPES.register("potion_explosion", () -> new ParticleType<PotionExplosionParticleData>(false, PotionExplosionParticleData.DESERIALIZER) {
        @Override
        public Codec<PotionExplosionParticleData> codec() {
            return PotionExplosionParticleData.CODEC;
        }
    });
    public static final RegistryObject<ParticleType<NoteEmitterParticleData>> NOTE_EMITTER = PARTICLE_TYPES.register("note_emitter", () -> new ParticleType<NoteEmitterParticleData>(false, NoteEmitterParticleData.DESERIALIZER) {
        @Override
        public Codec<NoteEmitterParticleData> codec() {
            return NoteEmitterParticleData.CODEC;
        }
    });
    public static final RegistryObject<SimpleParticleType> SPELLCRAFTING_RUNE_U = PARTICLE_TYPES.register("spellcrafting_rune_u", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SPELLCRAFTING_RUNE_V = PARTICLE_TYPES.register("spellcrafting_rune_v", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SPELLCRAFTING_RUNE_T = PARTICLE_TYPES.register("spellcrafting_rune_t", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> SPELLCRAFTING_RUNE_D = PARTICLE_TYPES.register("spellcrafting_rune_d", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> INFERNAL_FLAME = PARTICLE_TYPES.register("infernal_flame", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> AIR_CURRENT = PARTICLE_TYPES.register("air_current", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> VOID_SMOKE = PARTICLE_TYPES.register("void_smoke", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> DRIPPING_BLOOD_DROP = PARTICLE_TYPES.register("dripping_blood_drop", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> FALLING_BLOOD_DROP = PARTICLE_TYPES.register("falling_blood_drop", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> LANDING_BLOOD_DROP = PARTICLE_TYPES.register("landing_blood_drop", () -> new SimpleParticleType(false));
}
