package com.verdantartifice.primalmagick.common.books.grids;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.joml.Vector2i;

import net.minecraft.world.entity.player.Player;

/**
 * Definition of a player's instance of a linguistics grid, including any and all nodes they've
 * unlocked.  Serves as a front-end to the linguistics capability for querying and unlocking
 * nodes.
 * 
 * @author Daedalus4096
 */
public class PlayerGrid {
    protected final Player player;
    protected final GridDefinition definition;
    protected final Set<Vector2i> unlocked = new HashSet<>();
    
    public PlayerGrid(Player player, GridDefinition definition) {
        this.player = player;
        this.definition = definition;
    }
    
    public PlayerGrid(Player player, GridDefinition definition, Set<Vector2i> unlocked) {
        this(player, definition);
        this.unlocked.addAll(unlocked);
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public GridDefinition getDefinition() {
        return this.definition;
    }
    
    /**
     * Gets an unmodifiable copy of the grid's currently unlocked nodes.
     * 
     * @return an unmodifiable copy of the grid's currently unlocked nodes
     */
    public Set<Vector2i> getUnlocked() {
        return Collections.unmodifiableSet(this.unlocked);
    }
}
