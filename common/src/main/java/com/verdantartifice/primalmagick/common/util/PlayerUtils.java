package com.verdantartifice.primalmagick.common.util;

import net.minecraft.world.entity.player.Player;

/**
 * Collection of utility methods pertaining to players.
 *
 * @author Daedalus4096
 */
public class PlayerUtils {
    /**
     * @see Player#getXpNeededForNextLevel()
     */
    public static int getXpNeededForNextLevel(int currentLevel) {
        if (currentLevel >= 30) {
            return 112 + (currentLevel - 30) * 9;
        } else {
            return currentLevel >= 15 ? 37 + (currentLevel - 15) * 5 : 7 + currentLevel * 2;
        }
    }

    public static int getCumulativeXpNeededForLevel(int targetLevel) {
        int retVal = 0;
        for (int level = 0; level < targetLevel; level++) {
            retVal += getXpNeededForNextLevel(level);
        }
        return retVal;
    }

    public static boolean canAffordXp(Player player, int pointCost) {
        final int xpForLevel = PlayerUtils.getCumulativeXpNeededForLevel(player.experienceLevel);
        final double xpForProgress = Math.floor(player.experienceProgress * PlayerUtils.getXpNeededForNextLevel(player.experienceLevel));
        return (xpForLevel + xpForProgress) >= pointCost;
    }
}
