package com.verdantartifice.primalmagic.common.spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.ISpellVehicle;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

public class SpellManager {
    protected static final List<String> VEHICLE_TYPES = new ArrayList<>();
    protected static final List<String> PAYLOAD_TYPES = new ArrayList<>();
    protected static final List<String> MOD_TYPES = new ArrayList<>();
    
    protected static final Map<String, Supplier<ISpellVehicle>> VEHICLE_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<ISpellPayload>> PAYLOAD_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<ISpellMod>> MOD_SUPPLIERS = new HashMap<>();
    
    @Nonnull
    public static List<String> getVehicleTypes() {
        return Collections.unmodifiableList(VEHICLE_TYPES);
    }
    
    @Nullable
    public static Supplier<ISpellVehicle> getVehicleSupplier(String type) {
        return VEHICLE_SUPPLIERS.get(type);
    }
    
    public static void registerVehicleType(String type, Supplier<ISpellVehicle> supplier) {
        if (type != null && !type.isEmpty() && supplier != null) {
            VEHICLE_TYPES.add(type);
            VEHICLE_SUPPLIERS.put(type, supplier);
        }
    }
    
    @Nonnull
    public static List<String> getPayloadTypes() {
        return Collections.unmodifiableList(PAYLOAD_TYPES);
    }
    
    @Nullable
    public static Supplier<ISpellPayload> getPayloadSupplier(String type) {
        return PAYLOAD_SUPPLIERS.get(type);
    }
    
    public static void registerPayloadType(String type, Supplier<ISpellPayload> supplier) {
        if (type != null && !type.isEmpty() && supplier != null) {
            PAYLOAD_TYPES.add(type);
            PAYLOAD_SUPPLIERS.put(type, supplier);
        }
    }
    
    @Nonnull
    public static List<String> getModTypes() {
        return Collections.unmodifiableList(MOD_TYPES);
    }
    
    @Nullable
    public static Supplier<ISpellMod> getModSupplier(String type) {
        return MOD_SUPPLIERS.get(type);
    }
    
    public static void registerModType(String type, Supplier<ISpellMod> supplier) {
        if (type != null && !type.isEmpty()) {
            MOD_TYPES.add(type);
            MOD_SUPPLIERS.put(type, supplier);
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
                SpellPackage spell = wand.getActiveSpell(wandStack);
                if (spell == null) {
                    player.sendMessage(new TranslationTextComponent("event.primalmagic.cycle_spell.none"));
                } else {
                    player.sendMessage(new TranslationTextComponent("event.primalmagic.cycle_spell", spell.getName()));
                }
            }
        }
    }
}
