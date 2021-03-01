package com.verdantartifice.primalmagic.common.network.packets.misc;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.common.affinities.AffinityController;
import com.verdantartifice.primalmagic.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagic.common.capabilities.PrimalMagicCapabilities;
import com.verdantartifice.primalmagic.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagic.common.research.ResearchManager;
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

/**
 * Packet sent to trigger a server-side scan of a particular block in the world.  Used by the
 * arcanometer.  Will also scan inventory contents if the block has an inventory.
 * 
 * @author Daedalus4096
 */
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
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayerEntity player = ctx.get().getSender();
                World world = player.getEntityWorld();

                // Only process blocks that are currently loaded into the world.  Safety check to prevent
                // resource thrashing from falsified packets.
                if (message.pos != null && world.isBlockPresent(message.pos)) {
                    IPlayerKnowledge knowledge = PrimalMagicCapabilities.getKnowledge(player);
                    if (knowledge == null) {
                        return;
                    }
                    
                    // Scan the block
                    boolean found = false;
                    ItemStack posStack = new ItemStack(world.getBlockState(message.pos).getBlock());
                    if (!ResearchManager.isScanned(posStack, player)) {
                        // Delay syncing until scan is done
                        found = ResearchManager.setScanned(posStack, player, false);
                    }
                    
                    // If the given block has an inventory, scan its contents too
                    IItemHandler handler = InventoryUtils.getItemHandler(world, message.pos, Direction.UP);
                    if (handler != null) {
                        int scanCount = 0;
                        ItemStack chestStack;
                        for (int slot = 0; slot < handler.getSlots(); slot++) {
                            chestStack = handler.getStackInSlot(slot);
                            if (chestStack != null && !chestStack.isEmpty()) {
                                // Limit how much of an inventory can be scanned
                                if (scanCount >= AffinityController.MAX_SCAN_COUNT) {
                                    player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.toobig").mergeStyle(TextFormatting.RED), true);
                                    break;
                                }
                                if (ResearchManager.setScanned(chestStack, player, false)) {
                                    // Delay syncing until scan is done
                                    found = true;
                                }
                                scanCount++;
                            }
                        }
                    }
                    
                    // If at least one unscanned item was processed, send a success message
                    if (found) {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.success").mergeStyle(TextFormatting.GREEN), true);
                        knowledge.sync(player); // Sync immediately, rather than scheduling, for snappy arcanometer response
                    } else {
                        player.sendStatusMessage(new TranslationTextComponent("event.primalmagic.scan.repeat").mergeStyle(TextFormatting.RED), true);
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
