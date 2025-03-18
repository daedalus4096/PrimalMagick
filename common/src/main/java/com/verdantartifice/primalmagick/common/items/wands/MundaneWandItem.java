package com.verdantartifice.primalmagick.common.items.wands;

import com.verdantartifice.primalmagick.client.renderers.itemstack.MundaneWandISTER;
import com.verdantartifice.primalmagick.common.capabilities.ManaStorage;
import com.verdantartifice.primalmagick.common.components.DataComponentsPM;
import com.verdantartifice.primalmagick.common.spells.SpellPackage;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Item definition for a mundane wand.  Unlike modular wands, mundane wands cannot be inscribed with
 * spells.  They don't do much and are primarily meant to start the player on their progression path.
 * 
 * @author Daedalus4096
 */
public abstract class MundaneWandItem extends AbstractWandItem {
    public static final int MAX_MANA = 2500;

    private BlockEntityWithoutLevelRenderer customRenderer;

    public MundaneWandItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack retVal = super.getDefaultInstance();
        retVal.set(DataComponentsPM.CAPABILITY_MANA_STORAGE.get(), ManaStorage.emptyWand(MAX_MANA));
        return retVal;
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        // With no gem, a mundane wand's mana capacity is low and fixed
        return MAX_MANA;
    }
    
    @Override
    public int getBaseCostModifier(ItemStack stack) {
        // With no cap, a mundane wand gets no bonus to mana effectiveness
        return 0;
    }

    @Override
    public int getSiphonAmount(ItemStack stack) {
        // With no cap, a mundane wand siphons the minimum amount
        return 100;
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
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplier() {
        if (this.customRenderer == null) {
            this.customRenderer = this.getCustomRendererSupplierUncached().get();
        }
        return () -> this.customRenderer;
    }

    @Override
    public Supplier<BlockEntityWithoutLevelRenderer> getCustomRendererSupplierUncached() {
        return MundaneWandISTER::new;
    }
}
