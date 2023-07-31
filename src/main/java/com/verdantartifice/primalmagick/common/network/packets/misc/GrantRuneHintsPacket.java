package com.verdantartifice.primalmagick.common.network.packets.misc;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import com.verdantartifice.primalmagick.common.network.packets.IMessageToServer;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.SimpleResearchKey;
import com.verdantartifice.primalmagick.common.runes.RuneManager;
import com.verdantartifice.primalmagick.common.runes.RuneType;
import com.verdantartifice.primalmagick.common.util.WeightedRandomBag;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.network.NetworkEvent;

public class GrantRuneHintsPacket implements IMessageToServer {
    protected ItemStack stack;
    
    public GrantRuneHintsPacket() {
        this.stack = ItemStack.EMPTY;
    }
    
    public GrantRuneHintsPacket(ItemStack stack) {
        this.stack = stack;
    }
    
    public static void encode(GrantRuneHintsPacket message, FriendlyByteBuf buf) {
        buf.writeItem(message.stack);
    }
    
    public static GrantRuneHintsPacket decode(FriendlyByteBuf buf) {
        GrantRuneHintsPacket message = new GrantRuneHintsPacket();
        message.stack = buf.readItem();
        return message;
    }

    public static class Handler {
        protected static final List<RuneType> RUNE_TYPES = List.of(RuneType.VERB, RuneType.NOUN, RuneType.SOURCE);
        
        public static void onMessage(GrantRuneHintsPacket message, Supplier<NetworkEvent.Context> ctx) {
            // Enqueue the handler work on the main game thread
            ctx.get().enqueueWork(() -> {
                if (message.stack != null && !message.stack.isEmpty()) {
                    ServerPlayer player = ctx.get().getSender();
                    grantHints(player, message.stack);
                }
            });
            
            // Mark the packet as handled so we don't get warning log spam
            ctx.get().setPacketHandled(true);
        }
        
        public static void grantHints(Player player, ItemStack stack) {
            Set<Enchantment> enchants = EnchantmentHelper.getEnchantments(stack).keySet();
            int hintCount = 0;
            
            for (Enchantment enchant : enchants) {
                SimpleResearchKey fullResearch = SimpleResearchKey.parseRuneEnchantment(enchant);
                if (RuneManager.hasRuneDefinition(enchant) && !fullResearch.isKnownByStrict(player)) {
                    CompoundResearchKey requirements = RuneManager.getRuneDefinition(enchant).getRequiredResearch();
                    if (requirements == null || requirements.isKnownByStrict(player)) {
                        List<SimpleResearchKey> candidates = RUNE_TYPES.stream().map(rt -> SimpleResearchKey.parsePartialRuneEnchantment(enchant, rt)).filter(key -> !key.isKnownByStrict(player)).toList();
                        if (candidates.size() == 1) {
                            // If only one hint remains to be given, grant it and the full research as well
                            ResearchManager.completeResearch(player, candidates.get(0));
                            ResearchManager.completeResearch(player, fullResearch);
                            hintCount++;
                        } else if (!candidates.isEmpty()) {
                            // If more than one hint remains to be given, grant one at random
                            WeightedRandomBag<SimpleResearchKey> candidateBag = new WeightedRandomBag<>();
                            for (SimpleResearchKey candidate : candidates) {
                                candidateBag.add(candidate, 1);
                            }
                            ResearchManager.completeResearch(player, candidateBag.getRandom(player.getRandom()));
                            hintCount++;
                        }
                    }
                }
            }
            if (hintCount > 0) {
                // If at least one hint was granted to the player, notify them
                player.displayClientMessage(Component.translatable("event.primalmagick.runic_grindstone.hints_granted").withStyle(ChatFormatting.GREEN), false);
            }
        }
    }
}
