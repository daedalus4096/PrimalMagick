package com.verdantartifice.primalmagick.common.spells;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerCooldowns;
import com.verdantartifice.primalmagick.common.entities.projectiles.SpellMineEntity;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.items.wands.AbstractWandItem;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.fx.SpellImpactPacket;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.spells.mods.BurstSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.ConfiguredSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.MineSpellMod;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModType;
import com.verdantartifice.primalmagick.common.spells.mods.SpellModsPM;
import com.verdantartifice.primalmagick.common.spells.payloads.ConfiguredSpellPayload;
import com.verdantartifice.primalmagick.common.spells.payloads.SpellPayloadType;
import com.verdantartifice.primalmagick.common.spells.vehicles.ConfiguredSpellVehicle;
import com.verdantartifice.primalmagick.common.spells.vehicles.SpellVehicleType;
import com.verdantartifice.primalmagick.common.tags.EntityTypeTagsPM;
import com.verdantartifice.primalmagick.common.wands.ISpellContainer;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Primary access point for spell-related methods.  Also stores defined spell component data in static registries.
 * 
 * @author Daedalus4096
 */
public class SpellManager {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected static final DecimalFormat COOLDOWN_FORMATTER = new DecimalFormat("#######.##");

    protected static <T extends ISpellComponentType> List<T> getFilteredTypes(@Nullable Player player, Collection<T> values) {
        // Compute a list of spell component types that the given player is able to use
        return values.stream()
                .filter(type -> type.requirementSupplier().get() == null || type.requirementSupplier().get().isMetBy(player))
                .sorted(Comparator.comparing(T::sortOrder))
                .toList();
    }
    
    @NotNull
    public static List<SpellVehicleType<?>> getVehicleTypes(@Nullable Player player) {
        return getFilteredTypes(player, Services.SPELL_VEHICLE_TYPES_REGISTRY.getAll());
    }
    
    @NotNull
    public static List<SpellPayloadType<?>> getPayloadTypes(@Nullable Player player) {
        return getFilteredTypes(player, Services.SPELL_PAYLOAD_TYPES_REGISTRY.getAll());
    }
    
    @NotNull
    public static List<SpellModType<?>> getModTypes(@Nullable Player player) {
        return getFilteredTypes(player, Services.SPELL_MOD_TYPES_REGISTRY.getAll());
    }
    
    public static boolean isOnCooldown(@Nullable Player player) {
        // Determine whether the given player's spell cooldown is currently active, thus making spells unavailable
        return Services.CAPABILITIES.cooldowns(player).map(c -> c.isOnCooldown(IPlayerCooldowns.CooldownType.SPELL)).orElse(false);
    }
    
    public static void setCooldown(@Nullable Player player, int durationTicks) {
        if (player != null) {
            // Trigger a spell cooldown of the given duration for the given player and sync the data to their client
            Services.CAPABILITIES.cooldowns(player).ifPresent(c -> {
                c.setCooldown(IPlayerCooldowns.CooldownType.SPELL, durationTicks);
                if (player instanceof ServerPlayer serverPlayer) {
                    c.sync(serverPlayer); // Sync immediately, since cooldowns are time-sensitive
                }
            });

            // Show the shared cooldown visually
            player.getCooldowns().addCooldown(ItemsPM.SPELL_SCROLL_FILLED.get(), durationTicks);
            player.getCooldowns().addCooldown(ItemsPM.MUNDANE_WAND.get(), durationTicks);
            player.getCooldowns().addCooldown(ItemsPM.MODULAR_WAND.get(), durationTicks);
            player.getCooldowns().addCooldown(ItemsPM.MODULAR_STAFF.get(), durationTicks);
        }
    }

    public static void appendSpellListingText(@Nullable Player pPlayer, ItemStack pStack, Item.TooltipContext pContext, List<Component> pTooltipComponents) {
        if (pStack.getItem() instanceof ISpellContainer spellContainer) {
            List<SpellPackage> spells = spellContainer.getSpells(pStack);
            int activeIndex = spellContainer.getActiveSpellIndex(pStack);
            pTooltipComponents.add(Component.translatable("tooltip.primalmagick.spells.wand_header", spellContainer.getSpellCapacityText(pStack)));
            if (spells.isEmpty()) {
                pTooltipComponents.add(Component.translatable("tooltip.primalmagick.spells.none"));
            } else {
                for (int index = 0; index < spells.size(); index++) {
                    SpellPackage spell = spells.get(index);
                    if (index == activeIndex) {
                        pTooltipComponents.add(Component.translatable("tooltip.primalmagick.spells.name_selected", spell.getDisplayName()));
                        pTooltipComponents.addAll(SpellManager.getSpellPackageDetailTooltip(spell, pStack, pPlayer, true, pContext.registries()));
                    } else {
                        pTooltipComponents.add(Component.translatable("tooltip.primalmagick.spells.name_unselected", spell.getDisplayName()));
                    }
                }
            }
        }
    }

    public static void appendActiveSpellText(ItemStack pStack, List<Component> pTooltipComponents) {
        if (pStack.getItem() instanceof ISpellContainer spellContainer) {
            SpellPackage activeSpell = spellContainer.getActiveSpell(pStack);
            Component activeSpellName = (activeSpell == null) ?
                    Component.translatable("tooltip.primalmagick.none") :
                    activeSpell.getDisplayName();
            pTooltipComponents.add(Component.translatable("tooltip.primalmagick.spells.short_wand_header", activeSpellName));
        }
    }

