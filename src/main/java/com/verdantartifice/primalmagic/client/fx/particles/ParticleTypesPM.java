package com.verdantartifice.primalmagic.client.fx.particles;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.RegistryObject;
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
}
