package com.verdantartifice.primalmagick.common.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/**
 * Data structure recording player interactions with blocks, per PlayerInteractEvent events.
 * 
 * @author Daedalus4096
 */
public class InteractionRecord {
    protected final Player player;
    protected final InteractionHand hand;
    protected final BlockPos pos;
    protected final Direction face;
    
    public InteractionRecord(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        this.player = player;
        this.hand = hand;
        this.pos = pos;
        this.face = face;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public InteractionHand getHand() {
        return this.hand;
    }
    
    public BlockPos getBlockPos() {
        return this.pos;
    }
    
    public Direction getFace() {
        return this.face;
    }
}
