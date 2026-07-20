package com.verdantartifice.primalmagick.client.util;

import com.verdantartifice.primalmagick.client.gui.GrimoireScreen;
import com.verdantartifice.primalmagick.client.gui.StaticBookViewScreen;
import com.verdantartifice.primalmagick.common.books.BookType;
import com.verdantartifice.primalmagick.common.books.BookView;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/**
 * Collection of utility methods for accessing client-side data.
 * 
 * @author Daedalus4096
 */
public class ClientUtils {
    /**
     * Gets the current client-side player.
     * <br/>
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     * 
     * @return the current client-side player
     */
    @Nullable
    public static Player getCurrentPlayer() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player;
    }

    /**
     * Gets the current client-side level.
     * <br/>
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     * 
     * @return the current client-side level
     */
    @Nullable
    public static Level getCurrentLevel() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level;
    }
    
    /**
     * Gets the stats counter for the current client-side player.
     * <br/>
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     * 
     * @return the stats counter for the current player
     */
    @Nullable
    public static StatsCounter getStatsCounter() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null ? mc.player.getStats() : null;
    }
    
    /**
     * Gets whether the player has the shift key held down.
     * <br/>
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     * 
     * @return whether the shift key is down
     */
    public static boolean hasShiftDown() {
        return Minecraft.getInstance().hasShiftDown();
    }
    
    /**
     * Opens the grimoire GUI on the client.
     * <br/>
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     */
    public static void openGrimoireScreen() {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new GrimoireScreen());
    }
    
    /**
     * Opens the static book GUI on the client for the given book ID.
     * <br/>
     * ONLY CALL THIS METHOD AFTER CHECKING YOUR CURRENT FMLENVIRONMENT DIST.
     * 
     * @param view the view of the static book whose resources to load
     * @param bookType the type of book to be opened (e.g. book, tablet).
     */
    public static void openStaticBookScreen(BookView view, BookType bookType) {
        Minecraft mc = Minecraft.getInstance();
        mc.setScreen(new StaticBookViewScreen(view, bookType));
    }
}
