package com.verdantartifice.primalmagic.common.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.GameType;

/**
 * Definition for a potion effect type that grants creative flight for the duration.
 * 
 * @author Daedalus4096
 */
public class FlyingEffect extends MobEffect {
    public FlyingEffect(MobEffectCategory typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        if (!entityLivingBaseIn.level.isClientSide && entityLivingBaseIn instanceof ServerPlayer) {
            // Set the allowFlying player ability when this effect is applied and send the change to clients
            ServerPlayer player = (ServerPlayer)entityLivingBaseIn;
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
        super.addAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
    }
    
    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        if (!entityLivingBaseIn.level.isClientSide && entityLivingBaseIn instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer)entityLivingBaseIn;
            GameType type = player.gameMode.getGameModeForPlayer();
            player.getAbilities().mayfly = (type == GameType.CREATIVE || type == GameType.SPECTATOR);   // Cancel flight ability if not appropriate for game mode
            if (!player.getAbilities().mayfly) {
                // If flying is no longer allowed, end the player's flight
                player.getAbilities().flying = false;
            }
            player.onUpdateAbilities();   // Send ability changes to clients
        }
        super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
    }
}
