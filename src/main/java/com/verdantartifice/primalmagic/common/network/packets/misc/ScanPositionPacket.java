package com.verdantartifice.primalmagic.common.network.packets.misc;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class ScanPositionPacket implements IMessageToServer {
    protected BlockPos pos;
    
    public ScanPositionPacket() {
        this.pos = BlockPos.ZERO;
    }
    
    public ScanPositionPacket(@Nonnull BlockPos pos) {
        this.pos = pos;
    }
    
    public static void encode(ScanPositionPacket message, PacketBuffer buf) {
        buf.writeLong(message.pos.toLong());
    }
    
    public static ScanPositionPacket decode(PacketBuffer buf) {
        ScanPositionPacket message = new ScanPositionPacket();
        message.pos = BlockPos.fromLong(buf.readLong());
        return message;
    }
    
    public static class Handler {
        public static void onMessage(ScanPositionPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                World world = player.getEntityWorld();
                if (message.pos != null && world.isBlockPresent(message.pos)) {
                    boolean found = false;
                    ItemStack posStack = new ItemStack(world.getBlockState(message.pos).getBlock());
                    if (!AffinityManager.isScanned(posStack, player)) {
                        found = AffinityManager.setScanned(posStack, player);
                    }
                    // TODO determine if pos is a container with scannable stuff
                    if (found) {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.success").applyTextStyle(TextFormatting.GREEN), true);
                    } else {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.repeat").applyTextStyle(TextFormatting.RED), true);
                    }
                }
            });
        }
    }
}
