package com.verdantartifice.primalmagick.common.spells.payloads;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
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
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Definition of a frost damage spell.  Does standard damage to the target and applies a slowness
 * potion effect.  The strength of the slowness effect scales with the payload's power property and
 * its length scales with the duration property.
 * 
 * @author Daedalus4096
 */
public class FrostDamageSpellPayload extends AbstractDamageSpellPayload<FrostDamageSpellPayload> {
    public static final FrostDamageSpellPayload INSTANCE = new FrostDamageSpellPayload();
    
    public static final MapCodec<FrostDamageSpellPayload> CODEC = MapCodec.unit(FrostDamageSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, FrostDamageSpellPayload> STREAM_CODEC = StreamCodec.unit(FrostDamageSpellPayload.INSTANCE);
    
    public static final String TYPE = "frost_damage";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_FROST));

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    public static FrostDamageSpellPayload getInstance() {
        return INSTANCE;
    }
    
    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return ImmutableList.<SpellProperty>builder().addAll(super.getPropertiesInner()).add(SpellPropertiesPM.DURATION.get()).build();
    }

    @Override
    public SpellPayloadType<FrostDamageSpellPayload> getType() {
        return SpellPayloadsPM.FROST_DAMAGE.get();
    }

    @Override
    public Source getSource() {
        return Sources.SEA;
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.ICE.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }

    @Override
    protected float getTotalDamage(Entity target, SpellPackage spell, ItemStack spellSource, HolderLookup.Provider registries) {
        float retVal = super.getTotalDamage(target, spell, spellSource, registries);
        // TODO Make a tag for frost damage sensitivity
        if (target.getType() == EntityType.ENDERMAN) {
            // Endermen are hurt by water
            retVal *= 1.5F;
        }
        return retVal;
    }

    @Override
    protected void applySecondaryEffects(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource) {
        int duration = this.getDurationSeconds(spell, spellSource, world.registryAccess());
        if (target != null && target.getType() == HitResult.Type.ENTITY && duration > 0) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() != null && entityTarget.getEntity() instanceof LivingEntity) {
                int potency = (int)((1.0F + this.getModdedPropertyValue(SpellPropertiesPM.POWER.get(), spell, spellSource, world.registryAccess())) / 3.0F);   // 0, 1, 1, 1, 2
                ((LivingEntity)entityTarget.getEntity()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * duration, potency));
            }
        }
    }
    
    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
        int power = properties.get(SpellPropertiesPM.POWER.get());
        int duration = properties.get(SpellPropertiesPM.DURATION.get());
        return (1 << Math.max(0, power - 1)) + (duration == 0 ? 0 : (1 << Math.max(0, duration - 1)) >> 1);
    }

    protected int getDurationSeconds(SpellPackage spell, ItemStack spellSource, HolderLookup.Provider registries) {
        return 2 * this.getModdedPropertyValue(SpellPropertiesPM.DURATION.get(), spell, spellSource, registries);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource, HolderLookup.Provider registries) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getBaseDamage(spell, spellSource, registries)),
                DECIMAL_FORMATTER.format(this.getDurationSeconds(spell, spellSource, registries)));
    }
}
