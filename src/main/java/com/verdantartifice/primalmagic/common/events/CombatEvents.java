package com.verdantartifice.primalmagic.common.events;

import java.util.Arrays;
import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.affinities.AffinityManager;
import com.verdantartifice.primalmagic.common.attunements.AttunementManager;
import com.verdantartifice.primalmagic.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns.CooldownType;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagic.common.items.ItemsPM;
import com.verdantartifice.primalmagic.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagic.common.items.essence.EssenceType;
import com.verdantartifice.primalmagic.common.misc.DamageSourcesPM;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellBoltPacket;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;
import com.verdantartifice.primalmagic.common.util.EntityUtils;
import com.verdantartifice.primalmagic.common.util.WeightedRandomBag;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for combat-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class CombatEvents {
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        // Handle effects caused by damage target
        if (event.getEntityLiving() instanceof Player) {
            Player target = (Player)event.getEntityLiving();
            
            // Players with greater infernal attunement are immune to all fire damage
            if (event.getSource().isFire() && AttunementManager.meetsThreshold(target, Source.INFERNAL, AttunementThreshold.GREATER)) {
                event.setCanceled(true);
                return;
            }

            // Attuned players have a chance to turn invisible upon taking damage, if they aren't already
            if (target.level.random.nextDouble() < 0.5D &&
                    !target.hasEffect(MobEffects.INVISIBILITY) && 
                    AttunementManager.meetsThreshold(target, Source.MOON, AttunementThreshold.LESSER)) {
                target.level.playSound(target, target.blockPosition(), SoundsPM.SHIMMER.get(), 
                        SoundSource.PLAYERS, 1.0F, 1.0F + (0.05F * (float)target.level.random.nextGaussian()));
                target.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200));
            }
        }
        
        // Handle effects caused by damage source
        if (event.getSource().getEntity() instanceof Player) {
            Player attacker = (Player)event.getSource().getEntity();
            
            // If the attacker has lesser infernal attunement, launch a hellish chain at the next closest nearby target
            if (!DamageSourcesPM.HELLISH_CHAIN_TYPE.equals(event.getSource().msgId) && 
                    !attacker.level.isClientSide && 
                    AttunementManager.meetsThreshold(attacker, Source.INFERNAL, AttunementThreshold.LESSER)) {
                List<? extends LivingEntity> targets = EntityUtils.getEntitiesInRangeSorted(attacker.level, event.getEntityLiving().position(), 
                        Arrays.asList(event.getEntityLiving(), attacker), LivingEntity.class, 4.0D);
                if (!targets.isEmpty()) {
                    LivingEntity target = targets.get(0);
                    target.hurt(DamageSourcesPM.causeHellishChainDamage(attacker), event.getAmount() / 2.0F);
                    PacketHandler.sendToAllAround(new SpellBoltPacket(event.getEntityLiving().getEyePosition(1.0F), target.getEyePosition(1.0F), Source.INFERNAL.getColor()), 
                            attacker.level.dimension(), event.getEntityLiving().blockPosition(), 64.0D);
                    attacker.level.playSound(null, event.getEntityLiving().blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 1.0F + (float)(attacker.level.random.nextGaussian() * 0.05D));
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        // Handle effects triggered by damage target
        if (event.getEntityLiving() instanceof Player) {
            Player target = (Player)event.getEntityLiving();
            
            // Gain appropriate research for damage sources, if applicable
            if (ResearchManager.isResearchComplete(target, SimpleResearchKey.FIRST_STEPS)) {
                if (event.getSource() == DamageSource.DROWN && !ResearchManager.isResearchComplete(target, SimpleResearchKey.parse("m_drown_a_little"))) {
                    ResearchManager.completeResearch(target, SimpleResearchKey.parse("m_drown_a_little"));
                }
                if (event.getSource() == DamageSource.LAVA && !ResearchManager.isResearchComplete(target, SimpleResearchKey.parse("m_feel_the_burn"))) {
                    ResearchManager.completeResearch(target, SimpleResearchKey.parse("m_feel_the_burn"));
                }
            }

            // Reduce fall damage if the recipient has lesser sky attunement
            if (event.getSource() == DamageSource.FALL && AttunementManager.meetsThreshold(target, Source.SKY, AttunementThreshold.LESSER)) {
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
            if (!event.getSource().isBypassMagic() && AttunementManager.meetsThreshold(target, Source.VOID, AttunementThreshold.LESSER)) {
                event.setAmount(0.9F * event.getAmount());
            }
        }
        
        // Handle effects triggered by the damage source
        if (event.getSource().getEntity() instanceof Player) {
            Player attacker = (Player)event.getSource().getEntity();
            
            // Increase all non-absolute damage dealt by players with greater void attunement
            if (!event.getSource().isBypassMagic() && AttunementManager.meetsThreshold(attacker, Source.VOID, AttunementThreshold.GREATER)) {
                event.setAmount(1.25F * event.getAmount());
            }
            
            // Increase damage to undead targets by players with lesser hallowed attunement
            if (event.getEntityLiving().isInvertedHealAndHarm() && AttunementManager.meetsThreshold(attacker, Source.HALLOWED, AttunementThreshold.LESSER)) {
                event.setAmount(2.0F * event.getAmount());
            }

            // If at least one point of damage was done by a player with the lesser blood attunement, cause bleeding
            if (event.getAmount() >= 1.0F && AttunementManager.meetsThreshold(attacker, Source.BLOOD, AttunementThreshold.LESSER)) {
                event.getEntityLiving().addEffect(new MobEffectInstance(EffectsPM.BLEEDING.get(), 200));
            }
            
            // Players with greater blood attunement can steal health, with a chance based on damage done
            if (attacker.level.random.nextFloat() < (event.getAmount() / 12.0F) && AttunementManager.meetsThreshold(attacker, Source.BLOOD, AttunementThreshold.GREATER)) {
                attacker.heal(1.0F);
            }
        }
    }
    
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        
        // If the player has greater hallowed attunement and it's not on cooldown, cancel death as if using a totem of undying
        if (entity instanceof Player) {
            Player player = (Player)event.getEntityLiving();
            IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
            if (AttunementManager.meetsThreshold(player, Source.HALLOWED, AttunementThreshold.GREATER) &&
                    cooldowns != null &&
                    !cooldowns.isOnCooldown(CooldownType.DEATH_SAVE)) {
                player.setHealth(1.0F);
                player.removeAllEffects();
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                player.addEffect(new MobEffectInstance(EffectsPM.WEAKENED_SOUL.get(), 6000, 0, true, false, true));
                cooldowns.setCooldown(CooldownType.DEATH_SAVE, 6000);
                player.level.playSound(null, player.blockPosition(), SoundsPM.ANGELS.get(), 
                        SoundSource.PLAYERS, 1.0F, 1.0F + (0.05F * (float)player.level.random.nextGaussian()));
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
        
        // If the entity is afflicted with Stolen Essence, drop a sample of its essence
        if (entity.hasEffect(EffectsPM.STOLEN_ESSENCE.get()) && !event.isCanceled()) {
            MobEffectInstance instance = entity.getEffect(EffectsPM.STOLEN_ESSENCE.get());
            SourceList affinities = AffinityManager.getInstance().getAffinityValues(entity.getType());
            if (!affinities.isEmpty()) {
                WeightedRandomBag<Source> bag = new WeightedRandomBag<>();
                for (Source source : affinities.getSources()) {
                    int amount = affinities.getAmount(source);
                    if (amount > 0) {
                        bag.add(source, amount);
                    }
                }
                for (int index = 0; index < instance.getAmplifier() + 1; index++) {
                    Containers.dropItemStack(entity.getCommandSenderWorld(), entity.getX(), entity.getY(), entity.getZ(), EssenceItem.getEssence(EssenceType.DUST, bag.getRandom(entity.getRandom())));
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent<AbstractArrow> event) {
        // If the shooter has the Enderport enchantment, teleport to the hit location
        Entity shooter = event.getProjectile().getOwner();
        if (shooter instanceof LivingEntity && EnchantmentHelperPM.hasEnderport((LivingEntity)shooter)) {
            EntityUtils.teleportEntity((LivingEntity)shooter, event.getProjectile().level, event.getRayTraceResult().getLocation());
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
                    int soulpiercingLevel = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentsPM.SOULPIERCING.get(), livingShooter.getMainHandItem());
                    if (soulpiercingLevel > 0) {
                        MobEffectInstance soulpiercedInstance = new MobEffectInstance(EffectsPM.SOULPIERCED.get(), 12000, 0, false, false);
                        if (target.canBeAffected(soulpiercedInstance) && !target.hasEffect(soulpiercedInstance.getEffect())) {
                            Containers.dropItemStack(target.level, target.getX(), target.getY(), target.getZ(), new ItemStack(ItemsPM.SOUL_GEM_SLIVER.get(), soulpiercingLevel));
                            target.addEffect(soulpiercedInstance);
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onPotionApplicable(PotionEvent.PotionApplicableEvent event) {
        if (event.getPotionEffect().getEffect() == EffectsPM.BLEEDING.get() && event.getEntityLiving().isInvertedHealAndHarm()) {
            // The undead can't bleed
            event.setResult(Result.DENY);
        }
    }
}
