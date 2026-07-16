package com.verdantartifice.primalmagick.client.fx.particles;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.registries.IRegistryItem;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Deferred registry for mod particle types.
 * 
 * @author Daedalus4096
 */
public class ParticleTypesPM {
    public static void init() {
        // Pass the service initialization through this class so it gets class loaded and fields registered
        Services.PARTICLE_TYPES_REGISTRY.init();
    }

    public static final IRegistryItem<ParticleType<?>, ParticleType<ColorParticleOption>> WAND_POOF = register("wand_poof", true, ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<ManaSparkleParticleData>> MANA_SPARKLE = register("mana_sparkle", true, ManaSparkleParticleData::codec, ManaSparkleParticleData::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<ColorParticleOption>> SPELL_SPARKLE = register("spell_sparkle", true, ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<SpellBoltParticleData>> SPELL_BOLT = register("spell_bolt", false, SpellBoltParticleData::codec, SpellBoltParticleData::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<ItemParticleOption>> OFFERING = register("offering", false, ItemParticleOption::codec, ItemParticleOption::streamCodec);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> PROP_MARKER = registerSimple("prop_marker", true);
    public static final IRegistryItem<ParticleType<?>, ParticleType<PotionExplosionParticleData>> POTION_EXPLOSION = register("potion_explosion", false, PotionExplosionParticleData::codec, PotionExplosionParticleData::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<NoteEmitterParticleData>> NOTE_EMITTER = register("note_emitter", false, NoteEmitterParticleData::codec, NoteEmitterParticleData::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<ColorParticleOption>> SPELLCRAFTING_RUNE_U = register("spellcrafting_rune_u", true, ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<ColorParticleOption>> SPELLCRAFTING_RUNE_V = register("spellcrafting_rune_v", true, ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<ColorParticleOption>> SPELLCRAFTING_RUNE_T = register("spellcrafting_rune_t", true, ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final IRegistryItem<ParticleType<?>, ParticleType<ColorParticleOption>> SPELLCRAFTING_RUNE_D = register("spellcrafting_rune_d", true, ColorParticleOption::codec, ColorParticleOption::streamCodec);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> INFERNAL_FLAME = registerSimple("infernal_flame", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> AIR_CURRENT = registerSimple("air_current", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> VOID_SMOKE = registerSimple("void_smoke", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> DRIPPING_BLOOD_DROP = registerSimple("dripping_blood_drop", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> FALLING_BLOOD_DROP = registerSimple("falling_blood_drop", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> LANDING_BLOOD_DROP = registerSimple("landing_blood_drop", false);
    public static final IRegistryItem<ParticleType<?>, SimpleParticleType> LINGUISTICS = registerSimple("linguistics", false);
    
    private static IRegistryItem<ParticleType<?>, SimpleParticleType> registerSimple(String name, boolean overrideLimiter) {
        return Services.PARTICLE_TYPES_REGISTRY.register(name, () -> new SimpleParticleType(overrideLimiter));
    }
    
    private static <T extends ParticleOptions> IRegistryItem<ParticleType<?>, ParticleType<T>> register(String name, boolean overrideLimiter, final Function<ParticleType<T>, MapCodec<T>> codecGetter,
            final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecGetter) {
        return Services.PARTICLE_TYPES_REGISTRY.register(name, () -> new ParticleType<T>(overrideLimiter) {
            @Override
            @NotNull
            public MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            @Override
            @NotNull
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodecGetter.apply(this);
            }
        });
    }
}
