package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
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
        CombatEvents.onEntityHurt(event.getEntity(), event.getSource(), event::getAmount, event::setAmount);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurtLowest(LivingHurtEvent event) {
        if (!event.isCanceled()) {
            CombatEvents.onEntityHurtLowest(event.getEntity(), event.getSource(), event.getAmount());
        }
    }
    
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (!event.isCanceled() && CombatEvents.onDeath(event.getEntity())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent event) {
        CombatEvents.onArrowImpact(event.getProjectile(), event.getRayTraceResult());
    }
    
    @SubscribeEvent
    public static void onPotionApplicable(MobEffectEvent.Applicable event) {
        if (!CombatEvents.isPotionApplicable(event.getEntity(), event.getEffectInstance())) {
            event.setResult(Result.DENY);
        }
    }
}
