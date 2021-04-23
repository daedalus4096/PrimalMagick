package com.verdantartifice.primalmagic.common.entities.companions;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

/**
 * Primary access point for companion-related methods.  Companions are entities that follow a
 * player around and defend them, similar to a tameable entity.
 * 
 * @author Daedalus4096
 */
public class CompanionManager {
    // Set of unique IDs of players that need their companions synced to their client
    private static final Set<UUID> SYNC_SET = ConcurrentHashMap.newKeySet();

    public static boolean isSyncScheduled(@Nullable PlayerEntity player) {
        if (player == null) {
            return false;
        } else {
            return SYNC_SET.remove(player.getUniqueID());
        }
    }
    
    public static void scheduleSync(@Nullable PlayerEntity player) {
        if (player != null) {
            SYNC_SET.add(player.getUniqueID());
        }
    }
    
    /**
     * Link the given player and companion.  If the given player is already at the limit for that type of
     * companion, the oldest entity of that type will be killed.
     * 
     * @param player the player to gain the companion
     * @param companion the companion to be added
     */
    public static void addCompanion(@Nullable PlayerEntity player, @Nullable AbstractCompanionEntity companion) {
        if (player != null && companion != null) {
            companion.setCompanionOwnerId(player.getUniqueID());
            IPlayerCompanions companions = PrimalMagicCapabilities.getCompanions(player);
            if (companions != null) {
                UUID oldCompanion = companions.add(companion.getCompanionType(), companion.getUniqueID());
                if (oldCompanion != null && player.world instanceof ServerWorld) {
                    for (ServerWorld serverWorld : ((ServerWorld)player.world).getServer().getWorlds()) {
                        Entity entity = serverWorld.getEntityByUuid(oldCompanion);
                        if (entity != null) {
                            entity.onKillCommand();
                            break;
                        }
                    }
                }
                CompanionManager.scheduleSync(player);
            }
        }
    }
    
    /**
     * Remove the companion link between the given player and the given entity, if present.
     * 
     * @param player the player to lose the companion
     * @param companion the companion to be removed
     */
    public static void removeCompanion(@Nullable PlayerEntity player, @Nullable AbstractCompanionEntity companion) {
        if (player != null && companion != null) {
            companion.setCompanionOwnerId(null);
            IPlayerCompanions companions = PrimalMagicCapabilities.getCompanions(player);
            if (companions != null && companions.remove(companion.getCompanionType(), companion.getUniqueID())) {
                CompanionManager.scheduleSync(player);
            }
        }
    }
}
