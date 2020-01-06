package com.verdantartifice.primalmagic.common.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.world.GameType;

public class FlyingEffect extends Effect {
    public FlyingEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @Override
    public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        if (!entityLivingBaseIn.world.isRemote && entityLivingBaseIn instanceof ServerPlayerEntity) {
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
            player.abilities.allowFlying = (type == GameType.CREATIVE || type == GameType.SPECTATOR);
            if (!player.abilities.allowFlying) {
                player.abilities.isFlying = false;
            }
            player.sendPlayerAbilities();
        }
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    }
}
