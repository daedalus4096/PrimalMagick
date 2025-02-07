package com.verdantartifice.primalmagick.common.network.packets.spellcrafting;

import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet sent to update a spell package's name on the server in the spellcrafting altar GUI.
 * 
 * @author Daedalus4096
 */
public class SetSpellNamePacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("set_spell_name");
    public static final StreamCodec<RegistryFriendlyByteBuf, SetSpellNamePacket> STREAM_CODEC = StreamCodec.ofMember(SetSpellNamePacket::encode, SetSpellNamePacket::decode);

    protected final int windowId;
    protected final String name;
    
    public SetSpellNamePacket(int windowId, String name) {
        this.windowId = windowId;
        this.name = name;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SetSpellNamePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
        buf.writeUtf(message.name);
    }
    
    public static SetSpellNamePacket decode(RegistryFriendlyByteBuf buf) {
        return new SetSpellNamePacket(buf.readVarInt(), buf.readUtf());
    }
    
    public static void onMessage(PacketContext<SetSpellNamePacket> ctx) {
        SetSpellNamePacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        if (player.containerMenu instanceof SpellcraftingAltarMenu menu && menu.containerId == message.windowId) {
            // Update the spell name if the open menu window matches the given one
            menu.setSpellName(message.name);
        }
    }
}
