package com.verdantartifice.primalmagick.common.spells.payloads;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Definition of a healing spell.  Restores health to the target entity based on the power property of
 * the payload.  If the entity is undead, it instead takes damage based on power.  Has no effect on
 * blocks.
 * 
 * @author Daedalus4096
 */
public class HealingSpellPayload extends AbstractSpellPayload<HealingSpellPayload> {
    public static final HealingSpellPayload INSTANCE = new HealingSpellPayload();
    
    public static final MapCodec<HealingSpellPayload> CODEC = MapCodec.unit(HealingSpellPayload.INSTANCE);
    public static final StreamCodec<ByteBuf, HealingSpellPayload> STREAM_CODEC = StreamCodec.unit(HealingSpellPayload.INSTANCE);
    
    public static final String TYPE = "healing";
    protected static final AbstractRequirement<?> REQUIREMENT = new ResearchRequirement(new ResearchEntryKey(ResearchEntries.SPELL_PAYLOAD_HEALING));
    protected static final Supplier<List<SpellProperty>> PROPERTIES = () -> Arrays.asList(SpellPropertiesPM.POWER.get());

    public static AbstractRequirement<?> getRequirement() {
        return REQUIREMENT;
    }
    
    public static HealingSpellPayload getInstance() {
        return INSTANCE;
    }
    
    @Override
    public SpellPayloadType<HealingSpellPayload> getType() {
        return SpellPayloadsPM.HEALING.get();
    }

    @Override
    protected List<SpellProperty> getPropertiesInner() {
        return PROPERTIES.get();
    }

    protected DamageSource getDamageSource(LivingEntity source, SpellPackage spell, Entity projectileEntity) {
        Level level = source.level();
        if (projectileEntity != null) {
            // If the spell was a projectile or a mine, then it's indirect now matter how it was deployed
            return DamageSourcesPM.sorcery(level.registryAccess(), this.getSource(), projectileEntity, source);
        } else if (spell.vehicle().getComponent().isIndirect()) {
            // If the spell vehicle is indirect but no projectile was given, then it's still indirect
            return DamageSourcesPM.sorcery(level.registryAccess(), this.getSource(), null, source);
        } else {
            // Otherwise, do direct damage
            return DamageSourcesPM.sorcery(level.registryAccess(), this.getSource(), source);
        }
    }

    @Override
    public void execute(HitResult target, Vec3 burstPoint, SpellPackage spell, Level world, LivingEntity caster, ItemStack spellSource, Entity projectileEntity) {
        if (target != null && target.getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityTarget = (EntityHitResult)target;
            if (entityTarget.getEntity() instanceof LivingEntity entity) {
                if (entity.isInvertedHealAndHarm()) {
                    // Undead entities get dealt damage
                    entity.hurt(this.getDamageSource(caster, spell, projectileEntity), 1.5F * this.getBaseAmount(spell, spellSource, caster, world.registryAccess()));
                } else {
                    // All other entities are healed
                    float curHealth = entity.getHealth();
                    float maxHealth = entity.getMaxHealth();
                    float healAmount = (float)this.getBaseAmount(spell, spellSource, caster, world.registryAccess());
                    float overhealing = (curHealth + healAmount) - maxHealth;
                    entity.heal(healAmount);
                    if (overhealing > 0 && overhealing >= entity.getAbsorptionAmount()) {
                        // Grant a level of absorption for each four points of overhealing done
                        int level = (int)Math.floor(overhealing / 4.0F);
                        if (level > 0) {
                            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, level - 1));
                        }
                    }
                }
            }
        }
    }

    @Override
    public Source getSource() {
        return Sources.SUN;
    }

    @Override
    public int getBaseManaCost(SpellPropertyConfiguration properties) {
        int power = properties.get(SpellPropertiesPM.POWER.get());
        return (1 << Math.max(0, power - 1)) + ((1 << Math.max(0, power - 1)) >> 1);
    }

    @Override
    public void playSounds(Level world, BlockPos origin) {
        world.playSound(null, origin, SoundsPM.HEAL.get(), SoundSource.PLAYERS, 1.0F, 1.0F + (float)(world.random.nextGaussian() * 0.05D));
    }

    @Override
    protected String getPayloadType() {
        return TYPE;
    }

    protected int getBaseAmount(SpellPackage spell, ItemStack spellSource, LivingEntity caster, HolderLookup.Provider registries) {
        return 2 * this.getModdedPropertyValue(SpellPropertiesPM.POWER.get(), spell, spellSource, caster, registries);
    }

    @Override
    public Component getDetailTooltip(SpellPackage spell, ItemStack spellSource, LivingEntity caster, HolderLookup.Provider registries) {
        return Component.translatable("spells.primalmagick.payload." + this.getPayloadType() + ".detail_tooltip", DECIMAL_FORMATTER.format(this.getBaseAmount(spell, spellSource, caster, registries)));
    }
}
