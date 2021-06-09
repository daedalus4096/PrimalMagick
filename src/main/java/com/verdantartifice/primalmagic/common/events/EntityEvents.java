package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
import com.verdantartifice.primalmagic.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;
import com.verdantartifice.primalmagic.common.util.EntityUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.AnimalTameEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handlers for miscellaneous entity events.
 * 
 * @author Daedalus4096
 */
@Mod.EventBusSubscriber(modid=PrimalMagic.MODID)
public class EntityEvents {
    @SubscribeEvent
    public static void onEnderTeleport(EnderTeleportEvent event) {
        // Prevent the teleport if the teleporter is afflicted with Enderlock
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(EffectsPM.ENDERLOCK.get())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onEnderTeleportLowest(EnderTeleportEvent event) {
        // Keep track of the distance teleported for stats
        LivingEntity entity = event.getEntityLiving();
        if (!event.isCanceled() && entity instanceof PlayerEntity) {
            Vector3d start = entity.getPositionVec();
            Vector3d end = new Vector3d(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            StatsManager.incrementValue((PlayerEntity)entity, StatsPM.DISTANCE_TELEPORTED_CM, (int)(100 * start.distanceTo(end)));
        }
    }
    
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent.Arrow event) {
        // If the shooter has the Enderport enchantment, teleport to the hit location
        Entity shooter = event.getArrow().getShooter();
        if (shooter instanceof LivingEntity && EnchantmentHelperPM.hasEnderport((LivingEntity)shooter)) {
            EntityUtils.teleportEntity((LivingEntity)shooter, event.getArrow().world, event.getRayTraceResult().getHitVec());
        }
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onAnimalTameLowest(AnimalTameEvent event) {
        // Grant appropriate research if a player tames a wolf
        PlayerEntity player = event.getTamer();
        if ( !event.isCanceled() &&
             event.getAnimal() instanceof WolfEntity && 
             ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("FIRST_STEPS")) && 
             !ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("m_furry_friend")) ) {
            ResearchManager.completeResearch(player, SimpleResearchKey.parse("m_furry_friend"));
        }
    }
    
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public static void onBabyEntitySpawnLowest(BabyEntitySpawnEvent event) {
        // Grant appropriate research if a player breeds an animal
        PlayerEntity player = event.getCausedByPlayer();
        if ( !event.isCanceled() && 
             player != null &&
             ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("FIRST_STEPS")) &&
             !ResearchManager.isResearchComplete(player, SimpleResearchKey.parse("m_breed_animal")) ) {
            ResearchManager.completeResearch(player, SimpleResearchKey.parse("m_breed_animal"));
        }
    }
}
