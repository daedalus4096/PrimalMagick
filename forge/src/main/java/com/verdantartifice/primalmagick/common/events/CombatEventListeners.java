package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.Constants;
import net.minecraftforge.common.util.Result;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.listener.Priority;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge listeners for combat-related events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class CombatEventListeners {
    @SubscribeEvent
    public static boolean onAttack(LivingAttackEvent event) {
        return CombatEvents.onAttack(event.getEntity(), event.getSource(), event.getAmount());
    }
    
    @SubscribeEvent
    public static void onEntityHurt(LivingDamageEvent event) {
        CombatEvents.onEntityHurt(event.getEntity(), event.getSource(), event::getAmount, event::setAmount);
    }
    
    @SubscribeEvent(priority = Priority.LOWEST)
    public static void onEntityHurtLowest(LivingHurtEvent event) {
        CombatEvents.onEntityHurtLowest(event.getEntity(), event.getSource(), event.getAmount());
    }
    
    @SubscribeEvent
    public static boolean onDeath(LivingDeathEvent event) {
        return CombatEvents.onDeath(event.getEntity());
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
