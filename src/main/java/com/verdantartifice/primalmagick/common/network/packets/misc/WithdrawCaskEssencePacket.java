package com.verdantartifice.primalmagick.common.network.packets.misc;

import com.verdantartifice.primalmagick.common.items.essence.EssenceItem;
import com.verdantartifice.primalmagick.common.items.essence.EssenceType;
import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.tiles.devices.EssenceCaskTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Packet sent to trigger a server-side withdrawl of essence from an essence cask.
 * 
 * @author Daedalus4096
 */
public class WithdrawCaskEssencePacket implements IMessageToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, WithdrawCaskEssencePacket> STREAM_CODEC = StreamCodec.ofMember(WithdrawCaskEssencePacket::encode, WithdrawCaskEssencePacket::decode);

    protected final EssenceType essenceType;
    protected final Source essenceSource;
    protected final int amount;
    protected final BlockPos caskPos;
    
    public WithdrawCaskEssencePacket(EssenceType type, Source source, int amount, BlockPos pos) {
        this.essenceType = type;
        this.essenceSource = source;
        this.amount = amount;
        this.caskPos = pos;
    }
    
    public static void encode(WithdrawCaskEssencePacket message, RegistryFriendlyByteBuf buf) {
        buf.writeEnum(message.essenceType);
        Source.STREAM_CODEC.encode(buf, message.essenceSource);
        buf.writeVarInt(message.amount);
        buf.writeBlockPos(message.caskPos);
    }
    
    public static WithdrawCaskEssencePacket decode(RegistryFriendlyByteBuf buf) {
        return new WithdrawCaskEssencePacket(buf.readEnum(EssenceType.class), Source.STREAM_CODEC.decode(buf), buf.readVarInt(), buf.readBlockPos());
    }
    
    public static void onMessage(WithdrawCaskEssencePacket message, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
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
    }
}
