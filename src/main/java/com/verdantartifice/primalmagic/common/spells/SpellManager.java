package com.verdantartifice.primalmagic.common.spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.spells.packages.ISpellPackage;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class SpellManager {
    protected static final List<String> PACKAGE_TYPES = new ArrayList<>();
    protected static final List<String> PAYLOAD_TYPES = new ArrayList<>();
    
    @Nonnull
    public static List<String> getPackageTypes() {
        return Collections.unmodifiableList(PACKAGE_TYPES);
    }
    
    public static void registerPackageType(String type) {
        if (type != null && !type.isEmpty()) {
            PACKAGE_TYPES.add(type);
        }
    }
    
    @Nonnull
    public static List<String> getPayloadTypes() {
        return Collections.unmodifiableList(PAYLOAD_TYPES);
    }
    
    public static void registerPayloadType(String type) {
        if (type != null && !type.isEmpty()) {
            PAYLOAD_TYPES.add(type);
        }
    }
    
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
    
    public static void cycleActiveSpell(@Nullable PlayerEntity player, @Nullable ItemStack wandStack) {
        if (wandStack != null && wandStack.getItem() instanceof IWand) {
            IWand wand = (IWand)wandStack.getItem();
            int newIndex = wand.getActiveSpellIndex(wandStack) + 1;
            if (newIndex >= wand.getSpellCount(wandStack)) {
                newIndex = -1;
            }
            wand.setActiveSpellIndex(wandStack, newIndex);
            
            if (player != null) {
                ISpellPackage spell = wand.getActiveSpell(wandStack);
                if (spell == null) {
                    player.sendMessage(new TranslationTextComponent("event.primalmagic.cycle_spell.none"));
                } else {
                    player.sendMessage(new TranslationTextComponent("event.primalmagic.cycle_spell", spell.getName()));
                }
            }
        }
    }
}
