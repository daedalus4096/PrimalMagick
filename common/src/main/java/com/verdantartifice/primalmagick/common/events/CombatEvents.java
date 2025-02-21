package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.advancements.critereon.CriteriaTriggersPM;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns.CooldownType;
import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.damagesource.DamageTypesPM;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Sources;
import com.verdantartifice.primalmagick.common.util.EntitySelectorsPM;
import com.verdantartifice.primalmagick.common.util.EntityUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Handlers for combat-related events.
 * 
 * @author Daedalus4096
 */
public class CombatEvents {
    public static boolean onAttack(LivingEntity targetEntity, DamageSource damageSource, float amount) {
        // Handle effects caused by damage target
        if (targetEntity instanceof Player target) {
            // Players with greater infernal attunement are immune to all fire damage, and so the event should be cancelled
            if (damageSource.is(DamageTypeTags.IS_FIRE) && AttunementManager.meetsThreshold(target, Sources.INFERNAL, AttunementThreshold.GREATER)) {
                return true;
            }

            // Determine if the damage should be cancelled due to sky attunement fall damage reduction
            if (damageSource == target.damageSources().fall() && AttunementManager.meetsThreshold(target, Sources.SKY, AttunementThreshold.LESSER)) {
                float newDamage = Math.max(0.0F, amount / 3.0F - 2.0F);
                if (newDamage < 1.0F) {
                    // If the fall damage was reduced to less than one, cancel it
                    return true;
                }
            }

            // Attuned players have a chance to turn invisible upon taking damage, if they aren't already
            Level targetLevel = target.level();
            grantInvisibilityOnHurt(target, targetLevel, targetLevel.random);
        }
        
        // Handle effects caused by damage source
        if (damageSource.getEntity() instanceof Player attacker) {
            // If the attacker has lesser infernal attunement, launch a hellish chain at the next closest nearby target
            Level attackerLevel = attacker.level();
            if (!damageSource.is(DamageTypesPM.HELLISH_CHAIN) &&
                    amount > 0.0F &&
                    attackerLevel instanceof ServerLevel serverLevel &&
                    AttunementManager.meetsThreshold(attacker, Sources.INFERNAL, AttunementThreshold.LESSER)) {
                List<LivingEntity> targets = EntityUtils.getEntitiesInRangeSorted(attackerLevel, targetEntity.position(),
                        Arrays.asList(targetEntity, attacker), LivingEntity.class, 4.0D, EntitySelectorsPM.validHellishChainTarget(attacker));
                if (!targets.isEmpty()) {
                    LivingEntity target = targets.getFirst();
                    target.hurt(DamageSourcesPM.hellishChain(attackerLevel, attacker), amount / 2.0F);
                    PacketHandler.sendToAllAround(new SpellBoltPacket(targetEntity.getEyePosition(1.0F), target.getEyePosition(1.0F), Sources.INFERNAL.getColor()),
                            serverLevel, targetEntity.blockPosition(), 64.0D);
                    attackerLevel.playSound(null, targetEntity.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(attackerLevel.random.nextGaussian() * 0.05D));
                }
            }
        }

        // Do not cancel the event
        return false;
    }

