package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.keys.ResearchEntryKey;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.research.requirements.ResearchRequirement;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition for a flame damage spell.  Does standard damage to the target as fire damage and sets
 * the target on fire.  The length of the fire effect scales with the duration property of the payload.
 * 
 * @author Daedalus4096
 */
public class FlameDamageSpellPayload extends AbstractDamageSpellPayload<FlameDamageSpellPayload> {
    public static final FlameDamageSpellPayload INSTANCE = new FlameDamageSpellPayload();
    
    public static final MapCodec<FlameDamageSpellPayload> CODEC = MapCodec.unit(FlameDamageSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, FlameDamageSpellPayload> STREAM_CODEC = StreamCodec.unit(FlameDamageSpellPayload.INSTANCE);
    
    public static final String TYPE = "flame_damage";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_FLAME));

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.<SpellProperty>builder().addAll(super.getPropertiesInner()).add(SpellPropertiesPM.DURATION.get()).build();
    }

    @Override
    public SpellPayloadType<FlameDamageSpellPayload> getType() {
        return SpellPayloadsPM.FLAME_DAMAGE.get();
    }

    @Override
    public Source getSource() {
        return Sources.INFERNAL;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }
    
    @Override
    protected String getPayloadType() {
        return TYPE;
    }

    @Override
    protected void applySecondaryEffects(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        int duration = this.getDurationSeconds(spell, spellSource);
        if (target != null && target.getType() == HitResult.Type.ENTITY && duration > 0) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() != null && entityTarget.getEntity() instanceof LivingEntity entity) {
                // Set the entity on fire
                entity.igniteForSeconds(duration);
            }
        }
    }
    
    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
        int power = properties.get(SpellPropertiesPM.POWER.get());
        int duration = properties.get(SpellPropertiesPM.DURATION.get());
        return (1 << Math.max(0, power - 1)) + (duration == 0 ? 0 : (1 << Math.max(0, duration - 1)) >> 1);
    }
    
    protected int getDurationSeconds(SpellPackage spell, ItemStack spellSource) {
        return 2 * this.getModdedPropertyValue(SpellPropertiesPM.DURATION.get(), spell, spellSource);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getBaseDamage(spell, spellSource)),
                DECIMAL_FORMATTER.format(this.getDurationSeconds(spell, spellSource)));
    }
}
