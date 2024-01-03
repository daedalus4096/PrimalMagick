package com.verdantartifice.primalmagick.common.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

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
    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration <= 1;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        // End flying effect on the last tick, because there's no equivalent to onEffectStarted for effects ending
        Level level = pLivingEntity.level();
        if (!level.isClientSide && pLivingEntity instanceof ServerPlayer player) {
            GameType type = player.gameMode.getGameModeForPlayer();
            player.getAbilities().mayfly = (type == GameType.CREATIVE || type == GameType.SPECTATOR);   // Cancel flight ability if not appropriate for game mode
            if (!player.getAbilities().mayfly) {
                // If flying is no longer allowed, end the player's flight
                player.getAbilities().flying = false;
            }
            player.onUpdateAbilities();   // Send ability changes to clients
        }
    }

    @Override
    public void onEffectStarted(LivingEntity pLivingEntity, int pAmplifier) {
        Level level = pLivingEntity.level();
        if (!level.isClientSide && pLivingEntity instanceof ServerPlayer player) {
            // Set the mayFly player ability when this effect is applied and send the change to clients
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
        super.onEffectStarted(pLivingEntity, pAmplifier);
    }
}