    @VisibleForTesting
    public static void grantInvisibilityOnHurt(Player player, Level level, RandomSource random) {
        if (random.nextDouble() < 0.5D &&
                !player.hasEffect(MobEffects.INVISIBILITY) &&
                AttunementManager.meetsThreshold(player, Sources.MOON, AttunementThreshold.LESSER)) {
            level.playSound(player, player.blockPosition(), SoundsPM.SHIMMER.get(),
                    SoundSource.PLAYERS, 1.0F, 1.0F + (0.05F * (float)random.nextGaussian()));
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200));
        }
    }
    
    public static void onEntityHurt(LivingEntity targetEntity, DamageSource damageSource, Supplier<Float> damageGetter, Consumer<Float> damageSetter) {
        // Handle effects triggered by damage target
        if (targetEntity instanceof Player target) {
            // Gain appropriate research for damage sources, if applicable
            if (ResearchManager.isResearchComplete(target, ResearchEntries.FIRST_STEPS)) {
                if (damageSource == target.damageSources().drown() && !ResearchManager.isResearchComplete(target, ResearchEntries.DROWN_A_LITTLE)) {
                    ResearchManager.completeResearch(target, ResearchEntries.DROWN_A_LITTLE);
                }
                if (damageSource == target.damageSources().lava() && !ResearchManager.isResearchComplete(target, ResearchEntries.FEEL_THE_BURN)) {
                    ResearchManager.completeResearch(target, ResearchEntries.FEEL_THE_BURN);
                }
            }

            // Reduce fall damage if the recipient has lesser sky attunement
            if (damageSource == target.damageSources().fall() && AttunementManager.meetsThreshold(target, Sources.SKY, AttunementThreshold.LESSER)) {
                float newDamage = Math.max(0.0F, damageGetter.get() / 3.0F - 2.0F);
                if (newDamage < damageGetter.get()) {
                    damageSetter.accept(newDamage);
                }
            }
            
            // Reduce all non-absolute (e.g. starvation) damage taken players with lesser void attunement
            if (!damageSource.is(DamageTypeTags.BYPASSES_EFFECTS) && AttunementManager.meetsThreshold(target, Sources.VOID, AttunementThreshold.LESSER)) {
                damageSetter.accept(0.9F * damageGetter.get());
            }
            
            // Consume ward before health if damage is non-absolute (e.g. starvation)
            if (!damageSource.is(DamageTypeTags.BYPASSES_EFFECTS) && damageGetter.get() > 0) {
                Services.CAPABILITIES.ward(target).ifPresent(wardCap -> {
                    if (damageGetter.get() >= wardCap.getCurrentWard()) {
                        damageSetter.accept(damageGetter.get() - wardCap.getCurrentWard());
                        wardCap.setCurrentWard(0);
                    } else {
                        wardCap.decrementCurrentWard(damageGetter.get());
                        damageSetter.accept(0F);
                    }
                    wardCap.pauseRegeneration();
                    if (target instanceof ServerPlayer serverTarget) {
                        wardCap.sync(serverTarget);
                    }
                });
            }
        }
        
        // Handle effects triggered by the damage source
        if (damageSource.getEntity() instanceof Player attacker) {
            Level level = attacker.level();
            
            // Increase all non-absolute damage dealt by players with greater void attunement
            if (!damageSource.is(DamageTypeTags.BYPASSES_EFFECTS) && AttunementManager.meetsThreshold(attacker, Sources.VOID, AttunementThreshold.GREATER)) {
                damageSetter.accept(1.25F * damageGetter.get());
            }
            
            // Increase damage to undead targets by players with lesser hallowed attunement
            if (targetEntity.isInvertedHealAndHarm() && AttunementManager.meetsThreshold(attacker, Sources.HALLOWED, AttunementThreshold.LESSER)) {
                damageSetter.accept(2.0F * damageGetter.get());
            }

            // If at least one point of damage was done by a player with the lesser blood attunement, cause bleeding
            if (damageGetter.get() >= 1.0F && AttunementManager.meetsThreshold(attacker, Sources.BLOOD, AttunementThreshold.LESSER)) {
                targetEntity.addEffect(new MobEffectInstance(EffectsPM.BLEEDING.getHolder(), 200));
            }
            
            // Players with greater blood attunement can steal health, with a chance based on damage done
            if (level.random.nextFloat() < (damageGetter.get() / 12.0F) && AttunementManager.meetsThreshold(attacker, Sources.BLOOD, AttunementThreshold.GREATER)) {
                attacker.heal(1.0F);
            }
        }
    }
    
    public static void onEntityHurtLowest(LivingEntity targetEntity, DamageSource damageSource, float amount) {
        // Trigger the appropriate advancement criteria with more data than vanilla provides, namely player data
        if (targetEntity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggersPM.ENTITY_HURT_PLAYER_EXT.get().trigger(serverPlayer, damageSource, amount);
        }
    }
    
    public static boolean onDeath(LivingEntity entity) {
        // If the player has greater hallowed attunement and it's not on cooldown, cancel death as if using a totem of undying
        if (entity instanceof Player player) {
            Level level = player.level();
            if (AttunementManager.meetsThreshold(player, Sources.HALLOWED, AttunementThreshold.GREATER) &&
                    Services.CAPABILITIES.cooldowns(player).map(c -> !c.isOnCooldown(CooldownType.DEATH_SAVE)).orElse(false)) {
                player.setHealth(1.0F);
                player.removeAllEffects();
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                player.addEffect(new MobEffectInstance(EffectsPM.WEAKENED_SOUL.getHolder(), 6000, 0, true, false, true));
                Services.CAPABILITIES.cooldowns(player).ifPresent(c -> c.setCooldown(CooldownType.DEATH_SAVE, 6000));
                level.playSound(null, player.blockPosition(), SoundsPM.ANGELS.get(),
                        SoundSource.PLAYERS, 1.0F, 1.0F + (0.05F * (float)level.random.nextGaussian()));

                // Cancel the event
                return true;
            }
        }
        
        // If the entity is afflicted with Drain Soul, drop some soul gems
        if (entity.hasEffect(EffectsPM.DRAIN_SOUL.getHolder())) {
            float gems = entity.getType().getCategory().isFriendly() ? 
                    Mth.sqrt(entity.getMaxHealth()) / 20.0F : 
                    entity.getMaxHealth() / 20.0F;
            int wholeGems = Mth.floor(gems);
            int slivers = Mth.floor(Mth.frac(gems) * 10.0F);
            Containers.dropItemStack(entity.getCommandSenderWorld(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ItemsPM.SOUL_GEM.get(), wholeGems));
            Containers.dropItemStack(entity.getCommandSenderWorld(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ItemsPM.SOUL_GEM_SLIVER.get(), slivers));
        }

        // Do not cancel the event
        return false;
    }
    
    public static void onArrowImpact(Projectile projectile, HitResult hitResult) {
        // If the shooter has the Enderport enchantment, teleport to the hit location
        // TODO Could the Enderport effect be converted to an enchantment effect component?
        Entity shooter = projectile.getOwner();
        if (shooter instanceof LivingEntity livingShooter && EnchantmentHelperPM.hasEnderport(livingShooter)) {
            EntityUtils.teleportEntity(livingShooter, projectile.level(), hitResult.getLocation());
        }

        // Handle the Soulpiercing enchantment
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            Entity targetEntity = ((EntityHitResult)hitResult).getEntity();
            if (targetEntity instanceof LivingEntity target) {
                if (shooter instanceof LivingEntity livingShooter) {
                    // If the target can have its soul pierced, spawn some soul slivers
                    int soulpiercingLevel = EnchantmentHelperPM.getEnchantmentLevel(livingShooter.getMainHandItem(), EnchantmentsPM.SOULPIERCING, livingShooter.registryAccess());
                    if (soulpiercingLevel > 0) {
                        MobEffectInstance soulpiercedInstance = new MobEffectInstance(EffectsPM.SOULPIERCED.getHolder(), 12000, 0, false, false);
                        if (target.canBeAffected(soulpiercedInstance) && !target.hasEffect(soulpiercedInstance.getEffect())) {
                            Containers.dropItemStack(target.level(), target.getX(), target.getY(), target.getZ(), new ItemStack(ItemsPM.SOUL_GEM_SLIVER.get(), soulpiercingLevel));
                            target.addEffect(soulpiercedInstance);
                        }
                    }
                }
            }
        }
    }
    
    public static boolean isPotionApplicable(LivingEntity livingEntity, MobEffectInstance effectInstance) {
        if (effectInstance.getEffect().is(EffectsPM.BLEEDING.getHolder()) && livingEntity.isInvertedHealAndHarm()) {
            // The undead can't bleed
            return false;
        } else {
            return true;
        }
    }
}
