package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import com.verdantartifice.primalmagick.common.advancements.critereon.CriteriaTriggersPM;
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
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.sounds.SoundsPM;
import com.verdantartifice.primalmagick.common.sources.Sources;
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
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

/**
 * Forge listeners for combat-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class CombatEventListeners {
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        if (CombatEvents.onAttack(event.getEntity(), event.getSource(), event.getAmount())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onEntityHurt(LivingDamageEvent event) {
        if (CombatEvents.onEntityHurt(event.getEntity(), event.getSource(), event::getAmount, event::setAmount)) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurtLowest(LivingHurtEvent event) {
        // Trigger the appropriate advancement criteria with more data than vanilla provides, namely player data
        if (!event.isCanceled() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            CriteriaTriggersPM.ENTITY_HURT_PLAYER_EXT.trigger(serverPlayer, event.getSource(), event.getAmount());
        }
    }
    
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        
        // If the player has greater hallowed attunement and it's not on cooldown, cancel death as if using a totem of undying
        if (entity instanceof Player player) {
            Level level = player.level();
            IPlayerCooldowns cooldowns = PrimalMagickCapabilities.getCooldowns(player);
            if (AttunementManager.meetsThreshold(player, Sources.HALLOWED, AttunementThreshold.GREATER) &&
                    cooldowns != null &&
                    !cooldowns.isOnCooldown(CooldownType.DEATH_SAVE)) {
                player.setHealth(1.0F);
                player.removeAllEffects();
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                player.addEffect(new MobEffectInstance(EffectsPM.WEAKENED_SOUL.getHolder().get(), 6000, 0, true, false, true));
                cooldowns.setCooldown(CooldownType.DEATH_SAVE, 6000);
                level.playSound(null, player.blockPosition(), SoundsPM.ANGELS.get(), 
                        SoundSource.PLAYERS, 1.0F, 1.0F + (0.05F * (float)level.random.nextGaussian()));
                event.setCanceled(true);
            }
        }
        
        // If the entity is afflicted with Drain Soul, drop some soul gems
        if (entity.hasEffect(EffectsPM.DRAIN_SOUL.getHolder().get()) && !event.isCanceled()) {
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
        // TODO Could the Enderport effect be converted to an enchantment effect component?
        Entity shooter = event.getProjectile().getOwner();
        if (shooter instanceof LivingEntity livingShooter && EnchantmentHelperPM.hasEnderport(livingShooter)) {
            EntityUtils.teleportEntity(livingShooter, event.getProjectile().level(), event.getRayTraceResult().getLocation());
        }

        // Handle the Soulpiercing enchantment
        HitResult rayTraceResult = event.getRayTraceResult();
        if (rayTraceResult.getType() == HitResult.Type.ENTITY) {
            Entity targetEntity = ((EntityHitResult)rayTraceResult).getEntity();
            if (targetEntity instanceof LivingEntity target) {
                if (shooter instanceof LivingEntity livingShooter) {
                    // If the target can have its soul pierced, spawn some soul slivers
                    int soulpiercingLevel = EnchantmentHelperPM.getEnchantmentLevel(livingShooter.getMainHandItem(), EnchantmentsPM.SOULPIERCING, livingShooter.registryAccess());
                    if (soulpiercingLevel > 0) {
                        MobEffectInstance soulpiercedInstance = new MobEffectInstance(EffectsPM.SOULPIERCED.getHolder().get(), 12000, 0, false, false);
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
