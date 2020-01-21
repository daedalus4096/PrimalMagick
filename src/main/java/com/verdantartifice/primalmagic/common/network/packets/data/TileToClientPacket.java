package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Packet to sync tile entity data from the server to the client.  Primarily used to sync tile entity
 * inventory data to the client for rendering purposes.  See the wand charger for an example.
 * 
 * @author Daedalus4096
 */
public class TileToClientPacket implements IMessageToClient {
    protected BlockPos pos;
    protected CompoundNBT data;
    
    public TileToClientPacket() {
        this.pos = BlockPos.ZERO;
        this.data = null;
    }
    
    public TileToClientPacket(BlockPos pos, CompoundNBT data) {
        this.pos = pos;
        this.data = data;
    }
    
    public static void encode(TileToClientPacket message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
        buf.writeCompoundTag(message.data);
    }
    
    public static TileToClientPacket decode(PacketBuffer buf) {
        TileToClientPacket message = new TileToClientPacket();
        message.pos = buf.readBlockPos();
        message.data = buf.readCompoundTag();
        return message;
    }
    
    public static class Handler {
        @SuppressWarnings("deprecation")
        public static void onMessage(TileToClientPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                World world = Minecraft.getInstance().world;
                // Only process tile entities that are currently loaded into the world.  Safety check to prevent
                // resource thrashing from falsified packets.
                if (world != null && world.isBlockLoaded(message.pos)) {
                    TileEntity tile = world.getTileEntity(message.pos);
                    if (tile != null && tile instanceof TilePM) {
                        ((TilePM)tile).onMessageFromServer(message.data == null ? new CompoundNBT() : message.data);
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
