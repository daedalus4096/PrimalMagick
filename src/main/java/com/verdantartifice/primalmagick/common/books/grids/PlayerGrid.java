package com.verdantartifice.primalmagick.common.books.grids;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.joml.Vector2i;
import org.joml.Vector2ic;

import com.verdantartifice.primalmagick.common.books.LinguisticsManager;
import com.verdantartifice.primalmagick.common.books.grids.rewards.IReward;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.PacketHandler;
import com.verdantartifice.primalmagick.common.network.packets.scribe_table.UnlockGridNodeActionPacket;

import net.minecraft.server.level.ServerPlayer;
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
    protected final Set<Vector2ic> unlocked = new HashSet<>();
    protected long lastModified = 0L;
    
    public PlayerGrid(Player player, GridDefinition definition) {
        this.player = player;
        this.definition = definition;
    }
    
    public PlayerGrid(Player player, GridDefinition definition, Set<Vector2ic> unlocked, long lastModified) {
        this(player, definition);
        this.unlocked.addAll(unlocked);
        this.lastModified = lastModified;
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
    public Set<Vector2ic> getUnlocked() {
        return Collections.unmodifiableSet(this.unlocked);
    }
    
    public long getLastModified() {
        return this.lastModified;
    }
    
    public boolean unlock(int x, int y) {
        return this.unlock(new Vector2i(x, y));
    }
    
    public boolean unlock(Vector2ic node) {
        if (!this.isUnlockable(node)) {
            return false;
        }
        
        int cost = this.definition.nodes.get(node).vocabularyCost;
        if (!this.player.getAbilities().instabuild && LinguisticsManager.getVocabulary(this.player, this.definition.language) < cost) {
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
                    this.lastModified = linguistics.getGridLastModified(this.definition.getKey());
                    if (!this.player.getAbilities().instabuild) {
                        LinguisticsManager.incrementVocabulary(player, this.definition.language, -cost);
                    }
                    IReward reward = this.definition.nodes.get(node).getReward();
                    if (this.player instanceof ServerPlayer serverPlayer) {
                        reward.grant(serverPlayer);
                    }
                    LinguisticsManager.scheduleSync(this.player);
                    this.unlocked.add(node);
                    retVal.setTrue();
                }
            });
            return retVal.getValue();
        }
    }
    
    public boolean isUnlockable(Vector2ic node) {
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
        
        return true;
    }
    
    protected static boolean areAdjacent(Vector2ic vec1, Vector2ic vec2) {
        return vec1 != null && vec2 != null && vec1.gridDistance(vec2) == 1;
    }
}
