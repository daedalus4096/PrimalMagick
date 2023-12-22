package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkDirection;

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
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
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
    
    @SuppressWarnings("deprecation")
    public static void onMessage(TileToClientPacket message, CustomPayloadEvent.Context ctx) {
        Level world = (FMLEnvironment.dist == Dist.CLIENT) ? ClientUtils.getCurrentLevel() : null;
        // Only process tile entities that are currently loaded into the world.  Safety check to prevent
        // resource thrashing from falsified packets.
        if (world != null && world.hasChunkAt(message.pos)) {
            if (world.getBlockEntity(message.pos) instanceof AbstractTilePM tile) {
                tile.onMessageFromServer(message.data == null ? new CompoundTag() : message.data);
            }
        }
    }
}
