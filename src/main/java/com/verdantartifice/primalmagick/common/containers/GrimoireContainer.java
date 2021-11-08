package com.verdantartifice.primalmagick.common.containers;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagick.common.research.ResearchEntry;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Server data container for the grimoire GUI.
 * 
 * @author Daedalus4096
 */
public class GrimoireContainer extends AbstractContainerMenu {
    protected Object topic;
    
    public GrimoireContainer(int windowId) {
        super(ContainersPM.GRIMOIRE.get(), windowId);
        this.topic = null;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }
    
    public Object getTopic() {
        return this.topic;
    }
    
    /**
     * New topic can either be null (for the discipline index), a ResearchDiscipline (for a listing
     * of that discipline's entries), a ResearchEntry (for details of that entry), a Source (for
     * attunement details for that source), an Enchantment (for details about a rune enchantment), 
     * or strings (for recipes or various other topics).
     * 
     * @param newTopic
     */
    @Nullable
    public void setTopic(Object newTopic) {
        if ( newTopic == null || 
             newTopic instanceof ResearchDiscipline || 
             newTopic instanceof ResearchEntry || 
             newTopic instanceof Source || 
             newTopic instanceof Enchantment ||
             newTopic instanceof String ) {
            this.topic = newTopic;
        } else {
            throw new IllegalArgumentException("Invalid grimoire topic");
        }
    }
}
