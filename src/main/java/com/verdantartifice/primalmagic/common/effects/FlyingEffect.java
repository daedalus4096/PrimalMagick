package com.verdantartifice.primalmagic.common.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.GameType;

/**
 * Definition for a potion effect type that grants creative flight for the duration.
 * 
 * @author Daedalus4096
 */
public class FlyingEffect extends Effect {
    public FlyingEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        if (!entityLivingBaseIn.world.isRemote && entityLivingBaseIn instanceof ServerPlayerEntity) {
            // Set the allowFlying player ability when this effect is applied and send the change to clients
            ServerPlayerEntity player = (ServerPlayerEntity)entityLivingBaseIn;
            player.abilities.allowFlying = true;
            player.sendPlayerAbilities();
        }
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }
    
    @Override
    public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        if (!entityLivingBaseIn.world.isRemote && entityLivingBaseIn instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity)entityLivingBaseIn;
            GameType type = player.world.getWorldInfo().getGameType();
            player.abilities.allowFlying = (type == GameType.CREATIVE || type == GameType.SPECTATOR);   // Cancel flight ability if not appropriate for game mode
            if (!player.abilities.allowFlying) {
                // If flying is no longer allowed, end the player's flight
                player.abilities.isFlying = false;
            }
            player.sendPlayerAbilities();   // Send ability changes to clients
        }
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }
}
