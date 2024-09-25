package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.verdantartifice.primalmagick.common.advancements.critereon.CriteriaTriggersPM;
import com.verdantartifice.primalmagick.common.affinities.AffinityManager;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.items.IItemHandler;

/**
 * Packet sent to trigger a server-side scan of a particular block in the world.  Used by the
 * arcanometer.  Will also scan inventory contents if the block has an inventory.
 * 
 * @author Daedalus4096
 */
public class ScanPositionPacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, ScanPositionPacket> STREAM_CODEC = StreamCodec.ofMember(ScanPositionPacket::encode, ScanPositionPacket::decode);

    protected static final Logger LOGGER = LogManager.getLogger();
    
    protected final BlockPos pos;
    protected final ItemStack toolStack;
    
    public ScanPositionPacket(@Nonnull BlockPos pos, @Nonnull ItemStack toolStack) {
        this.pos = pos;
        this.toolStack = toolStack;
    }
    
    public static void encode(ScanPositionPacket message, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(message.pos);
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, message.toolStack);
    }
    
    public static ScanPositionPacket decode(RegistryFriendlyByteBuf buf) {
        return new ScanPositionPacket(buf.readBlockPos(), ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
    }
    
    public static void onMessage(ScanPositionPacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        Level world = player.getCommandSenderWorld();

        // Only process blocks that are currently loaded into the world.  Safety check to prevent
        // resource thrashing from falsified packets.
        if (message.pos != null && world.isLoaded(message.pos)) {
            PrimalMagickCapabilities.getKnowledge(player).ifPresent(knowledge -> {
                // Scan the block
                List<CompletableFuture<Boolean>> foundFutures = new ArrayList<>();
                ItemStack posStack = new ItemStack(world.getBlockState(message.pos).getBlock());
                foundFutures.add(CompletableFuture.completedFuture(posStack).thenCombine(ResearchManager.isScannedAsync(posStack, player), (stack, isScanned) -> {
                    return !isScanned && ResearchManager.setScanned(stack, player, false);
                }));
                
                // If the given block has an inventory, scan its contents too
                IItemHandler handler = InventoryUtils.getItemHandler(world, message.pos, Direction.UP);
                if (handler != null) {
                    int scanCount = 0;
                    ItemStack chestStack;
                    for (int slot = 0; slot < handler.getSlots(); slot++) {
                        chestStack = handler.getStackInSlot(slot);
                        if (chestStack != null && !chestStack.isEmpty()) {
                            // Limit how much of an inventory can be scanned
                            if (scanCount >= AffinityManager.MAX_SCAN_COUNT) {
                                player.displayClientMessage(Component.translatable("event.primalmagick.scan.toobig").withStyle(ChatFormatting.RED), true);
                                break;
                            } else {
                                foundFutures.add(CompletableFuture.completedFuture(chestStack).thenCombine(ResearchManager.isScannedAsync(chestStack, player), (stack, isScanned) -> {
                                    return !isScanned && ResearchManager.setScanned(stack, player, false);
                                }));
                                scanCount++;
                            }
                        }
                    }
                }
                
                // If at least one unscanned item was processed, send a success message
                Util.sequence(foundFutures).thenAccept(foundList -> {
                    if (foundList.stream().mapToInt(found -> found ? 1 : 0).sum() > 0) {
                        player.displayClientMessage(Component.translatable("event.primalmagick.scan.success").withStyle(ChatFormatting.GREEN), true);
                        knowledge.sync(player); // Sync immediately, rather than scheduling, for snappy arcanometer response
                    } else {
                        player.displayClientMessage(Component.translatable("event.primalmagick.scan.repeat").withStyle(ChatFormatting.RED), true);
                    }
                }).exceptionally(e -> {
                    LOGGER.error("Failed to scan block at position " + message.pos, e);
                    return null;
                });
                
                // Trigger any relevant advancement criteria
                CriteriaTriggersPM.SCAN_LOCATION.trigger(player, message.pos, message.toolStack);
            });
        }
    }
}
