package com.verdantartifice.primalmagick.common.network.packets.data;

import com.verdantartifice.primalmagick.client.util.ClientUtils;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.tiles.base.AbstractTilePM;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * Packet to sync tile entity data from the server to the client.  Primarily used to sync tile entity
 * inventory data to the client for rendering purposes.  See the wand charger for an example.
 * 
 * @author Daedalus4096
 */
public class TileToClientPacket implements IMessageToClient {
    public static final StreamCodec<RegistryFriendlyByteBuf, TileToClientPacket> STREAM_CODEC = StreamCodec.ofMember(TileToClientPacket::encode, TileToClientPacket::decode);

    protected final BlockPos pos;
    protected final CompoundTag data;
    
    public TileToClientPacket(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }
    
    public static void encode(TileToClientPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        buf.writeNbt(message.data);
    }
    
    public static TileToClientPacket decode(RegistryFriendlyByteBuf buf) {
        return new TileToClientPacket(buf.readBlockPos(), buf.readNbt());
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
