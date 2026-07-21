package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import com.verdantartifice.primalmagick.platform.Services;
import commonnetwork.networking.data.PacketContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Packet sent to trigger a server-side scan of a given entity.  Used by the arcanometer for
 * scanning entities in the world.
 * 
 * @author Daedalus4096
 */
public class ScanEntityPacket implements IMessageToServer {
    public static final Identifier CHANNEL = ResourceUtils.loc("scan_entity");
    public static final StreamCodec<RegistryFriendlyByteBuf, ScanEntityPacket> STREAM_CODEC = StreamCodec.composite(
            EntityReference.streamCodec(), p -> p.reference,
            ScanEntityPacket::new
    );

    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final EntityReference<Entity> reference;
    
    public ScanEntityPacket(EntityReference<Entity> reference) {
        this.reference = reference;
    }
    
    public static CustomPacketPayload.Type<CustomPacketPayload> type() {
        return new CustomPacketPayload.Type<>(CHANNEL);
    }

    public static void onMessage(PacketContext<ScanEntityPacket> ctx) {
        ScanEntityPacket message = ctx.message();
        if (message.reference != null) {
            ServerPlayer player = ctx.sender();
            ResearchManager.isScannedAsync(message.reference, player).thenAccept(isScanned -> {
                if (isScanned) {
                    player.sendOverlayMessage(Component.translatable("event.primalmagick.scan.repeat").withStyle(ChatFormatting.RED));
                } else if (ResearchManager.setScanned(message.reference, player)) {
                    player.sendOverlayMessage(Component.translatable("event.primalmagick.scan.success").withStyle(ChatFormatting.GREEN));
                } else {
                    player.sendOverlayMessage(Component.translatable("event.primalmagick.scan.fail").withStyle(ChatFormatting.RED));
                }
            }).exceptionally(e -> {
                LOGGER.error("Failed to scan entity {}", message.reference, e);
                return null;
            });
        }
    }
}
