package com.verdantartifice.primalmagick.client.fx.particles;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

/**
 * Deferred registry for mod particle types.
 * 
 * @author Daedalus4096
 */
public class ParticleTypesPM {
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> WAND_POOF = registerSimple("wand_poof", true);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> MANA_SPARKLE = registerSimple("mana_sparkle", true);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> SPELL_SPARKLE = registerSimple("spell_sparkle", true);
    public static final IRegistryItem<ParticleType<?>, ParticleType<SpellBoltParticleData>> SPELL_BOLT = register("spell_bolt", false, SpellBoltParticleData::codec, SpellBoltParticleData::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<ItemParticleOption>> OFFERING = register("offering", false, ItemParticleOption::codec, ItemParticleOption::streamCodec);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> PROP_MARKER = registerSimple("prop_marker", true);
    public static final IRegistryItem<ParticleType<?>, ParticleType<PotionExplosionParticleData>> POTION_EXPLOSION = register("potion_explosion", false, PotionExplosionParticleData::codec, PotionExplosionParticleData::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<NoteEmitterParticleData>> NOTE_EMITTER = register("note_emitter", false, NoteEmitterParticleData::codec, NoteEmitterParticleData::streamCodec);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> SPELLCRAFTING_RUNE_U = registerSimple("spellcrafting_rune_u", true);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> SPELLCRAFTING_RUNE_V = registerSimple("spellcrafting_rune_v", true);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> SPELLCRAFTING_RUNE_T = registerSimple("spellcrafting_rune_t", true);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> SPELLCRAFTING_RUNE_D = registerSimple("spellcrafting_rune_d", true);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> INFERNAL_FLAME = registerSimple("infernal_flame", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> AIR_CURRENT = registerSimple("air_current", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> VOID_SMOKE = registerSimple("void_smoke", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> DRIPPING_BLOOD_DROP = registerSimple("dripping_blood_drop", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> FALLING_BLOOD_DROP = registerSimple("falling_blood_drop", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> LANDING_BLOOD_DROP = registerSimple("landing_blood_drop", false);
    
    private static IRegistryItem<ParticleType<?>, SimpleParticleType> registerSimple(String name, boolean overrideLimiter) {
        return Services.PARTICLE_TYPES_REGISTRY.register(name, () -> new SimpleParticleType(overrideLimiter));
    }
    
    private static <T extends ParticleOptions> IRegistryItem<ParticleType<?>, ParticleType<T>> register(String name, boolean overrideLimiter, final Function<ParticleType<T>, MapCodec<T>> codecGetter,
            final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecGetter) {
        return Services.PARTICLE_TYPES_REGISTRY.register(name, () -> new ParticleType<T>(overrideLimiter) {
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
