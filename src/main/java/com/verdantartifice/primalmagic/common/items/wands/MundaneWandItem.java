package com.verdantartifice.primalmagic.common.items.wands;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.MundaneWandISTER;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemRenderProperties;

/**
 * Item definition for a mundane wand.  Unlike modular wands, mundane wands cannot be inscribed with
 * spells.  They don't do much and are primarily meant to start the player on their progression path.
 * 
 * @author Daedalus4096
 */
public class MundaneWandItem extends AbstractWandItem {
    public MundaneWandItem() {
        super(new Item.Properties().tab(PrimalMagic.ITEM_GROUP).stacksTo(1));
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        // With no gem, a mundane wand's mana capacity is low and fixed
        return 2500;
    }
    
    @Override
    public double getBaseCostModifier(ItemStack stack) {
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
    public Component getSpellCapacityText(ItemStack stack) {
        // Mundane wands can't carry spells
        return new TextComponent("0");
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

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            final BlockEntityWithoutLevelRenderer renderer = new MundaneWandISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }
}
