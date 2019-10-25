package com.verdantartifice.primalmagic.common.network.packets.misc;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.sources.AffinityManager;
import com.verdantartifice.primalmagic.common.util.InventoryUtils;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.items.IItemHandler;

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
                    IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
                    if (knowledge == null) {
                        return;
                    }
                    
                    boolean found = false;
                    ItemStack posStack = new ItemStack(world.getBlockState(message.pos).getBlock());
                    if (!AffinityManager.isScanned(posStack, player)) {
                        found = AffinityManager.setScanned(posStack, player, false);
                    }
                    IItemHandler handler = InventoryUtils.getItemHandler(world, message.pos, Direction.UP);
                    if (handler != null) {
                        int scanCount = 0;
                        ItemStack chestStack;
                        for (int slot = 0; slot < handler.getSlots(); slot++) {
                            chestStack = handler.getStackInSlot(slot);
                            if (chestStack != null && !chestStack.isEmpty()) {
                                if (scanCount >= AffinityManager.MAX_SCAN_COUNT) {
                                    player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.toobig").applyTextStyle(TextFormatting.RED), true);
                                    break;
                                }
                                if (AffinityManager.setScanned(chestStack, player, false)) {
                                    found = true;
                                }
                                scanCount++;
                            }
                        }
                    }
                    if (found) {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.success").applyTextStyle(TextFormatting.GREEN), true);
                        knowledge.sync(player);
                    } else {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.repeat").applyTextStyle(TextFormatting.RED), true);
                    }
                }
            });
        }
    }
}
