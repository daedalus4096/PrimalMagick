package com.verdantartifice.primalmagic.common.events;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.attunements.AttunementManager;
import com.verdantartifice.primalmagic.common.attunements.AttunementThreshold;
import com.verdantartifice.primalmagic.common.effects.EffectsPM;
import com.verdantartifice.primalmagic.common.sounds.SoundsPM;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
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
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)event.getEntityLiving();
            if (player.world.rand.nextDouble() < 0.5D &&
                    !player.isPotionActive(Effects.INVISIBILITY) && 
                    AttunementManager.meetsThreshold(player, Source.MOON, AttunementThreshold.LESSER)) {
                // Attuned players have a chance to turn invisible upon taking damage, if they aren't already
                player.world.playSound(player, player.getPosition(), SoundsPM.SHIMMER.get(), 
                        SoundCategory.PLAYERS, 1.0F, 1.0F + (0.05F * (float)player.world.rand.nextGaussian()));
                player.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 200));
            }
        }
    }
    
    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        // Handle effects triggered by damage target
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity target = (PlayerEntity)event.getEntityLiving();
            if (event.getSource() == DamageSource.FALL && AttunementManager.meetsThreshold(target, Source.SKY, AttunementThreshold.LESSER)) {
                // Reduce fall damage if the recipient has lesser sky attunement
                float newDamage = Math.max(0.0F, event.getAmount() / 3.0F - 2.0F);
                if (newDamage < event.getAmount()) {
                    event.setAmount(newDamage);
                }
                
                // If the fall damage was reduced to less than one, cancel it
                if (event.getAmount() < 1.0F) {
                    event.setAmount(0.0F);
                    event.setCanceled(true);
                    return;
                }
            }
        }
        
        // Handle effects triggered by the damage source
        if (event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity attacker = (PlayerEntity)event.getSource().getTrueSource();
            if (event.getAmount() >= 1.0F && AttunementManager.meetsThreshold(attacker, Source.BLOOD, AttunementThreshold.LESSER)) {
                // If at least one point of damage was done by a player with the lesser blood attunement, cause bleeding
                event.getEntityLiving().addPotionEffect(new EffectInstance(EffectsPM.BLEEDING.get(), 200));
            }
        }
    }
    
    @SubscribeEvent
    public static void onPotionApplicable(PotionEvent.PotionApplicableEvent event) {
        if (event.getPotionEffect().getPotion() == EffectsPM.BLEEDING.get() && event.getEntityLiving().isEntityUndead()) {
            // The undead can't bleed
            event.setResult(Result.DENY);
        }
    }
}
