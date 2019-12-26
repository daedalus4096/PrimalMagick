package com.verdantartifice.primalmagic.common.tiles.base;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.network.PacketHandler;
import com.verdantartifice.primalmagic.common.network.packets.data.TileToClientPacket;
import com.verdantartifice.primalmagic.common.network.packets.data.TileToServerPacket;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TilePM extends TileEntity {
    public TilePM(TileEntityType<?> type) {
        super(type);
    }
    
    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.readFromTileNBT(compound);
    }
    
    protected void readFromTileNBT(CompoundNBT compound) {
        // Do nothing by default
    }
    
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(this.writeToTileNBT(compound));
    }
    
    protected CompoundNBT writeToTileNBT(CompoundNBT compound) {
        // Do nothing by default
        return compound;
    }
    
    public void syncTile(boolean rerender) {
        BlockState state = this.world.getBlockState(this.pos);
        this.world.notifyBlockUpdate(this.pos, state, state, (rerender ? 0x6 : 0x2));
    }
    
    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }
    
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.readFromTileNBT(pkt.getNbtCompound());
    }
    
    public void sendMessageToClient(CompoundNBT nbt, @Nullable ServerPlayerEntity player) {
        if (player == null) {
            if (this.hasWorld()) {
                PacketHandler.sendToAllAround(new TileToClientPacket(this.pos, nbt), this.world.dimension.getType(), this.pos, 128.0D);
            }
        } else {
            PacketHandler.sendToPlayer(new TileToClientPacket(this.pos, nbt), player);
        }
    }
    
    public void sendMessageToServer(CompoundNBT nbt) {
        PacketHandler.sendToServer(new TileToServerPacket(this.pos, nbt));
    }
    
    public void onMessageFromClient(CompoundNBT nbt, @Nonnull ServerPlayerEntity player) {
        // Do nothing by default
    }
    
    public void onMessageFromServer(CompoundNBT nbt) {
        // Do nothing by default
    }
}
