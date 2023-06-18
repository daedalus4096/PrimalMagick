package com.verdantartifice.primalmagick.common.items.misc;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.capabilities.IPlayerKnowledge;
import com.verdantartifice.primalmagick.common.capabilities.PrimalMagickCapabilities;
import com.verdantartifice.primalmagick.common.containers.GrimoireContainer;
import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.MainIndexResearchTopic;
import com.verdantartifice.primalmagick.common.stats.StatsManager;
import com.verdantartifice.primalmagick.common.stats.StatsPM;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

/**
 * Item defintion for a grimoire.  The grimoire serves as a research browser and is the primary mechanism of
 * progression in the mod.
 * 
 * @author Daedalus4096
 */
public class GrimoireItem extends Item implements MenuProvider {
    public GrimoireItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        // Open the grimoire GUI on right click
        if (!worldIn.isClientSide && playerIn instanceof ServerPlayer serverPlayer) {
            IPlayerKnowledge knowledge = PrimalMagickCapabilities.getKnowledge(playerIn).orElse(null);
            AbstractResearchTopic lastTopic = knowledge == null || knowledge.getLastResearchTopic() == null ? MainIndexResearchTopic.INSTANCE : knowledge.getLastResearchTopic();
            List<AbstractResearchTopic> topicHistory = knowledge == null ? ImmutableList.of() : knowledge.getResearchTopicHistory();
            StatsManager.incrementValue(playerIn, StatsPM.GRIMOIRE_READ);
            NetworkHooks.openScreen(serverPlayer, this, buf -> {
                lastTopic.encode(buf);
                buf.writeVarInt(topicHistory.size());
                for (int index = 0; index < topicHistory.size(); index++) {
                    topicHistory.get(index).encode(buf);
                }
            });
        }
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
        return new GrimoireContainer(windowId, MainIndexResearchTopic.INSTANCE, ImmutableList.of());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(this.getDescriptionId());
    }
}
