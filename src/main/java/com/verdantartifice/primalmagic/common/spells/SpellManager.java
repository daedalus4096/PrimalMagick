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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * Primary access point for spell-related methods.  Also stores defined spell component data in static registries.
 * 
 * @author Daedalus4096
 */
public class SpellManager {
    // Lists of spell component type names
    protected static final List<String> VEHICLE_TYPES = new ArrayList<>();
    protected static final List<String> PAYLOAD_TYPES = new ArrayList<>();
    protected static final List<String> MOD_TYPES = new ArrayList<>();
    
    // Maps of spell component type names to suppliers for default instances of those components
    protected static final Map<String, Supplier<ISpellVehicle>> VEHICLE_INSTANCE_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<ISpellPayload>> PAYLOAD_INSTANCE_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<ISpellMod>> MOD_INSTANCE_SUPPLIERS = new HashMap<>();
    
    // Maps of spell component type names to suppliers for keys of required research for those components
    protected static final Map<String, Supplier<CompoundResearchKey>> VEHICLE_RESEARCH_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<CompoundResearchKey>> PAYLOAD_RESEARCH_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<CompoundResearchKey>> MOD_RESEARCH_SUPPLIERS = new HashMap<>();
    
    @Nonnull
    protected static List<String> getFilteredTypes(@Nullable PlayerEntity player, @Nonnull List<String> types, @Nonnull Map<String, Supplier<CompoundResearchKey>> suppliers) {
        // Compute a list of spell component types that the given player is able to use by dint of their accumulated research
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
    public static List<String> getVehicleTypes(@Nullable PlayerEntity player) {
        // Compute a list of spell vehicle types that the given player is able to use
        return getFilteredTypes(player, VEHICLE_TYPES, VEHICLE_RESEARCH_SUPPLIERS);
    }
    
    @Nullable
    public static Supplier<ISpellVehicle> getVehicleSupplier(@Nullable String type) {
        return VEHICLE_INSTANCE_SUPPLIERS.get(type);
    }
    
    public static void registerVehicleType(@Nullable String type, @Nullable Supplier<ISpellVehicle> instanceSupplier, @Nullable Supplier<CompoundResearchKey> researchSupplier) {
        // Register the given vehicle type and associate it with the given instance and research suppliers
        if (type != null && !type.isEmpty() && instanceSupplier != null && researchSupplier != null) {
            VEHICLE_TYPES.add(type);
            VEHICLE_INSTANCE_SUPPLIERS.put(type, instanceSupplier);
            VEHICLE_RESEARCH_SUPPLIERS.put(type, researchSupplier);
        }
    }
    
    @Nonnull
    public static List<String> getPayloadTypes(@Nullable PlayerEntity player) {
        // Compute a list of spell payload types that the given player is able to use
        return getFilteredTypes(player, PAYLOAD_TYPES, PAYLOAD_RESEARCH_SUPPLIERS);
    }
    
    @Nullable
    public static Supplier<ISpellPayload> getPayloadSupplier(@Nullable String type) {
        return PAYLOAD_INSTANCE_SUPPLIERS.get(type);
    }
    
    public static void registerPayloadType(@Nullable String type, @Nullable Supplier<ISpellPayload> instanceSupplier, @Nullable Supplier<CompoundResearchKey> researchSupplier) {
        // Register the given payload type and associate it with the given instance and research suppliers
        if (type != null && !type.isEmpty() && instanceSupplier != null && researchSupplier != null) {
            PAYLOAD_TYPES.add(type);
            PAYLOAD_INSTANCE_SUPPLIERS.put(type, instanceSupplier);
            PAYLOAD_RESEARCH_SUPPLIERS.put(type, researchSupplier);
        }
    }
    
    @Nonnull
    public static List<String> getModTypes(@Nullable PlayerEntity player) {
        // Compute a list of spell mod types that the given player is able to use
        return getFilteredTypes(player, MOD_TYPES, MOD_RESEARCH_SUPPLIERS);
    }
    
    @Nullable
    public static Supplier<ISpellMod> getModSupplier(@Nullable String type) {
        return MOD_INSTANCE_SUPPLIERS.get(type);
    }
    
    public static void registerModType(@Nullable String type, @Nullable Supplier<ISpellMod> instanceSupplier, @Nullable Supplier<CompoundResearchKey> researchSupplier) {
        // Register the given mod type and associate it with the given instance and research suppliers
        if (type != null && !type.isEmpty() && instanceSupplier != null && researchSupplier != null) {
            MOD_TYPES.add(type);
            MOD_INSTANCE_SUPPLIERS.put(type, instanceSupplier);
            MOD_RESEARCH_SUPPLIERS.put(type, researchSupplier);
        }
    }
    
    public static boolean isOnCooldown(@Nullable PlayerEntity player) {
        if (player == null) {
            return false;
        }
        
        // Determine whether the given player's spell cooldown is currently active, thus making spells unavailable
        IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
        if (cooldowns != null) {
            return cooldowns.isOnCooldown(IPlayerCooldowns.CooldownType.SPELL);
        } else {
            return false;
        }
    }
    
    public static void setCooldown(@Nullable PlayerEntity player, int durationTicks) {
        if (player != null) {
            // Trigger a spell cooldown of the given duration for the given player and sync the data to their client
            IPlayerCooldowns cooldowns = PrimalMagicCapabilities.getCooldowns(player);
            if (cooldowns != null) {
                cooldowns.setCooldown(IPlayerCooldowns.CooldownType.SPELL, durationTicks);
                if (player instanceof ServerPlayerEntity) {
                    cooldowns.sync((ServerPlayerEntity)player); // Sync immediately, since cooldowns are time-sensitive
                }
            }
        }
    }
    
    public static void cycleActiveSpell(@Nullable PlayerEntity player, @Nullable ItemStack wandStack, boolean reverse) {
        // Change the active spell for the given wand stack to the next (or previous, if specified) one in its inscribed list
        if (wandStack != null && wandStack.getItem() instanceof IWand) {
            IWand wand = (IWand)wandStack.getItem();
            int newIndex = wand.getActiveSpellIndex(wandStack) + (reverse ? -1 : 1);
            
            // Cycle to the beginning from the end of the list, or to the end from the beginning of the list
            if (newIndex >= wand.getSpellCount(wandStack)) {
                newIndex = -1;
            }
            if (newIndex < -1) {
                newIndex = wand.getSpellCount(wandStack) - 1;
            }
            
            // Set the new active spell index
            wand.setActiveSpellIndex(wandStack, newIndex);
            
            // Tell the player what the new active spell is
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
    
    public static void executeSpellPayload(@Nonnull SpellPackage spell, @Nonnull RayTraceResult result, @Nonnull World world, @Nonnull PlayerEntity caster, @Nonnull ItemStack spellSource, boolean allowMine) {
        // Execute the payload of the given spell upon the block/entity in the given raytrace result
        if (!world.isRemote && spell.getPayload() != null) {
            Vector3d hitVec = result.getHitVec();
            BurstSpellMod burstMod = spell.getMod(BurstSpellMod.class, "radius");
            MineSpellMod mineMod = spell.getMod(MineSpellMod.class, "duration");
            
            // Trigger spell impact FX on the clients of every player in range
            int radius = (burstMod == null || (allowMine && mineMod != null)) ? 1 : burstMod.getPropertyValue("radius");
            PacketHandler.sendToAllAround(
                    new SpellImpactPacket(hitVec.x, hitVec.y, hitVec.z, radius, spell.getPayload().getSource().getColor()), 
                    world.getDimensionKey(), 
                    new BlockPos(hitVec), 
                    64.0D);
            
            if (allowMine && mineMod != null) {
                // If the spell package has the Mine mod and mines are allowed (i.e. this payload wasn't triggered by an existing mine),
                // spawn a new mine
                SpellMineEntity mineEntity = new SpellMineEntity(world, hitVec, caster, spell, spellSource, mineMod.getModdedPropertyValue("duration", spell, spellSource));
                world.addEntity(mineEntity);
            } else if (burstMod != null) {
                // If the spell package has the burst mod, calculate the set of affected blocks/entities and execute the payload on each
                Set<RayTraceResult> targetSet = burstMod.getBurstTargets(result, spell, spellSource, world);
                for (RayTraceResult target : targetSet) {
                    spell.getPayload().execute(target, hitVec, spell, world, caster, spellSource);
                }
            } else {
                // Otherwise, just execute the payload on the given target
                spell.getPayload().execute(result, null, spell, world, caster, spellSource);
            }
        }
    }
}
