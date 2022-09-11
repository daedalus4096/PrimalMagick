package com.verdantartifice.primalmagick.common.entities.ai.sensing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.verdantartifice.primalmagick.common.entities.ai.memory.MemoryModuleTypesPM;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkAi;
import com.verdantartifice.primalmagick.common.entities.treefolk.TreefolkEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.player.Player;

/**
 * AI sensor specific to custom treefolk needs.
 * 
 * @author Daedalus4096
 */
public class TreefolkSpecificSensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleTypesPM.NEAREST_VISIBLE_ADULT_TREEFOLK.get(), MemoryModuleTypesPM.NEARBY_ADULT_TREEFOLK.get(), MemoryModuleTypesPM.NEARBY_TREEFOLK.get());
    }

    @Override
    protected void doTick(ServerLevel pLevel, LivingEntity pEntity) {
        Brain<?> brain = pEntity.getBrain();
        Optional<Player> playerHoldingWantedItemOpt = Optional.empty();
        List<TreefolkEntity> nearbyTreefolkList = new ArrayList<>();
        List<TreefolkEntity> nearbyAdultTreefolkList = new ArrayList<>();
        List<TreefolkEntity> nearestVisibleAdultTreefolkList = new ArrayList<>();
        
        NearestVisibleLivingEntities visibleEntities = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());

        for (LivingEntity living : visibleEntities.findAll(e -> true)) {
            if (living instanceof TreefolkEntity treefolk) {
                nearbyTreefolkList.add(treefolk);
                if (treefolk.isAdult()) {
                    nearestVisibleAdultTreefolkList.add(treefolk);
                }
            } else if (living instanceof Player player) {
                if (playerHoldingWantedItemOpt.isEmpty() && !player.isSpectator() && TreefolkAi.isPlayerHoldingLovedItem(player)) {
                    playerHoldingWantedItemOpt = Optional.of(player);
                }
            }
        }
        for (LivingEntity living : brain.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of())) {
            if (living instanceof TreefolkEntity treefolk && treefolk.isAdult()) {
                nearbyAdultTreefolkList.add(treefolk);
            }
        }
        
        brain.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, playerHoldingWantedItemOpt);
        brain.setMemory(MemoryModuleTypesPM.NEARBY_TREEFOLK.get(), nearbyTreefolkList);
        brain.setMemory(MemoryModuleTypesPM.NEARBY_ADULT_TREEFOLK.get(), nearbyAdultTreefolkList);
        brain.setMemory(MemoryModuleTypesPM.NEAREST_VISIBLE_ADULT_TREEFOLK.get(), nearestVisibleAdultTreefolkList);
    }
}
