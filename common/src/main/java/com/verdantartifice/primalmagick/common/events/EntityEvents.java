package com.verdantartifice.primalmagick.common.events;

import com.verdantartifice.primalmagick.common.blocks.BlocksPM;
import com.verdantartifice.primalmagick.common.blocks.misc.EnderwardBlock;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.effects.EffectsPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentHelperPM;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.armor.WardingModuleItem;
import com.verdantartifice.primalmagick.common.research.ResearchEntries;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Handlers for miscellaneous entity events.
 * 
 * @author Daedalus4096
 */
public class EntityEvents {
    public static boolean onEnderTeleport(LivingEntity entity, Vec3 target) {
        // Prevent the teleport if the teleporter is afflicted with Enderlock
        if (entity.hasEffect(EffectsPM.ENDERLOCK.getHolder())) {
            return true;
        }
        
        // Check to see if an enderward blocks the teleport
        return checkEnderward(entity, target);
    }
    
    private static boolean checkEnderward(LivingEntity entity, Vec3 target) {
        double edgeLength = 2D * EnderwardBlock.EFFECT_RADIUS;
        AABB searchAABB = AABB.ofSize(target, edgeLength, edgeLength, edgeLength);
        if (BlockPos.betweenClosedStream(searchAABB).anyMatch(pos -> entity.level().getBlockState(pos).is(BlocksPM.ENDERWARD.get()))) {
            if (entity instanceof Player player) {
                player.displayClientMessage(Component.translatable("event.primalmagick.enderward.block").withStyle(ChatFormatting.RED), true);
            }
            return true;
        }
        return false;
    }
    
    public static void onEnderTeleportLowest(Player player, Vec3 target) {
        // Keep track of the distance teleported for stats
        StatsManager.incrementValue(player, StatsPM.DISTANCE_TELEPORTED_CM, (int)(100 * player.position().distanceTo(target)));
    }
    
    public static void onAnimalTameLowest(Player player, Animal animal) {
        // Grant appropriate research if a player tames a wolf
        if ( animal instanceof Wolf &&
             ResearchManager.isResearchComplete(player, ResearchEntries.FIRST_STEPS) && 
             !ResearchManager.isResearchComplete(player, ResearchEntries.FURRY_FRIEND) ) {
            ResearchManager.completeResearch(player, ResearchEntries.FURRY_FRIEND);
        }
    }
    
    public static void onBabyEntitySpawnLowest(Player player) {
        // Grant appropriate research if a player breeds an animal
        if ( ResearchManager.isResearchComplete(player, ResearchEntries.FIRST_STEPS) &&
             !ResearchManager.isResearchComplete(player, ResearchEntries.BREED_ANIMAL) ) {
            ResearchManager.completeResearch(player, ResearchEntries.BREED_ANIMAL);
        }
    }
    
    public static void onLivingEntityUseItemTick(LivingEntity entity, ItemStack stack, int currentDuration) {
        // Stack up resistance on the wielders of shields with the Bulwark enchantment
        int maxDuration = stack.getUseDuration(entity);
        int delta = maxDuration - currentDuration;
        int enchantLevel = EnchantmentHelperPM.getEnchantmentLevel(stack, EnchantmentsPM.BULWARK, entity.registryAccess());
        if (stack.getItem() instanceof ShieldItem && delta > 0 && delta % 5 == 0 && enchantLevel > 0) {
            MobEffectInstance effectInstance = entity.getEffect(MobEffects.DAMAGE_RESISTANCE);
            int amplifier = (effectInstance == null) ? 0 : Mth.clamp(1 + effectInstance.getAmplifier(), 0, enchantLevel - 1);
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10, amplifier));
        }
    }
    
    public static void onLivingEquipmentChange(LivingEntity livingEntity, ItemStack fromStack, ItemStack toStack) {
        // If a player is donning or removing a warded piece of armor, update their max ward level
        if (livingEntity instanceof ServerPlayer serverPlayer &&
                (WardingModuleItem.hasWardAttached(fromStack) || WardingModuleItem.hasWardAttached(toStack) ) ) {
            PrimalMagickCapabilities.getWard(serverPlayer).ifPresent(playerWard -> {
                int newMax = playerWard.getApplicableSlots().stream().map(slot -> serverPlayer.getItemBySlot(slot)).filter(WardingModuleItem::hasWardAttached)
                        .mapToInt(stack -> 1 + WardingModuleItem.getAttachedWardLevel(stack)).sum();
                playerWard.setMaxWard(newMax);
                playerWard.sync(serverPlayer);
            });
        }
    }
}
