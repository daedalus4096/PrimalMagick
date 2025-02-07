package com.verdantartifice.primalmagick.common.network.packets.spellcrafting;

import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.spells.SpellComponent;
import com.verdantartifice.primalmagick.common.spells.SpellProperty;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet sent to update the value of a spell component's property on the server in the spellcrafting altar GUI.
 * 
 * @author Daedalus4096
 */
public class SetSpellComponentPropertyPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("set_spell_component_property");
    public static final StreamCodec<RegistryFriendlyByteBuf, SetSpellComponentPropertyPacket> STREAM_CODEC = StreamCodec.ofMember(
            SetSpellComponentPropertyPacket::encode, SetSpellComponentPropertyPacket::decode);

    protected final int windowId;
    protected final SpellComponent attr;
    protected final SpellProperty property;
    protected final int value;

    public SetSpellComponentPropertyPacket(int windowId, SpellComponent attr, SpellProperty property, int value) {
        this.windowId = windowId;
        this.attr = attr;
        this.property = property;
        this.value = value;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SetSpellComponentPropertyPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
        buf.writeEnum(message.attr);
        SpellProperty.STREAM_CODEC.encode(buf, message.property);
        buf.writeVarInt(message.value);
    }
    
    public static SetSpellComponentPropertyPacket decode(RegistryFriendlyByteBuf buf) {
        return new SetSpellComponentPropertyPacket(buf.readVarInt(), buf.readEnum(SpellComponent.class), SpellProperty.STREAM_CODEC.decode(buf), buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<SetSpellComponentPropertyPacket> ctx) {
        SetSpellComponentPropertyPacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        if (player.containerMenu instanceof SpellcraftingAltarMenu altarMenu && altarMenu.containerId == message.windowId) {
            // Update the property value if the open menu window matches the given one
            altarMenu.setSpellPropertyValue(message.attr, message.property, message.value);
        }
    }
}
