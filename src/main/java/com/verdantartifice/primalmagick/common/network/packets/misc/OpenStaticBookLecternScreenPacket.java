package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.client.gui.StaticBookLecternScreen;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToClient;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Packet sent from the server to trigger the opening of a lectern-placed static book on the client.
 * This is a dirty hack to get around the fact that we're opening a screen not normally bound to
 * this type of menu.
 * 
 * @author Daedalus4096
 */
public class OpenStaticBookLecternScreenPacket implements IMessageToClient {
    private static final Logger LOGGER = LogManager.getLogger();
    
    private final int id;
    private final int windowId;
    private final Component name;

    @SuppressWarnings("deprecation")
    public OpenStaticBookLecternScreenPacket(int windowId, Component name) {
        this(BuiltInRegistries.MENU.getId(MenuType.LECTERN), windowId, name);
    }
    
    private OpenStaticBookLecternScreenPacket(int id, int windowId, Component name) {
        this.id = id;
        this.windowId = windowId;
        this.name = name;
    }
    
    public static void encode(OpenStaticBookLecternScreenPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.id);
        buf.writeVarInt(msg.windowId);
        buf.writeComponent(msg.name);
    }
    
    public static OpenStaticBookLecternScreenPacket decode(FriendlyByteBuf buf) {
        return new OpenStaticBookLecternScreenPacket(buf.readVarInt(), buf.readVarInt(), buf.readComponent());
    }
    
    @SuppressWarnings("deprecation")
    public final MenuType<?> getType() {
        return BuiltInRegistries.MENU.byId(this.id);
    }
    
    public static class Handler {
        public static void onMessage(OpenStaticBookLecternScreenPacket msg, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                AbstractContainerMenu c = msg.getType().create(msg.windowId, mc.player.getInventory());
                if (c instanceof LecternMenu lecternMenu) {
                    StaticBookLecternScreen s = new StaticBookLecternScreen(lecternMenu, mc.player.getInventory(), msg.name);
                    mc.player.containerMenu = s.getMenu();
                    mc.setScreen(s);
                } else {
                    LOGGER.warn("Cannot open static book lectern screen for unexpected menu type {}", ForgeRegistries.MENU_TYPES.getKey(msg.getType()).toString());
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
