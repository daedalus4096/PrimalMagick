package com.verdantartifice.primalmagic.common.spells;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

public class SpellManager {
    public static boolean isOnCooldown(PlayerEntity player) {
        IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
        if (cooldowns != null) {
            return cooldowns.isOnCooldown(IPlayerCooldowns.CooldownType.SPELL);
        } else {
            return false;
        }
    }
    
    public static void setCooldown(PlayerEntity player, int durationTicks) {
        IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
        if (cooldowns != null) {
            cooldowns.setCooldown(IPlayerCooldowns.CooldownType.SPELL, durationTicks);
            if (player instanceof ServerPlayerEntity) {
                cooldowns.sync((ServerPlayerEntity)player);
            }
        }
    }
}
