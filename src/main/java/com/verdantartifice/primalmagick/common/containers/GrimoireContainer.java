package com.verdantartifice.primalmagick.common.containers;

import java.util.LinkedList;
import java.util.List;

import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.MainIndexResearchTopic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

/**
 * Server data container for the grimoire GUI.
 * 
 * @author Daedalus4096
 */
public class GrimoireContainer extends AbstractContainerMenu {
    protected final LinkedList<AbstractResearchTopic> history = new LinkedList<>();

    protected AbstractResearchTopic topic;
    
    public GrimoireContainer(int windowId, AbstractResearchTopic topic, List<AbstractResearchTopic> history) {
        super(ContainersPM.GRIMOIRE.get(), windowId);
        this.topic = topic == null ? MainIndexResearchTopic.INSTANCE : topic;
        this.history.addAll(history);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }
    
    public AbstractResearchTopic getTopic() {
        return this.topic;
    }
    
    public void setTopic(AbstractResearchTopic newTopic) {
        this.topic = newTopic;
    }
    
    public LinkedList<AbstractResearchTopic> getTopicHistory() {
        return this.history;
    }
    
    public void setTopicHistory(List<AbstractResearchTopic> history) {
        this.history.clear();
        this.history.addAll(history);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
