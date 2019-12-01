package com.verdantartifice.primalmagic.common.items.wands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.client.renderers.itemstack.ModularWandTEISR;
import com.verdantartifice.primalmagic.common.spells.ISpellPackage;
import com.verdantartifice.primalmagic.common.spells.SpellFactory;
import com.verdantartifice.primalmagic.common.spells.TouchSpellPackage;
import com.verdantartifice.primalmagic.common.spells.payloads.EarthDamageSpellPayload;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ModularWandItem extends AbstractWandItem {
    public ModularWandItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1).setTEISR(() -> ModularWandTEISR::new));
        this.setRegistryName(PrimalMagic.MODID, "modular_wand");
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        WandGem gem = this.getWandGem(stack);
        return (gem == null) ? 25 : gem.getCapacity();
    }

    @Nullable
    public WandCore getWandCore(@Nonnull ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("core")) {
            return WandCore.getWandCore(stack.getTag().getString("core"));
        } else {
            return null;
        }
    }
    
    public void setWandCore(@Nonnull ItemStack stack, @Nonnull WandCore core) {
        stack.setTagInfo("core", new StringNBT(core.getTag()));
    }
    
    @Nullable 
    public WandCap getWandCap(@Nonnull ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("cap")) {
            return WandCap.getWandCap(stack.getTag().getString("cap"));
        } else {
            return null;
        }
    }
    
    public void setWandCap(@Nonnull ItemStack stack, @Nonnull WandCap cap) {
        stack.setTagInfo("cap", new StringNBT(cap.getTag()));
    }
    
    @Nullable
    public WandGem getWandGem(@Nonnull ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("gem")) {
            return WandGem.getWandGem(stack.getTag().getString("gem"));
        } else {
            return null;
        }
    }
    
    public void setWandGem(@Nonnull ItemStack stack, @Nonnull WandGem gem) {
        stack.setTagInfo("gem", new StringNBT(gem.getTag()));
    }
    
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        WandCore core = this.getWandCore(stack);
        ITextComponent coreName = (core == null) ? new StringTextComponent("Unknown") : new TranslationTextComponent(core.getNameTranslationKey());
        WandCap cap = this.getWandCap(stack);
        ITextComponent capName = (cap == null) ? new StringTextComponent("Unknown") : new TranslationTextComponent(cap.getNameTranslationKey());
        WandGem gem = this.getWandGem(stack);
        ITextComponent gemName = (gem == null) ? new StringTextComponent("Unknown's") : new TranslationTextComponent(gem.getNameTranslationKey());
        return new TranslationTextComponent("item.primalmagic.modular_wand", gemName, capName, coreName);
    }
    
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            ItemStack stack = new ItemStack(this);
            this.setWandCore(stack, WandCore.HEARTWOOD);
            this.setWandCap(stack, WandCap.IRON);
            this.setWandGem(stack, WandGem.APPRENTICE);
            
            // TODO Remove after testing is complete
            ISpellPackage spell = new TouchSpellPackage("Test Spell");
            spell.setPayload(new EarthDamageSpellPayload(5));
            this.addSpell(stack, spell);
            this.setActiveSpellIndex(stack, 0);
            
            items.add(stack);
        }
    }

    @Override
    public List<ISpellPackage> getSpells(ItemStack stack) {
        List<ISpellPackage> retVal = new ArrayList<>();
        if (stack != null) {
            ListNBT spellTagsList = stack.getTag().getList("Spells", 10);
            for (int index = 0; index < spellTagsList.size(); index++) {
                CompoundNBT spellTag = spellTagsList.getCompound(index);
                ISpellPackage newSpell = SpellFactory.getPackageFromNBT(spellTag);
                if (newSpell != null) {
                    retVal.add(newSpell);
                }
            }
        }
        return retVal;
    }
    
    @Override
    public int getSpellCount(ItemStack stack) {
        if (stack != null) {
            return stack.getTag().getList("Spells", 10).size();
        } else {
            return 0;
        }
    }

    @Override
    public int getActiveSpellIndex(ItemStack stack) {
        return (stack != null && stack.getTag().contains("ActiveSpell")) ? stack.getTag().getInt("ActiveSpell") : -1;
    }
    
    @Override
    public ISpellPackage getActiveSpell(ItemStack stack) {
        ISpellPackage retVal = null;
        if (stack != null) {
            ListNBT spellTagsList = stack.getTag().getList("Spells", 10);
            int index = this.getActiveSpellIndex(stack);
            if (index >= 0 && index < spellTagsList.size()) {
                CompoundNBT spellTag = spellTagsList.getCompound(index);
                retVal = SpellFactory.getPackageFromNBT(spellTag);
            }
        }
        return retVal;
    }

    @Override
    public boolean setActiveSpellIndex(ItemStack stack, int index) {
        if (stack == null) {
            return false;
        } else if (index >= -1 && index < this.getSpells(stack).size()) {
            stack.setTagInfo("ActiveSpell", new IntNBT(index));
            return true;
        }
        return false;
    }

    @Override
    public boolean addSpell(ItemStack stack, ISpellPackage spell) {
        if (stack == null || spell == null) {
            return false;
        }
        
        WandCore core = this.getWandCore(stack);
        if (core == null) {
            return false;
        }
        
        List<ISpellPackage> existingSpells = this.getSpells(stack);
        if (existingSpells.size() >= core.getSpellSlots()) {
            return false;
        }
        
        if (!stack.getTag().contains("Spells")) {
            ListNBT newList = new ListNBT();
            newList.add(spell.serializeNBT());
            stack.setTagInfo("Spells", newList);
            return true;
        } else {
            return stack.getTag().getList("Spells", 10).add(spell.serializeNBT());
        }
    }
}
