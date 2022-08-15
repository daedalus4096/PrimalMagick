package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceCaskTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet sent to trigger a server-side withdrawl of essence from an essence cask.
 * 
 * @author Daedalus4096
 */
public class WithdrawCaskEssencePacket implements IMessageToServer {
    protected EssenceType essenceType;
    protected Source essenceSource;
    protected int amount;
    protected BlockPos caskPos;
    
    public WithdrawCaskEssencePacket() {
        this.essenceType = null;
        this.essenceSource = null;
        this.amount = -1;
        this.caskPos = BlockPos.ZERO;
    }
    
    public WithdrawCaskEssencePacket(EssenceType type, Source source, int amount, BlockPos pos) {
        this.essenceType = type;
        this.essenceSource = source;
        this.amount = amount;
        this.caskPos = pos;
    }
    
    public static void encode(WithdrawCaskEssencePacket message, FriendlyByteBuf buf) {
        buf.writeUtf(message.essenceType.toString());
        buf.writeUtf(message.essenceSource.getTag());
        buf.writeVarInt(message.amount);
        buf.writeLong(message.caskPos.asLong());
    }
    
    public static WithdrawCaskEssencePacket decode(FriendlyByteBuf buf) {
        WithdrawCaskEssencePacket message = new WithdrawCaskEssencePacket();
        message.essenceType = EssenceType.valueOf(buf.readUtf());
        message.essenceSource = Source.getSource(buf.readUtf());
        message.amount = buf.readVarInt();
        message.caskPos = BlockPos.of(buf.readLong());
        return message;
    }
    
    public static class Handler {
        public static void onMessage(WithdrawCaskEssencePacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                Level level = player.getCommandSenderWorld();

                // Only process blocks that are currently loaded into the world.  Safety check to prevent
                // resource thrashing from falsified packets.
                if (message.caskPos != null && level.isLoaded(message.caskPos)) {
                    BlockEntity tile = level.getBlockEntity(message.caskPos);
                    if (tile instanceof EssenceCaskTileEntity caskTile) {
                        int curCount = caskTile.getEssenceCount(message.essenceType, message.essenceSource);
                        int toRemove = Math.min(message.amount, curCount);
                        if (toRemove > 0) {
                            int newCount = Math.max(0, curCount - toRemove);
                            caskTile.setEssenceCount(message.essenceType, message.essenceSource, newCount);
                            ItemStack stack = EssenceItem.getEssence(message.essenceType, message.essenceSource, toRemove);
                            if (!player.getInventory().add(stack)) {
                                player.drop(stack, false);
                            }
                        }
                    }
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
    }
}
