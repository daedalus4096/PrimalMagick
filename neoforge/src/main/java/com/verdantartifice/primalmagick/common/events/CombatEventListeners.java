package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

/**
 * Neoforge listeners for combat-related events.
 * 
 * @author Daedalus4096
 */
@EventBusSubscriber(modid = Constants.MOD_ID)
public class CombatEventListeners {
    @SubscribeEvent
    public static void onAttack(LivingIncomingDamageEvent event) {
        if (CombatEvents.onAttack(event.getEntity(), event.getSource(), event.getAmount())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onEntityHurt(LivingDamageEvent.Pre event) {
        // The Neoforge version of this event cannot be cancelled, so ignore the return value
        CombatEvents.onEntityHurt(event.getEntity(), event.getSource(), event::getNewDamage, event::setNewDamage);
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityHurtLowest(LivingDamageEvent.Post event) {
        // The Neoforge version of this event cannot be cancelled, so don't check
        CombatEvents.onEntityHurtLowest(event.getEntity(), event.getSource(), event.getNewDamage());
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
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}
