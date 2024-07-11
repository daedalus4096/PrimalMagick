package com.verdantartifice.primalmagick.client.fx.particles;

import java.util.function.Function;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.PrimalMagick;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
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
    
    public static final RegistryObject<SimpleParticleType> WAND_POOF = registerSimple("wand_poof", true);
    public static final RegistryObject<SimpleParticleType> MANA_SPARKLE = registerSimple("mana_sparkle", true);
    public static final RegistryObject<SimpleParticleType> SPELL_SPARKLE = registerSimple("spell_sparkle", true);
    public static final RegistryObject<ParticleType<SpellBoltParticleData>> SPELL_BOLT = register("spell_bolt", false, SpellBoltParticleData::codec, SpellBoltParticleData::streamCodec);
    public static final RegistryObject<ParticleType<ItemParticleOption>> OFFERING = register("offering", false, ItemParticleOption::codec, ItemParticleOption::streamCodec);
    public static final RegistryObject<SimpleParticleType> PROP_MARKER = registerSimple("prop_marker", true);
    public static final RegistryObject<ParticleType<PotionExplosionParticleData>> POTION_EXPLOSION = register("potion_explosion", false, PotionExplosionParticleData::codec, PotionExplosionParticleData::streamCodec);
    public static final RegistryObject<ParticleType<NoteEmitterParticleData>> NOTE_EMITTER = register("note_emitter", false, NoteEmitterParticleData::codec, NoteEmitterParticleData::streamCodec);
    public static final RegistryObject<SimpleParticleType> SPELLCRAFTING_RUNE_U = registerSimple("spellcrafting_rune_u", true);
    public static final RegistryObject<SimpleParticleType> SPELLCRAFTING_RUNE_V = registerSimple("spellcrafting_rune_v", true);
    public static final RegistryObject<SimpleParticleType> SPELLCRAFTING_RUNE_T = registerSimple("spellcrafting_rune_t", true);
    public static final RegistryObject<SimpleParticleType> SPELLCRAFTING_RUNE_D = registerSimple("spellcrafting_rune_d", true);
    public static final RegistryObject<SimpleParticleType> INFERNAL_FLAME = registerSimple("infernal_flame", false);
    public static final RegistryObject<SimpleParticleType> AIR_CURRENT = registerSimple("air_current", false);
    public static final RegistryObject<SimpleParticleType> VOID_SMOKE = registerSimple("void_smoke", false);
    public static final RegistryObject<SimpleParticleType> DRIPPING_BLOOD_DROP = registerSimple("dripping_blood_drop", false);
    public static final RegistryObject<SimpleParticleType> FALLING_BLOOD_DROP = registerSimple("falling_blood_drop", false);
    public static final RegistryObject<SimpleParticleType> LANDING_BLOOD_DROP = registerSimple("landing_blood_drop", false);
    
    private static RegistryObject<SimpleParticleType> registerSimple(String name, boolean overrideLimiter) {
        return PARTICLE_TYPES.register(name, () -> new SimpleParticleType(overrideLimiter));
    }
    
    private static <T extends ParticleOptions> RegistryObject<ParticleType<T>> register(String name, boolean overrideLimiter, final Function<ParticleType<T>, MapCodec<T>> codecGetter,
            final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecGetter) {
        return PARTICLE_TYPES.register(name, () -> new ParticleType<T>(overrideLimiter) {
            @Override
            public MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodecGetter.apply(this);
            }
        });
    }
}
