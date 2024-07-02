package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.Arrays;
import java.util.List;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import com.verdantartifice.primalmagick.common.spells.SpellPropertiesPM;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.spells.SpellPropertyConfiguration;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a flight spell.  This payload grants the target a short-lived potion effect which
 * grants creative flight.  The length of the effect scales with the duration property of the
 * payload.  Has no effect on blocks.
 * 
 * @author Daedalus4096
 * @see {@link com.verdantartifice.primalmagick.common.effects.FlyingEffect}
 */
public class FlightSpellPayload extends AbstractSpellPayload<FlightSpellPayload> {
    public static final FlightSpellPayload INSTANCE = new FlightSpellPayload();
    
    public static final MapCodec<FlightSpellPayload> CODEC = MapCodec.unit(FlightSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, FlightSpellPayload> STREAM_CODEC = StreamCodec.unit(FlightSpellPayload.INSTANCE);
    
    public static final String TYPE = "flight";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_FLIGHT));
    protected static final List<SpellProperty> PROPERTIES = Arrays.asList(SpellPropertiesPM.NON_ZERO_DURATION.get());

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    @Override
    public SpellPayloadType<FlightSpellPayload> getType() {
        return SpellPayloadsPM.FLIGHT.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return PROPERTIES;
    }

    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() instanceof LivingEntity) {
                // Grant the potion effect
                LivingEntity entity = (LivingEntity)entityTarget.getEntity();
                int ticks = 20 * this.getDurationSeconds(spell, spellSource);
                entity.addEffect(new MobEffectInstance(EffectsPM.FLYING.get(), ticks));
            }
        }
    }

    @Override
    public Source getSource() {
        return Sources.SKY;
    }

    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
        return 20 * properties.get(SpellPropertiesPM.NON_ZERO_DURATION.get());
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.WINGFLAP.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }
    
    protected int getDurationSeconds(SpellPackage spell, ItemStack spellSource) {
        return 3 * this.getModdedPropertyValue(SpellPropertiesPM.NON_ZERO_DURATION.get(), spell, spellSource);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getDurationSeconds(spell, spellSource)));
    }
}
