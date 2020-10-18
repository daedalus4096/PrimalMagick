package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagic.common.util.EntityUtils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
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
    
    @SubscribeEvent
    public static void onArrowImpact(ProjectileImpactEvent.Arrow event) {
        // If the shooter has the Enderport enchantment, teleport to the hit location
        Entity shooter = event.getArrow().func_234616_v_();
        if (shooter instanceof PlayerEntity && EnchantmentHelperPM.hasEnderport((PlayerEntity)shooter)) {
            EntityUtils.teleportPlayer((PlayerEntity)shooter, event.getArrow().world, event.getRayTraceResult().getHitVec());
        }
    }
}
