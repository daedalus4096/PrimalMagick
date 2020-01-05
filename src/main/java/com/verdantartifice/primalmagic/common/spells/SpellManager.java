package com.verdantartifice.primalmagic.common.spells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.entities.projectiles.SpellMineEntity;
import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.fx.SpellImpactPacket;
import com.verdantartifice.primalmagic.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagic.common.spells.mods.BurstSpellMod;
import com.verdantartifice.primalmagic.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagic.common.spells.mods.MineSpellMod;
import com.verdantartifice.primalmagic.common.spells.payloads.ISpellPayload;
import com.verdantartifice.primalmagic.common.spells.vehicles.ISpellVehicle;
import com.verdantartifice.primalmagic.common.wands.IWand;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class SpellManager {
    protected static final List<String> VEHICLE_TYPES = new ArrayList<>();
    protected static final List<String> PAYLOAD_TYPES = new ArrayList<>();
    protected static final List<String> MOD_TYPES = new ArrayList<>();
    
    protected static final Map<String, Supplier<ISpellVehicle>> VEHICLE_INSTANCE_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<ISpellPayload>> PAYLOAD_INSTANCE_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<ISpellMod>> MOD_INSTANCE_SUPPLIERS = new HashMap<>();
    
    protected static final Map<String, Supplier<CompoundResearchKey>> VEHICLE_RESEARCH_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<CompoundResearchKey>> PAYLOAD_RESEARCH_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<CompoundResearchKey>> MOD_RESEARCH_SUPPLIERS = new HashMap<>();
    
    @Nonnull
    protected static List<String> getFilteredTypes(PlayerEntity player, List<String> types, Map<String, Supplier<CompoundResearchKey>> suppliers) {
        List<String> retVal = new ArrayList<>();
        for (String type : types) {
            Supplier<CompoundResearchKey> supplier = suppliers.get(type);
            if (supplier != null) {
                CompoundResearchKey key = supplier.get();
                if (key == null || key.isKnownByStrict(player)) {
                    retVal.add(type);
                }
            }
        }
        return retVal;
    }
    
    @Nonnull
    public static List<String> getVehicleTypes(PlayerEntity player) {
        return getFilteredTypes(player, VEHICLE_TYPES, VEHICLE_RESEARCH_SUPPLIERS);
    }
    
    @Nullable
    public static Supplier<ISpellVehicle> getVehicleSupplier(String type) {
        return VEHICLE_INSTANCE_SUPPLIERS.get(type);
    }
    
    public static void registerVehicleType(String type, Supplier<ISpellVehicle> instanceSupplier, Supplier<CompoundResearchKey> researchSupplier) {
        if (type != null && !type.isEmpty() && instanceSupplier != null && researchSupplier != null) {
            VEHICLE_TYPES.add(type);
            VEHICLE_INSTANCE_SUPPLIERS.put(type, instanceSupplier);
            VEHICLE_RESEARCH_SUPPLIERS.put(type, researchSupplier);
        }
    }
    
    @Nonnull
    public static List<String> getPayloadTypes(PlayerEntity player) {
        return getFilteredTypes(player, PAYLOAD_TYPES, PAYLOAD_RESEARCH_SUPPLIERS);
    }
    
    @Nullable
    public static Supplier<ISpellPayload> getPayloadSupplier(String type) {
        return PAYLOAD_INSTANCE_SUPPLIERS.get(type);
    }
    
    public static void registerPayloadType(String type, Supplier<ISpellPayload> instanceSupplier, Supplier<CompoundResearchKey> researchSupplier) {
        if (type != null && !type.isEmpty() && instanceSupplier != null && researchSupplier != null) {
            PAYLOAD_TYPES.add(type);
            PAYLOAD_INSTANCE_SUPPLIERS.put(type, instanceSupplier);
            PAYLOAD_RESEARCH_SUPPLIERS.put(type, researchSupplier);
        }
    }
    
    @Nonnull
    public static List<String> getModTypes(PlayerEntity player) {
        return getFilteredTypes(player, MOD_TYPES, MOD_RESEARCH_SUPPLIERS);
    }
    
    @Nullable
    public static Supplier<ISpellMod> getModSupplier(String type) {
        return MOD_INSTANCE_SUPPLIERS.get(type);
    }
    
    public static void registerModType(String type, Supplier<ISpellMod> instanceSupplier, Supplier<CompoundResearchKey> researchSupplier) {
        if (type != null && !type.isEmpty() && instanceSupplier != null && researchSupplier != null) {
            MOD_TYPES.add(type);
            MOD_INSTANCE_SUPPLIERS.put(type, instanceSupplier);
            MOD_RESEARCH_SUPPLIERS.put(type, researchSupplier);
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
    
    public static void executeSpellPayload(@Nonnull SpellPackage spell, @Nonnull RayTraceResult result, @Nonnull World world, @Nonnull PlayerEntity caster, boolean allowMine) {
        if (!world.isRemote && spell.getPayload() != null) {
            Vec3d hitVec = result.getHitVec();
            BurstSpellMod burstMod = spell.getMod(BurstSpellMod.class, "radius");
            MineSpellMod mineMod = spell.getMod(MineSpellMod.class, "duration");
            int radius = (burstMod == null || (allowMine && mineMod != null)) ? 1 : burstMod.getPropertyValue("radius");
            PacketHandler.sendToAllAround(
                    new SpellImpactPacket(hitVec.x, hitVec.y, hitVec.z, radius, spell.getPayload().getSource().getColor()), 
                    world.getDimension().getType(), 
                    new BlockPos(hitVec), 
                    64.0D);
            
            if (allowMine && mineMod != null) {
                SpellMineEntity mineEntity = new SpellMineEntity(world, hitVec, caster, spell, mineMod.getModdedPropertyValue("duration", spell));
                world.addEntity(mineEntity);
            } else if (burstMod != null) {
                Set<RayTraceResult> targetSet = burstMod.getBurstTargets(result, spell, world);
                for (RayTraceResult target : targetSet) {
                    spell.getPayload().execute(target, hitVec, spell, world, caster);
                }
            } else {
                spell.getPayload().execute(result, null, spell, world, caster);
            }
        }
    }
}
