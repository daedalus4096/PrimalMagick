package com.verdantartifice.primalmagick.common.books.grids;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.joml.Vector2i;

import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.scribe_table.UnlockGridNodeActionPacket;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

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
    
    public boolean unlock(int x, int y) {
        return this.unlock(new Vector2i(x, y));
    }
    
    public boolean unlock(Vector2i node) {
        if (node == null) {
            // Can't unlock a null node
            return false;
        }
        if (this.unlocked.contains(node)) {
            // Can't unlock a node that's already been unlocked
            return false;
        }
        if (!this.definition.isValidPos(node)) {
            // Can't unlock a node that isn't in the grid definition
            return false;
        }
        if (!this.definition.getStartPos().equals(node) && !this.unlocked.stream().anyMatch(v -> areAdjacent(node, v))) {
            // Can't unlock a node that the player doesn't have a path to
            return false;
        }
        
        Level level = this.player.level();
        if (level.isClientSide) {
            // Send packet to server and presume success on the remote
            PacketHandler.sendToServer(new UnlockGridNodeActionPacket(this.definition.getKey(), node));
            this.unlocked.add(node);
            return true;
        } else {
            // Add the unlocked node to the player's linguistics data and update the local cache
            MutableBoolean retVal = new MutableBoolean(false);
            PrimalMagickCapabilities.getLinguistics(this.player).ifPresent(linguistics -> {
                if (linguistics.unlockNode(this.definition.getKey(), node)) {
                    LinguisticsManager.scheduleSync(this.player);
                    this.unlocked.add(node);
                    retVal.setTrue();
                }
            });
            return retVal.getValue();
        }
    }
    
    protected static boolean areAdjacent(Vector2i vec1, Vector2i vec2) {
        return vec1 != null && vec2 != null && vec1.gridDistance(vec2) == 1;
    }
}
