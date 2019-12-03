package com.verdantartifice.primalmagic.common.items.wands;

import java.util.Collections;
import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.MundaneWandTEISR;
import com.verdantartifice.primalmagic.common.spells.packages.ISpellPackage;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MundaneWandItem extends AbstractWandItem {
    public MundaneWandItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).setTEISR(() -> MundaneWandTEISR::new));
        this.setRegistryName(PrimalMagic.MODID, "mundane_wand");
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        return 25;
    }

    @Override
    public List<ISpellPackage> getSpells(ItemStack stack) {
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
    public ISpellPackage getActiveSpell(ItemStack stack) {
        // Mundane wands can't carry spells
        return null;
    }

    @Override
    public boolean setActiveSpellIndex(ItemStack stack, int index) {
        // Mundane wands can't carry spells
        return false;
    }
    
    @Override
    public boolean canAddSpell(ItemStack stack, ISpellPackage spell) {
        // Mundane wands can't carry spells
        return false;
    }

    @Override
    public boolean addSpell(ItemStack stack, ISpellPackage spell) {
        // Mundane wands can't carry spells
        return false;
    }
}
