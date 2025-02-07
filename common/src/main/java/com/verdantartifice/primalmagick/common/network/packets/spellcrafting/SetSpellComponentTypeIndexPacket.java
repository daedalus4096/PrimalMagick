package com.verdantartifice.primalmagick.common.network.packets.spellcrafting;

import com.verdantartifice.primalmagick.common.menus.SpellcraftingAltarMenu;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.spells.SpellComponent;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Packet sent to update the value of a spell component's type on the server in the spellcrafting altar GUI.
 * 
 * @author Daedalus4096
 */
public class SetSpellComponentTypeIndexPacket implements IMessageToServer {
    public static final ResourceLocation CHANNEL = ResourceUtils.loc("set_spell_component_type_index");
    public static final StreamCodec<RegistryFriendlyByteBuf, SetSpellComponentTypeIndexPacket> STREAM_CODEC = StreamCodec.ofMember(
            SetSpellComponentTypeIndexPacket::encode, SetSpellComponentTypeIndexPacket::decode);

    protected final int windowId;
    protected final SpellComponent attr;
    protected final int index;

    public SetSpellComponentTypeIndexPacket(int windowId, SpellComponent attr, int index) {
        this.windowId = windowId;
        this.attr = attr;
        this.index = index;
    }

    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void encode(SetSpellComponentTypeIndexPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(message.windowId);
        buf.writeEnum(message.attr);
        buf.writeVarInt(message.index);
    }
    
    public static SetSpellComponentTypeIndexPacket decode(RegistryFriendlyByteBuf buf) {
        return new SetSpellComponentTypeIndexPacket(buf.readVarInt(), buf.readEnum(SpellComponent.class), buf.readVarInt());
    }
    
    public static void onMessage(PacketContext<SetSpellComponentTypeIndexPacket> ctx) {
        SetSpellComponentTypeIndexPacket message = ctx.message();
        ServerPlayer player = ctx.sender();
        if (player.containerMenu instanceof SpellcraftingAltarMenu altarMenu && altarMenu.containerId == message.windowId) {
            // Update the appropriate spell component type if the open menu window matches the given one
            switch (message.attr) {
            case VEHICLE:
                altarMenu.setSpellVehicleTypeIndex(message.index);
                break;
            case PAYLOAD:
                altarMenu.setSpellPayloadTypeIndex(message.index);
                break;
            case PRIMARY_MOD:
                altarMenu.setSpellPrimaryModTypeIndex(message.index);
                break;
            case SECONDARY_MOD:
                altarMenu.setSpellSecondaryModTypeIndex(message.index);
                break;
            default:
                // Do nothing
            }
        }
    }
}
