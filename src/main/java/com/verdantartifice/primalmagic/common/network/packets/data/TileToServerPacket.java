package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class TileToServerPacket implements IMessageToServer {
    protected BlockPos pos;
    protected CompoundNBT data;
    
    public TileToServerPacket() {
        this.pos = BlockPos.ZERO;
        this.data = null;
    }
    
    public TileToServerPacket(BlockPos pos, CompoundNBT data) {
        this.pos = pos;
        this.data = data;
    }
    
    public static void encode(TileToServerPacket message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
        buf.writeCompoundTag(message.data);
    }
    
    public static TileToServerPacket decode(PacketBuffer buf) {
        TileToServerPacket message = new TileToServerPacket();
        message.pos = buf.readBlockPos();
        message.data = buf.readCompoundTag();
        return message;
    }
    
    public static class Handler {
        @SuppressWarnings("deprecation")
        public static void onMessage(TileToServerPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity sender = ctx.get().getSender();
                World world = sender.world;
                if (world != null && world.isBlockLoaded(message.pos)) {
                    TileEntity tile = world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TilePM) {
                        ((TilePM)tile).onMessageFromClient(message.data == null ? new CompoundNBT() : message.data, sender);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
