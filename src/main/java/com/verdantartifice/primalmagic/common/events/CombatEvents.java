package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.attunements.AttunementManager;
import com.verdantartifice.primalmagic.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
    public static void entityHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            if (event.getSource() == DamageSource.FALL && AttunementManager.meetsThreshold(player, Source.SKY, AttunementThreshold.LESSER)) {
                // Reduce fall damage if the recipient has lesser sky attunement
                float newDamage = Math.max(0.0F, event.getAmount() / 3.0F - 2.0F);
                if (newDamage < event.getAmount()) {
                    event.setAmount(newDamage);
                }
            }
            
            // If the damage was reduced to less than one, cancel it
            if (event.getAmount() < 1.0F) {
                event.setAmount(0.0F);
                if (event.isCancelable()) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
