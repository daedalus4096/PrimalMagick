package com.verdantartifice.primalmagick.common.items.wands;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.verdantartifice.primalmagick.client.renderers.itemstack.MundaneWandISTER;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * Item definition for a mundane wand.  Unlike modular wands, mundane wands cannot be inscribed with
 * spells.  They don't do much and are primarily meant to start the player on their progression path.
 * 
 * @author Daedalus4096
 */
public class MundaneWandItem extends AbstractWandItem {
    public MundaneWandItem() {
        super(new Item.Properties().stacksTo(1));
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
    public int getSiphonAmount(ItemStack stack) {
        // With no cap, a mundane wand siphons the minimum amount
        return 1;
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
        return Component.literal("0");
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
    public void clearSpells(ItemStack stack) {
        // Mundane wands can't carry spells
    }

    @Override
    public boolean isGlamoured(ItemStack stack) {
        // Mundane wands can't have glamours applied
        return false;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            final BlockEntityWithoutLevelRenderer renderer = new MundaneWandISTER();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }
}
