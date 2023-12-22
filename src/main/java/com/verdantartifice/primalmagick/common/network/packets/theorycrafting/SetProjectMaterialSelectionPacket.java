package com.verdantartifice.primalmagick.common.network.packets.theorycrafting;

import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.theorycrafting.Project;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet sent to update the selection status of a research project's materials in the research table GUI.
 * 
 * @author Daedalus4096
 */
public class SetProjectMaterialSelectionPacket implements IMessageToServer {
    protected int index;
    protected boolean selected;
    
    public SetProjectMaterialSelectionPacket() {
        this.index = -1;
        this.selected = false;
    }
    
    public SetProjectMaterialSelectionPacket(int index, boolean selected) {
        this.index = index;
        this.selected = selected;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_SERVER;
    }
    
    public static void encode(SetProjectMaterialSelectionPacket message, FriendlyByteBuf buf) {
        buf.writeInt(message.index);
        buf.writeBoolean(message.selected);
    }
    
    public static SetProjectMaterialSelectionPacket decode(FriendlyByteBuf buf) {
        SetProjectMaterialSelectionPacket message = new SetProjectMaterialSelectionPacket();
        message.index = buf.readInt();
        message.selected = buf.readBoolean();
        return message;
    }
    
    public static void onMessage(SetProjectMaterialSelectionPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
            Project project = knowledge.getActiveResearchProject();
            if (project != null && message.index >= 0 && message.index < project.getMaterials().size()) {
                project.getMaterials().get(message.index).setSelected(message.selected);    // No need to sync because the screen updated its end
            }
        });
    }
}
