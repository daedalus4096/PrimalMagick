package com.verdantartifice.primalmagic.common.items.wands;

import java.util.Collections;
import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.MundaneWandTEISR;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

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
    public List<SpellPackage> getSpells(ItemStack stack) {
        // Mundane wands can't carry spells
        return Collections.emptyList();
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
    public boolean addSpell(ItemStack stack, SpellPackage spell) {
        // Mundane wands can't carry spells
        return false;
    }
}
