package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.HashMap;
import java.util.Map;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.theorycrafting.ProjectTemplate;
import com.verdantartifice.primalmagick.common.theorycrafting.ProjectTemplateLoader;
import com.verdantartifice.primalmagick.common.theorycrafting.TheorycraftManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.NetworkDirection;

/**
 * Packet to update theorycrafting project template JSON data on the client from the server.
 * 
 * @author Daedalus4096
 */
public class UpdateTheorycraftingPacket implements IMessageToClient {
    protected Map<ResourceLocation, ProjectTemplate> templates;
    
    public UpdateTheorycraftingPacket(Map<ResourceLocation, ProjectTemplate> templates) {
        this.templates = new HashMap<>(templates);
    }
    
    public UpdateTheorycraftingPacket(FriendlyByteBuf buf) {
        this.templates = new HashMap<>();
        int mapSize = buf.readVarInt();
        for (int index = 0; index < mapSize; index++) {
            ResourceLocation loc = buf.readResourceLocation();
            ProjectTemplate template = TheorycraftManager.TEMPLATE_SERIALIZER.fromNetwork(buf);
            this.templates.put(loc, template);
        }
    }
    
    public Map<ResourceLocation, ProjectTemplate> getTemplates() {
        return this.templates;
    }
    
    public static NetworkDirection direction() {
        return NetworkDirection.PLAY_TO_CLIENT;
    }
    
    public static void encode(UpdateTheorycraftingPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.templates.size());
        for (Map.Entry<ResourceLocation, ProjectTemplate> entry : message.templates.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            TheorycraftManager.TEMPLATE_SERIALIZER.toNetwork(buf, entry.getValue());
        }
    }
    
    public static UpdateTheorycraftingPacket decode(FriendlyByteBuf buf) {
        return new UpdateTheorycraftingPacket(buf);
    }
    
    public static void onMessage(UpdateTheorycraftingPacket message, CustomPayloadEvent.Context ctx) {
        ProjectTemplateLoader.createInstance().replaceTemplates(message.getTemplates());
    }
}
