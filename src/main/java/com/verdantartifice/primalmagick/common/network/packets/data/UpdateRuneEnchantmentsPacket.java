package com.verdantartifice.primalmagick.common.network.packets.data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;
import com.verdantartifice.primalmagick.common.runes.RuneEnchantmentDefinition;
import com.verdantartifice.primalmagick.common.runes.RuneEnchantmentDefinitionLoader;
import com.verdantartifice.primalmagick.common.runes.RuneManager;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet to update rune enchantment definition JSON data on the client from the server.
 * 
 * @author Daedalus4096
 */
public class UpdateRuneEnchantmentsPacket implements IMessageToClient {
    protected Map<ResourceLocation, RuneEnchantmentDefinition> definitions;
    
    public UpdateRuneEnchantmentsPacket(Map<ResourceLocation, RuneEnchantmentDefinition> definitions) {
        this.definitions = new HashMap<>(definitions);
    }
    
    public UpdateRuneEnchantmentsPacket(FriendlyByteBuf buf) {
        this.definitions = new HashMap<>();
        int mapSize = buf.readVarInt();
        for (int index = 0; index < mapSize; index++) {
            ResourceLocation loc = buf.readResourceLocation();
            RuneEnchantmentDefinition def = RuneManager.DEFINITION_SERIALIZER.fromNetwork(buf);
            this.definitions.put(loc, def);
        }
    }
    
    public Map<ResourceLocation, RuneEnchantmentDefinition> getDefinitions() {
        return this.definitions;
    }
    
    public static void encode(UpdateRuneEnchantmentsPacket message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.definitions.size());
        for (Map.Entry<ResourceLocation, RuneEnchantmentDefinition> entry : message.definitions.entrySet()) {
            buf.writeResourceLocation(entry.getKey());
            RuneManager.DEFINITION_SERIALIZER.toNetwork(buf, entry.getValue());
        }
    }
    
    public static UpdateRuneEnchantmentsPacket decode(FriendlyByteBuf buf) {
        return new UpdateRuneEnchantmentsPacket(buf);
    }
    
    public static class Handler {
        public static void onMessage(UpdateRuneEnchantmentsPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                RuneEnchantmentDefinitionLoader.createInstance().replaceRuneEnchantments(message.getDefinitions());
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
