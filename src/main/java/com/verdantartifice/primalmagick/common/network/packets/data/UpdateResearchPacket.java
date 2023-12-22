package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.research.ResearchLoader;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet to update grimoire research JSON data on the client from the server.
 * 
 * @author Daedalus4096
 */
public class UpdateResearchPacket implements IMessageToClient {
    protected List<ResearchEntry> entries;
    
    public UpdateResearchPacket(Collection<ResearchEntry> entries) {
        this.entries = new ArrayList<>(entries);
    }
    
    public UpdateResearchPacket(FriendlyByteBuf buf) {
        this.entries = buf.readList(ResearchEntry::fromNetwork);
    }
    
    public List<ResearchEntry> getEntries() {
        return this.entries;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(UpdateResearchPacket message, FriendlyByteBuf buf) {
        buf.writeCollection(message.entries, ResearchEntry::toNetwork);
    }
    
    public static UpdateResearchPacket decode(FriendlyByteBuf buf) {
        return new UpdateResearchPacket(buf);
    }
    
    public static void onMessage(UpdateResearchPacket message, CustomPayloadEvent.Context ctx) {
        ResearchLoader.createInstance().replaceResearch(message.getEntries());
    }
}
