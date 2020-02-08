package com.verdantartifice.primalmagic.common.items.wands;

import java.util.Collections;
import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.MundaneWandTEISR;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Item definition for a mundane wand.  Unlike modular wands, mundane wands cannot be inscribed with
 * spells.  They don't do much and are primarily meant to start the player on their progression path.
 * 
 * @author Daedalus4096
 */
public class MundaneWandItem extends AbstractWandItem {
    public MundaneWandItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).setISTER(() -> MundaneWandTEISR::new));
        this.setRegistryName(PrimalMagic.MODID, "mundane_wand");
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        // With no gem, a mundane wand's mana capacity is low and fixed
        return 2500;
    }
    
    @Override
    public float getBaseCostModifier(ItemStack stack) {
        // With no cap, a mundane wand gets a 20% penalty to all mana expenditures
        return 1.2F;
    }

    @Override
    public List<SpellPackage> getSpells(ItemStack stack) {
        // Mundane wands can't carry spells
        return Collections.emptyList();
    }
    
    @Override
    public int getSpellCount(ItemStack stack) {
        // Mundane wands can't carry spells
        return 0;
    }

    @Override
    public int getActiveSpellIndex(ItemStack stack) {
        // Mundane wands can't carry spells
        return -1;
    }
    
    @Override
    public SpellPackage getActiveSpell(ItemStack stack) {
        // Mundane wands can't carry spells
        return null;
    }

    @Override
    public boolean setActiveSpellIndex(ItemStack stack, int index) {
        // Mundane wands can't carry spells
        return false;
    }
    
    @Override
    public boolean canAddSpell(ItemStack stack, SpellPackage spell) {
        // Mundane wands can't carry spells
        return false;
    }

    @Override
    public boolean addSpell(ItemStack stack, SpellPackage spell) {
        // Mundane wands can't carry spells
        return false;
    }
}
