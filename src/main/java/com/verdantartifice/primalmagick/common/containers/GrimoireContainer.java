package com.verdantartifice.primalmagick.common.containers;

import com.verdantartifice.primalmagick.common.research.topics.AbstractResearchTopic;
import com.verdantartifice.primalmagick.common.research.topics.MainIndexResearchTopic;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Server data container for the grimoire GUI.
 * 
 * @author Daedalus4096
 */
public class GrimoireContainer extends AbstractContainerMenu {
    protected AbstractResearchTopic topic;
    
    public GrimoireContainer(int windowId, AbstractResearchTopic topic) {
        super(ContainersPM.GRIMOIRE.get(), windowId);
        this.topic = topic == null ? MainIndexResearchTopic.INSTANCE : topic;
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
}
