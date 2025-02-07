package com.verdantartifice.primalmagick.common.network.packets.theorycrafting;

import com.verdantartifice.primalmagick.common.menus.ResearchTableMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftManager;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet sent to start a research project on the server in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class StartProjectPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("start_project");
    public static final StreamCodec<RegistryFriendlyByteBuf, StartProjectPacket> STREAM_CODEC = StreamCodec.ofMember(StartProjectPacket::encode, StartProjectPacket::decode);

    protected final int windowId;

    public StartProjectPacket(int windowId) {
        this.windowId = windowId;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(StartProjectPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
    }
    
    public static StartProjectPacket decode(RegistryFriendlyByteBuf buf) {
        return new StartProjectPacket(buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<StartProjectPacket> ctx) {
        StartProjectPacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        Services.CAPABILITIES.knowledge(player).ifPresent(knowledge -> {
            if (player.containerMenu instanceof ResearchTableMenu menu && player.containerMenu.containerId == message.windowId) {
                menu.getContainerLevelAccess().execute((world, blockPos) -> {
                    knowledge.setActiveResearchProject(TheorycraftManager.createRandomProject(player, blockPos));
                });
                knowledge.sync(player);
            }
        });
    }
}
