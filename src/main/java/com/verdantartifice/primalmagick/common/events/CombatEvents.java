package com.verdantartifice.primalmagick.common.events;

import java.util.Arrays;
import java.util.List;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.attunements.AttunementManager;
import com.verdantartifice.primalmagick.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns.CooldownType;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.damagesource.DamageSourcesPM;
import com.verdantartifice.primalmagick.common.damagesource.DamageTypesPM;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.ResearchNames;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.util.EntitySelectorsPM;
import com.verdantartifice.primalmagick.common.util.EntityUtils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for combat-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagick.MODID)
public class CombatEvents {
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        // Handle effects caused by damage target
        if (event.getEntity() instanceof Player target) {
            // Players with greater infernal attunement are immune to all fire damage
            if (event.getSource().is(DamageTypeTags.IS_FIRE) && AttunementManager.meetsThreshold(target, Source.INFERNAL, AttunementThreshold.GREATER)) {
                event.setCanceled(true);
                return;
            }

            // Attuned players have a chance to turn invisible upon taking damage, if they aren't already
            Level targetLevel = target.level();
            if (targetLevel.random.nextDouble() < 0.5D &&
                    !target.hasEffect(MobEffects.INVISIBILITY) && 
                    AttunementManager.meetsThreshold(target, Source.MOON, AttunementThreshold.LESSER)) {
                targetLevel.playSound(target, target.blockPosition(), SoundsPM.SHIMMER.get(), 
                        SoundSource.PLAYERS, 1.0F, 1.0F + (0.05F * (float)targetLevel.random.nextGaussian()));
                target.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200));
            }
        }
        
        // Handle effects caused by damage source
        if (event.getSource().getEntity() instanceof Player attacker) {
            // If the attacker has lesser infernal attunement, launch a hellish chain at the next closest nearby target
            Level attackerLevel = attacker.level();
            if (!event.getSource().is(DamageTypesPM.HELLISH_CHAIN) && 
                    event.getAmount() > 0.0F && 
                    !attackerLevel.isClientSide && 
                    AttunementManager.meetsThreshold(attacker, Source.INFERNAL, AttunementThreshold.LESSER)) {
                List<LivingEntity> targets = EntityUtils.getEntitiesInRangeSorted(attackerLevel, event.getEntity().position(), 
                        Arrays.asList(event.getEntity(), attacker), LivingEntity.class, 4.0D, EntitySelectorsPM.validHellishChainTarget(attacker));
                if (!targets.isEmpty()) {
                    LivingEntity target = targets.get(0);
                    target.hurt(DamageSourcesPM.hellishChain(attackerLevel, attacker), event.getAmount() / 2.0F);
                    PacketHandler.sendToAllAround(new SpellBoltPacket(event.getEntity().getEyePosition(1.0F), target.getEyePosition(1.0F), Source.INFERNAL.getColor()), 
                            attackerLevel.dimension(), event.getEntity().blockPosition(), 64.0D);
                    attackerLevel.playSound(null, event.getEntity().blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(attackerLevel.random.nextGaussian() * 0.05D));
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        // Handle effects triggered by damage target
        if (event.getEntity() instanceof Player target) {
            // Gain appropriate research for damage sources, if applicable
            if (ResearchManager.isResearchComplete(target, SimpleResearchKey.FIRST_STEPS)) {
                SimpleResearchKey drownKey = ResearchNames.INTERNAL_DROWN_A_LITTLE.get().simpleKey();
                if (event.getSource() == target.damageSources().drown() && !ResearchManager.isResearchComplete(target, drownKey)) {
                    ResearchManager.completeResearch(target, drownKey);
                }
                
                SimpleResearchKey lavaKey = ResearchNames.INTERNAL_FEEL_THE_BURN.get().simpleKey();
                if (event.getSource() == target.damageSources().lava() && !ResearchManager.isResearchComplete(target, lavaKey)) {
                    ResearchManager.completeResearch(target, lavaKey);
                }
            }

            // Reduce fall damage if the recipient has lesser sky attunement
            if (event.getSource() == target.damageSources().fall() && AttunementManager.meetsThreshold(target, Source.SKY, AttunementThreshold.LESSER)) {
                float newDamage = Math.max(0.0F, event.getAmount() / 3.0F - 2.0F);
                if (newDamage < event.getAmount()) {
                    event.setAmount(newDamage);
                }
                if (event.getAmount() < 1.0F) {
                    // If the fall damage was reduced to less than one, cancel it
                    event.setAmount(0.0F);
                    event.setCanceled(true);
                    return;
                }
            }
            
            // Reduce all non-absolute (e.g. starvation) damage taken players with lesser void attunement
            if (!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) && AttunementManager.meetsThreshold(target, Source.VOID, AttunementThreshold.LESSER)) {
                event.setAmount(0.9F * event.getAmount());
            }
            
            // Consume ward before health if damage is non-absolute (e.g. starvation)
            if (!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) && event.getAmount() > 0) {
                PrimalMagickCapabilities.getWard(target).ifPresent(wardCap -> {
                    if (event.getAmount() >= wardCap.getCurrentWard()) {
                        event.setAmount(event.getAmount() - wardCap.getCurrentWard());
                        wardCap.setCurrentWard(0);
                    } else {
                        wardCap.decrementCurrentWard(event.getAmount());
                        event.setAmount(0);
                    }
                    wardCap.pauseRegeneration();
                    if (target instanceof ServerPlayer serverTarget) {
                        wardCap.sync(serverTarget);
                    }
                });
            }
        }
        
        // Handle effects triggered by the damage source
        if (event.getSource().getEntity() instanceof Player) {
            Player attacker = (Player)event.getSource().getEntity();
            Level level = attacker.level();
            
            // Increase all non-absolute damage dealt by players with greater void attunement
            if (!event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) && AttunementManager.meetsThreshold(attacker, Source.VOID, AttunementThreshold.GREATER)) {
                event.setAmount(1.25F * event.getAmount());
            }
            
            // Increase damage to undead targets by players with lesser hallowed attunement
            if (event.getEntity().isInvertedHealAndHarm() && AttunementManager.meetsThreshold(attacker, Source.HALLOWED, AttunementThreshold.LESSER)) {
                event.setAmount(2.0F * event.getAmount());
            }

            // If at least one point of damage was done by a player with the lesser blood attunement, cause bleeding
            if (event.getAmount() >= 1.0F && AttunementManager.meetsThreshold(attacker, Source.BLOOD, AttunementThreshold.LESSER)) {
                event.getEntity().addEffect(new MobEffectInstance(EffectsPM.BLEEDING.get(), 200));
            }
            
            // Players with greater blood attunement can steal health, with a chance based on damage done
            if (level.random.nextFloat() < (event.getAmount() / 12.0F) && AttunementManager.meetsThreshold(attacker, Source.BLOOD, AttunementThreshold.GREATER)) {
                attacker.heal(1.0F);
            }
        }
    }
    
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        
        // If the player has greater hallowed attunement and it's not on cooldown, cancel death as if using a totem of undying
        if (entity instanceof Player player) {
            Level level = player.level();
            IPlayerCooldowns cooldowns = PrimalMagickCapabilities.getCooldowns(player);
            if (AttunementManager.meetsThreshold(player, Source.HALLOWED, AttunementThreshold.GREATER) &&
                    cooldowns != null &&
                    !cooldowns.isOnCooldown(CooldownType.DEATH_SAVE)) {
                player.setHealth(1.0F);
                player.removeAllEffects();
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                player.addEffect(new MobEffectInstance(EffectsPM.WEAKENED_SOUL.get(), 6000, 0, true, false, true));
                cooldowns.setCooldown(CooldownType.DEATH_SAVE, 6000);
                level.playSound(null, player.blockPosition(), SoundsPM.ANGELS.get(), 
                        SoundSource.PLAYERS, 1.0F, 1.0F + (0.05F * (float)level.random.nextGaussian()));
                event.setCanceled(true);
            }
        }
        
        // If the entity is afflicted with Drain Soul, drop some soul gems
        if (entity.hasEffect(EffectsPM.DRAIN_SOUL.get()) && !event.isCanceled()) {
            float gems = entity.getType().getCategory().isFriendly() ? 
                    Mth.sqrt(entity.getMaxHealth()) / 20.0F : 
                    entity.getMaxHealth() / 20.0F;
            int wholeGems = Mth.floor(gems);
            int slivers = Mth.floor(Mth.frac(gems) * 10.0F);
            Containers.dropItemStack(entity.getCommandSenderWorld(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ItemsPM.SOUL_GEM.get(), wholeGems));
            Containers.dropItemStack(entity.getCommandSenderWorld(), entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ItemsPM.SOUL_GEM_SLIVER.get(), slivers));
        }
    }
    
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent event) {
        // If the shooter has the Enderport enchantment, teleport to the hit location
        Entity shooter = event.getProjectile().getOwner();
        if (shooter instanceof LivingEntity && EnchantmentHelperPM.hasEnderport((LivingEntity)shooter)) {
            EntityUtils.teleportEntity((LivingEntity)shooter, event.getProjectile().level(), event.getRayTraceResult().getLocation());
        }

        // Handle the Soulpiercing enchantment
        HitResult rayTraceResult = event.getRayTraceResult();
        if (rayTraceResult.getType() == HitResult.Type.ENTITY) {
            Entity targetEntity = ((EntityHitResult)rayTraceResult).getEntity();
            if (targetEntity instanceof LivingEntity) {
                LivingEntity target = (LivingEntity)targetEntity;
                if (shooter instanceof LivingEntity) {
                    LivingEntity livingShooter = (LivingEntity)shooter;
                    
                    // If the target can have its soul pierced, spawn some soul slivers
                    int soulpiercingLevel = livingShooter.getMainHandItem().getEnchantmentLevel(EnchantmentsPM.SOULPIERCING.get());
                    if (soulpiercingLevel > 0) {
                        MobEffectInstance soulpiercedInstance = new MobEffectInstance(EffectsPM.SOULPIERCED.get(), 12000, 0, false, false);
                        if (target.canBeAffected(soulpiercedInstance) && !target.hasEffect(soulpiercedInstance.getEffect())) {
                            Containers.dropItemStack(target.level(), target.getX(), target.getY(), target.getZ(), new ItemStack(ItemsPM.SOUL_GEM_SLIVER.get(), soulpiercingLevel));
                            target.addEffect(soulpiercedInstance);
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onPotionApplicable(MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect() == EffectsPM.BLEEDING.get() && event.getEntity().isInvertedHealAndHarm()) {
            // The undead can't bleed
            event.setResult(Result.DENY);
        }
    }
}