    public static @NotNull List<SpellPackage> getSpells(@NotNull ItemStack mainHandStack, @NotNull ItemStack offHandStack) {
        ImmutableList.Builder<SpellPackage> builder = ImmutableList.builder();
        if (mainHandStack.getItem() instanceof ISpellContainer mainHandContainer) {
            builder.addAll(mainHandContainer.getSpells(mainHandStack));
        }
        if (offHandStack.getItem() instanceof ISpellContainer offHandContainer) {
            builder.addAll(offHandContainer.getSpells(offHandStack));
        }
        return builder.build();
    }

    public static int getSpellCount(@NotNull ItemStack mainHandStack, @NotNull ItemStack offHandStack) {
        return getSpells(mainHandStack, offHandStack).size();
    }

    public static int getActiveSpellIndex(@NotNull ItemStack mainHandStack, @NotNull ItemStack offHandStack) {
        if (mainHandStack.getItem() instanceof ISpellContainer mainHandContainer) {
            int mainHandSelection = mainHandContainer.getActiveSpellIndex(mainHandStack);
            if (mainHandSelection != ISpellContainer.OTHER_HAND_SELECTED) {
                return mainHandSelection;
            }
        }
        if (offHandStack.getItem() instanceof ISpellContainer offHandContainer) {
            int offHandSelection = offHandContainer.getActiveSpellIndex(offHandStack);
            if (offHandSelection != ISpellContainer.OTHER_HAND_SELECTED) {
                int padding = (mainHandStack.getItem() instanceof ISpellContainer mainHandContainer) ? mainHandContainer.getSpellCount(mainHandStack) : 0;
                return offHandSelection + padding;
            }
        }
        return ISpellContainer.NO_SPELL_SELECTED;
    }

    public static @Nullable SpellPackage getActiveSpell(@NotNull ItemStack mainHandStack, @NotNull ItemStack offHandStack) {
        if (mainHandStack.getItem() instanceof ISpellContainer mainHandContainer) {
            SpellPackage activeSpell = mainHandContainer.getActiveSpell(mainHandStack);
            if (activeSpell != null) {
                return activeSpell;
            }
        }
        if (offHandStack.getItem() instanceof ISpellContainer offHandContainer) {
            return offHandContainer.getActiveSpell(offHandStack);
        }
        return null;
    }

    public static boolean setActiveSpellIndex(@Nullable Player player, @NotNull ItemStack mainHandStack, @NotNull ItemStack offHandStack, int index) {
        boolean retVal = false;
        if (mainHandStack.getItem() instanceof ISpellContainer mainHandContainer && offHandStack.getItem() instanceof ISpellContainer offHandContainer) {
            int mainHandCount = mainHandContainer.getSpellCount(mainHandStack);
            if (index >= 0 && index < mainHandCount) {
                mainHandContainer.setActiveSpellIndex(mainHandStack, index);
                offHandContainer.setActiveSpellIndex(offHandStack, ISpellContainer.OTHER_HAND_SELECTED);
                retVal = true;
            } else if (index >= mainHandCount && index < mainHandCount + offHandContainer.getSpellCount(offHandStack)) {
                mainHandContainer.setActiveSpellIndex(mainHandStack, ISpellContainer.OTHER_HAND_SELECTED);
                offHandContainer.setActiveSpellIndex(offHandStack, index - mainHandCount);
                retVal = true;
            }
        } else if (mainHandStack.getItem() instanceof ISpellContainer mainHandContainer && index >= 0 && index < mainHandContainer.getSpellCount(mainHandStack)) {
            mainHandContainer.setActiveSpellIndex(mainHandStack, index);
            retVal = true;
        } else if (offHandStack.getItem() instanceof ISpellContainer offHandContainer && index >= 0 && index < offHandContainer.getSpellCount(offHandStack)) {
            offHandContainer.setActiveSpellIndex(offHandStack, index);
            retVal = true;
        }

        if (retVal && player != null) {
            SpellPackage spell = getActiveSpell(mainHandStack, offHandStack);
            if (spell == null) {
                player.sendSystemMessage(Component.translatable("event.primalmagick.cycle_spell.none"));
            } else {
                player.sendSystemMessage(Component.translatable("event.primalmagick.cycle_spell", spell.getDisplayName()));
            }
        } else if (!retVal) {
            LOGGER.warn("Failed to set active spell to invalid index {}", index);
        }

        return retVal;
    }
    
    public static void executeSpellPayload(@NotNull SpellPackage spell, @NotNull HitResult result, @NotNull Level world, @NotNull LivingEntity caster, @Nullable ItemStack spellSource,
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
    
    public static boolean canPolymorph(@NotNull EntityType<?> entityType) {
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
    
    @NotNull
    public static List<Component> getSpellPackageDetailTooltip(@Nullable SpellPackage spell, @NotNull ItemStack spellSource, @Nullable Player player, boolean indent, HolderLookup.Provider registries) {
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
            
            if (spellSource.getItem() instanceof AbstractWandItem wandItem && player != null && payload != null) {
                // Scale the spell's centimana cost down to whole mana points for display
                Source source = payload.getComponent().getSource();
                SourceList baseCost = spell.getManaCost();
                SourceList modifiedCost = wandItem.getModifiedCost(spellSource, player, baseCost, registries);
                retVal.add(leader.copy().append(Component.translatable("tooltip.primalmagick.spells.details.mana_cost", modifiedCost.getScaledText(0.01D))));
            }
        }
        return retVal;
    }
}
