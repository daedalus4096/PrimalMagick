package com.verdantartifice.primalmagick.common.entities.companions;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.capabilities.IPlayerCompanions;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * Primary access point for companion-related methods.  Companions are entities that follow a
 * player around and defend them, similar to a tameable entity.
 * 
 * @author Daedalus4096
 */
public class CompanionManager {
    // Set of unique IDs of players that need their companions synced to their client
    private static final Set<UUID> SYNC_SET = ConcurrentHashMap.newKeySet();

    public static boolean isSyncScheduled(@Nullable Player player) {
        if (player == null) {
            return false;
        } else {
            return SYNC_SET.remove(player.getUUID());
        }
    }
    
    public static void scheduleSync(@Nullable Player player) {
        if (player != null) {
            SYNC_SET.add(player.getUUID());
        }
    }
    
    /**
     * Link the given player and companion.  If the given player is already at the limit for that type of
     * companion, the oldest entity of that type will be killed.
     * 
     * @param player the player to gain the companion
     * @param companion the companion to be added
     */
    public static void addCompanion(@Nullable Player player, @Nullable AbstractCompanionEntity companion) {
        if (player != null && companion != null) {
            companion.setCompanionOwnerId(player.getUUID());
            IPlayerCompanions companions = PrimalMagickCapabilities.getCompanions(player);
            if (companions != null) {
                UUID oldCompanion = companions.add(companion.getCompanionType(), companion.getUUID());
                if (oldCompanion != null && player.level() instanceof ServerLevel serverLevel) {
                    for (ServerLevel serverWorld : serverLevel.getServer().getAllLevels()) {
                        Entity entity = serverWorld.getEntity(oldCompanion);
                        if (entity != null) {
                            entity.kill();
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
    public static void removeCompanion(@Nullable Player player, @Nullable AbstractCompanionEntity companion) {
        if (player != null && companion != null) {
            companion.setCompanionOwnerId(null);
            IPlayerCompanions companions = PrimalMagickCapabilities.getCompanions(player);
            if (companions != null && companions.remove(companion.getCompanionType(), companion.getUUID())) {
                CompanionManager.scheduleSync(player);
            }
        }
    }
    
    public static boolean isCurrentCompanion(@Nullable Player player, @Nullable AbstractCompanionEntity companion) {
        if (player == null || companion == null) {
            return false;
        } else {
            IPlayerCompanions companions = PrimalMagickCapabilities.getCompanions(player);
            return companions != null && companions.contains(companion.getCompanionType(), companion.getUUID());
        }
    }
}
