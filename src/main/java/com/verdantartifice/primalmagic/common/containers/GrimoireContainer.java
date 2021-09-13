package com.verdantartifice.primalmagic.common.containers;

import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.client.gui.grimoire.AttunementIndexPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.RecipeIndexPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.RuneEnchantmentIndexPage;
import com.verdantartifice.primalmagic.client.gui.grimoire.StatisticsPage;
import com.verdantartifice.primalmagic.common.research.ResearchDiscipline;
import com.verdantartifice.primalmagic.common.research.ResearchEntry;
import com.verdantartifice.primalmagic.common.sources.Source;

import net.minecraft.resources.ResourceLocation;
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
     * a ResourceLocation (for a recipe), or specific strings (for various other topics).
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
             newTopic instanceof ResourceLocation || 
             AttunementIndexPage.TOPIC.equals(newTopic) || 
             RuneEnchantmentIndexPage.TOPIC.equals(newTopic) || 
             RecipeIndexPage.TOPIC.equals(newTopic) || 
             StatisticsPage.TOPIC.equals(newTopic) ) {
            this.topic = newTopic;
        } else {
            throw new IllegalArgumentException("Invalid grimoire topic");
        }
    }
}
