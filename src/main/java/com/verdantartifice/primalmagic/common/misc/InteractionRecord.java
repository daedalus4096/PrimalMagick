package com.verdantartifice.primalmagic.common.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

/**
 * Data structure recording player interactions with blocks, per PlayerInteractEvent events.
 * 
 * @author Daedalus4096
 */
public class InteractionRecord {
    protected final PlayerEntity player;
    protected final Hand hand;
    protected final BlockPos pos;
    protected final Direction face;
    
    public InteractionRecord(PlayerEntity player, Hand hand, BlockPos pos, Direction face) {
        this.player = player;
        this.hand = hand;
        this.pos = pos;
        this.face = face;
    }
    
    public PlayerEntity getPlayer() {
        return this.player;
    }
    
    public Hand getHand() {
        return this.hand;
    }
    
    public BlockPos getBlockPos() {
        return this.pos;
    }
    
    public Direction getFace() {
        return this.face;
    }
}
