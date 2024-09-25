package com.verdantartifice.primalmagick.common.network.packets.theorycrafting;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.theorycrafting.Project;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent to update the selection status of a research project's materials in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class SetProjectMaterialSelectionPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, SetProjectMaterialSelectionPacket> STREAM_CODEC = StreamCodec.ofMember(
            SetProjectMaterialSelectionPacket::encode, SetProjectMaterialSelectionPacket::decode);

    protected final int index;
    protected final boolean selected;
    
    public SetProjectMaterialSelectionPacket(int index, boolean selected) {
        this.index = index;
        this.selected = selected;
    }
    
    public static void encode(SetProjectMaterialSelectionPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.index);
        buf.writeBoolean(message.selected);
    }
    
    public static SetProjectMaterialSelectionPacket decode(RegistryFriendlyByteBuf buf) {
        return new SetProjectMaterialSelectionPacket(buf.readVarInt(), buf.readBoolean());
    }
    
    public static void onMessage(SetProjectMaterialSelectionPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            Project project = knowledge.getActiveResearchProject();
            if (project != null && message.index >= 0 && message.index < project.activeMaterials().size()) {
                project.activeMaterials().get(message.index).setSelected(message.selected);    // No need to sync because the screen updated its end
            }
        });
    }
}
