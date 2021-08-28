package com.verdantartifice.primalmagic.common.network.packets.data;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagic.common.tiles.base.TilePM;
import com.verdantartifice.primalmagic.common.util.LevelUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

/**
 * Packet to sync tile entity data from the server to the client.  Primarily used to sync tile entity
 * inventory data to the client for rendering purposes.  See the wand charger for an example.
 * 
 * @author Daedalus4096
 */
public class TileToClientPacket implements IMessageToClient {
    protected BlockPos pos;
    protected CompoundTag data;
    
    public TileToClientPacket() {
        this.pos = BlockPos.ZERO;
        this.data = null;
    }
    
    public TileToClientPacket(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }
    
    public static void encode(TileToClientPacket message, FriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeNbt(message.data);
    }
    
    public static TileToClientPacket decode(FriendlyByteBuf buf) {
        TileToClientPacket message = new TileToClientPacket();
        message.pos = buf.readBlockPos();
        message.data = buf.readNbt();
        return message;
    }
    
    public static class Handler {
        @SuppressWarnings("deprecation")
        public static void onMessage(TileToClientPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                Level world = (FMLEnvironment.dist == Dist.CLIENT) ? LevelUtils.getCurrentLevel() : null;
                // Only process tile entities that are currently loaded into the world.  Safety check to prevent
                // resource thrashing from falsified packets.
                if (world != null && world.hasChunkAt(message.pos)) {
                    BlockEntity tile = world.getBlockEntity(message.pos);
                    if (tile != null && tile instanceof TilePM) {
                        ((TilePM)tile).onMessageFromServer(message.data == null ? new CompoundTag() : message.data);
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
