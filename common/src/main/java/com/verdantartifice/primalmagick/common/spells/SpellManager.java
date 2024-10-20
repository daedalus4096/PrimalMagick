package com.verdantartifice.primalmagick.common.spells;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.entities.projectiles.SpellMineEntity;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellImpactPacket;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.spells.mods.BurstSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.ConfiguredSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.ISpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.MineSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModType;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModsPM;
import com.verdantartifice.primalmagick.common.spells.payloads.ConfiguredSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.ISpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadType;
import com.verdantartifice.primalmagick.common.spells.vehicles.ConfiguredSpellVehicle;
import com.verdantartifice.primalmagick.common.spells.vehicles.ISpellVehicle;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleType;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;
import com.verdantartifice.primalmagick.common.wands.IWand;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

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
    protected static final Map<String, Supplier<AbstractRequirement<?>>> VEHICLE_RESEARCH_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<AbstractRequirement<?>>> PAYLOAD_RESEARCH_SUPPLIERS = new HashMap<>();
    protected static final Map<String, Supplier<AbstractRequirement<?>>> MOD_RESEARCH_SUPPLIERS = new HashMap<>();
    
    protected static final DecimalFormat COOLDOWN_FORMATTER = new DecimalFormat("#######.##");

    @Nonnull
    protected static List<String> getFilteredTypes(@Nullable Player player, @Nonnull List<String> types, @Nonnull Map<String, Supplier<AbstractRequirement<?>>> suppliers) {
        // Compute a list of spell component types that the given player is able to use by dint of their accumulated research
        List<String> retVal = new ArrayList<>();
        for (String type : types) {
            Supplier<AbstractRequirement<?>> supplier = suppliers.get(type);
            if (supplier != null) {
                AbstractRequirement<?> req = supplier.get();
                if (req == null || req.isMetBy(player)) {
                    retVal.add(type);
                }
            }
        }
        return retVal;
    }
    
    protected static <T extends ISpellComponentType> List<T> getFilteredTypes(@Nullable Player player, Collection<T> values) {
        // Compute a list of spell component types that the given player is able to use
        return values.stream()
                .filter(type -> type.requirementSupplier().get() == null || type.requirementSupplier().get().isMetBy(player))
                .sorted(Comparator.comparing(T::sortOrder))
                .toList();
    }
    
    @Nonnull
    public static List<SpellVehicleType<?>> getVehicleTypes(@Nullable Player player) {
        return getFilteredTypes(player, Services.SPELL_VEHICLE_TYPES.getAll());
    }
    
    @Nullable
    public static Supplier<ISpellVehicle> getVehicleSupplier(@Nullable String type) {
        return VEHICLE_INSTANCE_SUPPLIERS.get(type);
    }
    
    public static void registerVehicleType(@Nullable String type, @Nullable Supplier<ISpellVehicle> instanceSupplier, @Nullable Supplier<AbstractRequirement<?>> researchSupplier) {
        // Register the given vehicle type and associate it with the given instance and research suppliers
        if (type != null && !type.isEmpty() && instanceSupplier != null && researchSupplier != null) {
            VEHICLE_TYPES.add(type);
            VEHICLE_INSTANCE_SUPPLIERS.put(type, instanceSupplier);
            VEHICLE_RESEARCH_SUPPLIERS.put(type, researchSupplier);
        }
    }
    
    @Nonnull
    public static List<SpellPayloadType<?>> getPayloadTypes(@Nullable Player player) {
        return getFilteredTypes(player, Services.SPELL_PAYLOAD_TYPES.getAll());
    }
    
    @Nullable
    public static Supplier<ISpellPayload> getPayloadSupplier(@Nullable String type) {
        return PAYLOAD_INSTANCE_SUPPLIERS.get(type);
    }
    
    public static void registerPayloadType(@Nullable String type, @Nullable Supplier<ISpellPayload> instanceSupplier, @Nullable Supplier<AbstractRequirement<?>> researchSupplier) {
        // Register the given payload type and associate it with the given instance and research suppliers
        if (type != null && !type.isEmpty() && instanceSupplier != null && researchSupplier != null) {
            PAYLOAD_TYPES.add(type);
            PAYLOAD_INSTANCE_SUPPLIERS.put(type, instanceSupplier);
            PAYLOAD_RESEARCH_SUPPLIERS.put(type, researchSupplier);
        }
    }
    
    @Nonnull
    public static List<SpellModType<?>> getModTypes(@Nullable Player player) {
        return getFilteredTypes(player, Services.SPELL_MOD_TYPES.getAll());
    }
    
    @Nullable
    public static Supplier<ISpellMod> getModSupplier(@Nullable String type) {
        return MOD_INSTANCE_SUPPLIERS.get(type);
    }
    
    public static void registerModType(@Nullable String type, @Nullable Supplier<ISpellMod> instanceSupplier, @Nullable Supplier<AbstractRequirement<?>> researchSupplier) {
        // Register the given mod type and associate it with the given instance and research suppliers
        if (type != null && !type.isEmpty() && instanceSupplier != null && researchSupplier != null) {
            MOD_TYPES.add(type);
            MOD_INSTANCE_SUPPLIERS.put(type, instanceSupplier);
            MOD_RESEARCH_SUPPLIERS.put(type, researchSupplier);
        }
    }
    
    public static boolean isOnCooldown(@Nullable Player player) {
        if (player == null) {
            return false;
        }
        
        // Determine whether the given player's spell cooldown is currently active, thus making spells unavailable
        IPlayerCooldowns cooldowns = PrimalMagickCapabilities.getCooldowns(player);
        if (cooldowns != null) {
            return cooldowns.isOnCooldown(IPlayerCooldowns.CooldownType.SPELL);
        } else {
            return false;
        }
    }
    
    public static void setCooldown(@Nullable Player player, int durationTicks) {
        if (player != null) {
            // Trigger a spell cooldown of the given duration for the given player and sync the data to their client
            IPlayerCooldowns cooldowns = PrimalMagickCapabilities.getCooldowns(player);
            if (cooldowns != null) {
                cooldowns.setCooldown(IPlayerCooldowns.CooldownType.SPELL, durationTicks);
                if (player instanceof ServerPlayer) {
                    cooldowns.sync((ServerPlayer)player); // Sync immediately, since cooldowns are time-sensitive
                }
            }
            
            // Show the shared cooldown visually
            player.getCooldowns().addCooldown(ItemsPM.SPELL_SCROLL_FILLED.get(), durationTicks);
            player.getCooldowns().addCooldown(ItemsPM.MUNDANE_WAND.get(), durationTicks);
            player.getCooldowns().addCooldown(ItemsPM.MODULAR_WAND.get(), durationTicks);
            player.getCooldowns().addCooldown(ItemsPM.MODULAR_STAFF.get(), durationTicks);
        }
    }
    
    public static void cycleActiveSpell(@Nullable Player player, @Nullable ItemStack wandStack, boolean reverse) {
        // Change the active spell for the given wand stack to the next (or previous, if specified) one in its inscribed list
        if (wandStack != null && wandStack.getItem() instanceof IWand wand) {
            int newIndex = wand.getActiveSpellIndex(wandStack) + (reverse ? -1 : 1);
            
            // Cycle to the beginning from the end of the list, or to the end from the beginning of the list
            if (newIndex >= wand.getSpellCount(wandStack)) {
                newIndex = -1;
            }
            if (newIndex < -1) {
                newIndex = wand.getSpellCount(wandStack) - 1;
            }
            
            setActiveSpell(player, wandStack, newIndex);
        }
    }
    
    public static void setActiveSpell(@Nullable Player player, @Nullable ItemStack wandStack, int spellIndex) {
        // Set the active spell for the given wand stack to the given index, clamped
        if (wandStack != null && wandStack.getItem() instanceof IWand wand) {
            // Clamp the given index to safe bounds
            int newIndex = Mth.clamp(spellIndex, -1, wand.getSpellCount(wandStack) - 1);
            
            // Set the new active spell index
            wand.setActiveSpellIndex(wandStack, newIndex);
            
            // Tell the player what the new active spell is
            if (player != null) {
                SpellPackage spell = wand.getActiveSpell(wandStack);
                if (spell == null) {
                    player.sendSystemMessage(Component.translatable("event.primalmagick.cycle_spell.none"));
                } else {
                    player.sendSystemMessage(Component.translatable("event.primalmagick.cycle_spell", spell.getDisplayName()));
                }
            }
        }
    }
    
    public static void executeSpellPayload(@Nonnull SpellPackage spell, @Nonnull HitResult result, @Nonnull Level world, @Nonnull LivingEntity caster, @Nullable ItemStack spellSource, 
            boolean allowMine, @Nullable Entity projectileEntity) {
        // Execute the payload of the given spell upon the block/entity in the given raytrace result
        if (world instanceof ServerLevel serverLevel && spell.payload() != null) {
            Vec3 hitVec = result.getLocation();
            ConfiguredSpellMod<BurstSpellMod> burstMod = spell.getMod(SpellModsPM.BURST.get()).orElse(null);
            ConfiguredSpellMod<MineSpellMod> mineMod = spell.getMod(SpellModsPM.MINE.get()).orElse(null);
            
            // Trigger spell impact FX on the clients of every player in range
            int radius = (burstMod == null || (allowMine && mineMod != null)) ? 1 : burstMod.getPropertyValue(SpellPropertiesPM.RADIUS.get());
            PacketHandler.sendToAllAround(
                    new SpellImpactPacket(hitVec.x, hitVec.y, hitVec.z, radius, spell.payload().getComponent().getSource().getColor()),
                    serverLevel,
                    BlockPos.containing(hitVec), 
                    64.0D);
            
            if (allowMine && mineMod != null) {
                // If the spell package has the Mine mod and mines are allowed (i.e. this payload wasn't triggered by an existing mine),
                // spawn a new mine
                SpellMineEntity mineEntity = new SpellMineEntity(world, hitVec, caster, spell, spellSource, mineMod.getComponent().getDurationMinutes(spell, spellSource));
                world.addFreshEntity(mineEntity);
            } else if (burstMod != null) {
                // If the spell package has the burst mod, calculate the set of affected blocks/entities and execute the payload on each
                Set<HitResult> targetSet = burstMod.getComponent().getBurstTargets(result, spell, spellSource, world);
                for (HitResult target : targetSet) {
                    spell.payload().getComponent().execute(target, hitVec, spell, world, caster, spellSource, null);
                }
            } else {
                // Otherwise, just execute the payload on the given target
                spell.payload().getComponent().execute(result, null, spell, world, caster, spellSource, null);
            }
        }
    }
    
    public static boolean canPolymorph(@Nonnull EntityType<?> entityType) {
        // TODO Change to deny, allow, deny pattern
        if (entityType.is(EntityTypeTagsPM.POLYMORPH_ALLOW)) {
            return true;
        } else if (entityType.is(EntityTypeTagsPM.POLYMORPH_BAN)) {
            return false;
        } else {
            // Don't allow misc entities like arrows and fishing bobbers unless explicitly allow-listed
            return !entityType.getCategory().equals(MobCategory.MISC);
        }
    }
    
    @Nonnull
    public static List<Component> getSpellPackageDetailTooltip(@Nullable SpellPackage spell, @Nonnull ItemStack spellSource, boolean indent, HolderLookup.Provider registries) {
        List<Component> retVal = new ArrayList<>();
        Component leader = indent ? Component.literal("    ") : Component.literal("");
        if (spell != null) {
            ConfiguredSpellVehicle<?> vehicle = spell.vehicle();
            if (vehicle != null) {
                retVal.add(leader.copy().append(Component.translatable("tooltip.primalmagick.spells.details.vehicle", vehicle.getComponent().getDetailTooltip(spell, spellSource, registries))));
            }
            
            ConfiguredSpellPayload<?> payload = spell.payload();
            if (payload != null) {
                retVal.add(leader.copy().append(Component.translatable("tooltip.primalmagick.spells.details.payload", payload.getComponent().getDetailTooltip(spell, spellSource, registries))));
            }
            
            Optional<ConfiguredSpellMod<?>> primary = spell.primaryMod();
            Optional<ConfiguredSpellMod<?>> secondary = spell.secondaryMod();
            if (primary.isPresent() && primary.get().getComponent().isActive() && secondary.isPresent() && secondary.get().getComponent().isActive()) {
                retVal.add(leader.copy().append(Component.translatable("tooltip.primalmagick.spells.details.mods.double", primary.get().getComponent().getDetailTooltip(spell, spellSource, registries),
                        secondary.get().getComponent().getDetailTooltip(spell, spellSource, registries))));
            } else if (primary.isPresent() && primary.get().getComponent().isActive()) {
                retVal.add(leader.copy().append(Component.translatable("tooltip.primalmagick.spells.details.mods.single", primary.get().getComponent().getDetailTooltip(spell, spellSource, registries))));
            } else if (secondary.isPresent() && secondary.get().getComponent().isActive()) {
                retVal.add(leader.copy().append(Component.translatable("tooltip.primalmagick.spells.details.mods.single", secondary.get().getComponent().getDetailTooltip(spell, spellSource, registries))));
            }
            
            retVal.add(leader.copy().append(Component.translatable("tooltip.primalmagick.spells.details.cooldown", COOLDOWN_FORMATTER.format(spell.getCooldownTicks() / 20.0D))));
            
            if (!spellSource.is(ItemsPM.SPELL_SCROLL_FILLED.get())) {
                retVal.add(leader.copy().append(Component.translatable("tooltip.primalmagick.spells.details.mana_cost", spell.getManaCost().getText())));
            }
        }
        return retVal;
    }
}
