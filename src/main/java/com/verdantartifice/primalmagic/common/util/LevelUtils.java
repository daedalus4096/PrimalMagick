package com.verdantartifice.primalmagic.common.util;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

/**
 * Collection of utility methods pertaining to levels.
 * 
 * @author Daedalus4096
 */
public class LevelUtils {
    /**
     * Gets the current client-side level.
     * 
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     * 
     * @return the current client-side level
     */
    @Nullable
    public static Level getCurrentLevel() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level;
    }
}
