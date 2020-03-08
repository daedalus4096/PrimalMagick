package com.verdantartifice.primalmagic.common.network.packets.theorycrafting;

import java.util.function.Supplier;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.theorycrafting.AbstractProject;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

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
    
    public static void encode(SetProjectMaterialSelectionPacket message, PacketBuffer buf) {
        buf.writeInt(message.index);
        buf.writeBoolean(message.selected);
    }
    
    public static SetProjectMaterialSelectionPacket decode(PacketBuffer buf) {
        SetProjectMaterialSelectionPacket message = new SetProjectMaterialSelectionPacket();
        message.index = buf.readInt();
        message.selected = buf.readBoolean();
        return message;
    }
    
    public static class Handler {
        public static void onMessage(SetProjectMaterialSelectionPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
                if (knowledge != null) {
                    AbstractProject project = knowledge.getActiveResearchProject();
                    if (project != null && message.index >= 0 && message.index < project.getMaterials().size()) {
                        project.getMaterials().get(message.index).setSelected(message.selected);    // No need to sync because the screen updated its end
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
