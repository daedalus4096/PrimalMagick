package com.verdantartifice.primalmagick.common.entities.companions;

import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Primary access point for companion-related methods.  Companions are entities that follow a
 * player around and defend them, similar to a tameable entity.
 * 
 * @author Daedalus4096
 */
public class CompanionManager {
    // Set of unique IDs of players that need their companions synced to their client
    private static final Set<UUID> SYNC_SET = ConcurrentHashMap.newKeySet();

    public static boolean isSyncScheduled(@Nullable Player owner) {
        return owner != null && SYNC_SET.remove(owner.getUUID());
    }
    
    public static void scheduleSync(@Nullable Player owner) {
        if (owner != null) {
            SYNC_SET.add(owner.getUUID());
        }
    }
    
    /**
     * Link the given player and companion.  If the given player is already at the limit for that type of
     * companion, the oldest entity of that type will be killed.
     * 
     * @param owner the player to gain the companion
     * @param companion the companion to be added
     */
    public static void addCompanion(@Nullable Player owner, @Nullable AbstractCompanionEntity companion) {
        if (owner != null && companion != null) {
            companion.setCompanionOwner(owner);
            Services.CAPABILITIES.companions(owner).ifPresent(companions -> {
                UUID oldCompanion = companions.add(companion.getCompanionType(), companion.getUUID());
                if (oldCompanion != null && owner.level() instanceof ServerLevel serverLevel) {
                    for (ServerLevel sl : serverLevel.getServer().getAllLevels()) {
                        Entity entity = sl.getEntity(oldCompanion);
                        if (entity != null) {
                            entity.kill(sl);
                            break;
                        }
                    }
                }
                CompanionManager.scheduleSync(owner);
            });
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
            companion.setCompanionOwner(null);
            Services.CAPABILITIES.companions(player).ifPresent(companions -> {
                if (companions.remove(companion.getCompanionType(), companion.getUUID())) {
                    CompanionManager.scheduleSync(player);
                }
            });
        }
    }
    
    public static boolean isCurrentCompanion(@Nullable Player player, @Nullable AbstractCompanionEntity companion) {
        if (player == null || companion == null) {
            return false;
        } else {
            return Services.CAPABILITIES.companions(player).map(c -> c.contains(companion.getCompanionType(), companion.getUUID())).orElse(false);
        }
    }
}
