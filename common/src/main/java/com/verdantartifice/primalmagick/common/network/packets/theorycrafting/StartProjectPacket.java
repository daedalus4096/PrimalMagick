package com.verdantartifice.primalmagick.common.network.packets.theorycrafting;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.menus.ResearchTableMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftManager;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent to start a research project on the server in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class StartProjectPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, StartProjectPacket> STREAM_CODEC = StreamCodec.ofMember(StartProjectPacket::encode, StartProjectPacket::decode);

    protected final int windowId;

    public StartProjectPacket(int windowId) {
        this.windowId = windowId;
    }
    
    public static void encode(StartProjectPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
    }
    
    public static StartProjectPacket decode(RegistryFriendlyByteBuf buf) {
        return new StartProjectPacket(buf.readVarInt());
    }
    
    public static void onMessage(StartProjectPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            if (player.containerMenu != null && player.containerMenu.containerId == message.windowId && player.containerMenu instanceof ResearchTableMenu menu) {
                menu.getContainerLevelAccess().execute((world, blockPos) -> {
                    knowledge.setActiveResearchProject(TheorycraftManager.createRandomProject(player, blockPos));
                });
                knowledge.sync(player);
            }
        });
    }
}
