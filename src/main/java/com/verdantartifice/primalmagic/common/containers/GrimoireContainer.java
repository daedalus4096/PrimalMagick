package com.verdantartifice.primalmagic.common.containers;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.client.gui.grimoire.pages.StatisticsPage;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

/**
 * Server data container for the grimoire GUI.
 * 
 * @author Daedalus4096
 */
public class GrimoireContainer extends Container {
    protected Object topic;
    
    public GrimoireContainer(int windowId) {
        super(ContainersPM.GRIMOIRE, windowId);
        this.topic = null;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
    
    public Object getTopic() {
        return this.topic;
    }
    
    /**
     * New topic can either be null (for the discipline index), a ResearchDiscipline (for a listing
     * of that discipline's entries), a ResearchEntry (for details of that entry), or specific strings
     * (for various other topics).
     * 
     * @param newTopic
     */
    @Nullable
    public void setTopic(Object newTopic) {
        if ( newTopic == null || 
             newTopic instanceof ResearchDiscipline || 
             newTopic instanceof ResearchEntry || 
             StatisticsPage.TOPIC.equals(newTopic) ) {
            this.topic = newTopic;
        } else {
            throw new IllegalArgumentException("Invalid grimoire topic");
        }
    }
}
