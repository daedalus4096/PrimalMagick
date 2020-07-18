package com.verdantartifice.primalmagic.common.items.wands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * Item definition for a modular wand.  Modular wands are made up of cores, caps, and gems, and their
 * properties are determined by those components.
 * 
 * @author Daedalus4096
 */
public class ModularWandItem extends AbstractWandItem {
    public ModularWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        // The maximum amount of real mana a wand can hold is determined by its gem
        if (stack == null) {
            return 2500;
        }
        WandGem gem = this.getWandGem(stack);
        if (gem == null) {
            return 2500;
        } else if (gem.getCapacity() == -1) {
            return -1;
        } else {
            return 100 * gem.getCapacity();
        }
    }
    
    @Override
    public double getBaseCostModifier(ItemStack stack) {
        // The base mana cost modifier of a wand is determined by its cap
        if (stack == null) {
            return 1.2D;
        }
        WandCap cap = this.getWandCap(stack);
        return (cap == null) ? 1.2D : cap.getBaseCostModifier();
    }
    
    @Override
    public double getTotalCostModifier(ItemStack stack, PlayerEntity player, Source source) {
        double mod = super.getTotalCostModifier(stack, player, source);
        
        // Subtract discount for wand core alignment, if any
        WandCore core = this.getWandCore(stack);
        if (core != null && core.getAlignedSources().contains(source)) {
            mod -= 0.05D;
        }
        
        return mod;
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
        stack.setTagInfo("core", StringNBT.valueOf(core.getTag()));
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
        stack.setTagInfo("cap", StringNBT.valueOf(cap.getTag()));
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
        stack.setTagInfo("gem", StringNBT.valueOf(gem.getTag()));
    }
    
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        // A modular wand's display name is determined by its components (e.g. "Apprentice's Iron-Shod Heartwood Wand")
        WandCore core = this.getWandCore(stack);
        ITextComponent coreName = (core == null) ? new TranslationTextComponent("primalmagic.wand_core.unknown.name") : new TranslationTextComponent(core.getNameTranslationKey());
        WandCap cap = this.getWandCap(stack);
        ITextComponent capName = (cap == null) ? new TranslationTextComponent("primalmagic.wand_cap.unknown.name") : new TranslationTextComponent(cap.getNameTranslationKey());
        WandGem gem = this.getWandGem(stack);
        ITextComponent gemName = (gem == null) ? new TranslationTextComponent("primalmagic.wand_gem.unknown.name") : new TranslationTextComponent(gem.getNameTranslationKey());
        return new TranslationTextComponent("item.primalmagic.modular_wand", gemName, capName, coreName);
    }
    
    @Override
    public Rarity getRarity(ItemStack stack) {
        // A modular wand's rarity is the highest of those of its components
        Rarity retVal = Rarity.COMMON;
        WandCore core = this.getWandCore(stack);
        if (core != null && core.getRarity().compareTo(retVal) > 0) {
            retVal = core.getRarity();
        }
        WandCap cap = this.getWandCap(stack);
        if (cap != null && cap.getRarity().compareTo(retVal) > 0) {
            retVal = cap.getRarity();
        }
        WandGem gem = this.getWandGem(stack);
        if (gem != null && gem.getRarity().compareTo(retVal) > 0) {
            retVal = gem.getRarity();
        }
        return retVal;
    }
    
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        // Populate the creative pane with a NBT-complete modular wand(s)
        if (this.isInGroup(group)) {
            ItemStack stack = new ItemStack(this);
            this.setWandCore(stack, WandCore.HEARTWOOD);
            this.setWandCap(stack, WandCap.IRON);
            this.setWandGem(stack, WandGem.APPRENTICE);
            items.add(stack);
        }
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        
        // Regenerate one mana per second for core-aligned sources
        if (stack != null && entityIn.ticksExisted % 20 == 0) {
            int maxMana = this.getMaxMana(stack);
            WandCore core = this.getWandCore(stack);
            if (core != null && maxMana != -1) {
                for (Source alignedSource : core.getAlignedSources()) {
                    int curMana = this.getMana(stack, alignedSource);
                    if (curMana < (0.1D * maxMana)) {
                        this.addRealMana(stack, alignedSource, 1);
                    }
                }
            }
        }
    }

    @Override
    public List<SpellPackage> getSpells(ItemStack stack) {
        // Deserialize the list of inscribed spells from the given wand stack's NBT data
        List<SpellPackage> retVal = new ArrayList<>();
        if (stack != null) {
            ListNBT spellTagsList = stack.getTag().getList("Spells", Constants.NBT.TAG_COMPOUND);
            for (int index = 0; index < spellTagsList.size(); index++) {
                CompoundNBT spellTag = spellTagsList.getCompound(index);
                SpellPackage newSpell = new SpellPackage(spellTag);
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
            return stack.getTag().getList("Spells", Constants.NBT.TAG_COMPOUND).size();
        } else {
            return 0;
        }
    }
    
    @Override
    public ITextComponent getSpellCapacityText(ItemStack stack) {
        if (stack == null) {
            return new StringTextComponent("0");
        } else {
            WandCore core = this.getWandCore(stack);
            if (core == null) {
                return new StringTextComponent("0");
            } else {
                int baseSlots = core.getSpellSlots();
                Source bonusSlot = core.getBonusSlot();
                if (bonusSlot == null) {
                    return new StringTextComponent(Integer.toString(baseSlots));
                } else {
                    ITextComponent bonusText = new TranslationTextComponent(bonusSlot.getNameTranslationKey());
                    return new StringTextComponent(String.format("%1$d + 1 %2$s", baseSlots, bonusText.getFormattedText()));
                }
            }
        }
    }

    @Override
    public int getActiveSpellIndex(ItemStack stack) {
        // Return -1 if no active spell is selected
        return (stack != null && stack.getTag().contains("ActiveSpell")) ? stack.getTag().getInt("ActiveSpell") : -1;
    }
    
    @Override
    public SpellPackage getActiveSpell(ItemStack stack) {
        // Deserialize the active inscribed spell from the given wand stack's NBT data
        SpellPackage retVal = null;
        if (stack != null) {
            ListNBT spellTagsList = stack.getTag().getList("Spells", Constants.NBT.TAG_COMPOUND);
            int index = this.getActiveSpellIndex(stack);
            if (index >= 0 && index < spellTagsList.size()) {
                CompoundNBT spellTag = spellTagsList.getCompound(index);
                retVal = new SpellPackage(spellTag);
            }
        }
        return retVal;
    }

    @Override
    public boolean setActiveSpellIndex(ItemStack stack, int index) {
        if (stack == null) {
            return false;
        } else if (index >= -1 && index < this.getSpells(stack).size()) {
            // -1 is a valid value and means "no active spell"
            stack.setTagInfo("ActiveSpell", IntNBT.valueOf(index));
            return true;
        }
        return false;
    }

    @Override
    public boolean canAddSpell(ItemStack stack, SpellPackage spell) {
        if (stack == null || spell == null || spell.getPayload() == null) {
            return false;
        }
        
        // The number of spells which can be inscribed onto a wand is determined by its core
        WandCore core = this.getWandCore(stack);
        if (core == null) {
            return false;
        }
        
        // Determine the payload sources of all spells to be included in the given wand
        List<Source> spellSources = this.getSpells(stack).stream()
                .filter(p -> (p != null && p.getPayload() != null))
                .map(p -> p.getPayload().getSource())
                .collect(Collectors.toCollection(() -> new ArrayList<>()));
        spellSources.add(spell.getPayload().getSource());
        
        int coreSlots = core.getSpellSlots();
        if (spellSources.size() < coreSlots + 1) {
            // If the spells would fit in the base slots without the bonus, then it's fine
            return true;
        } else if (spellSources.size() > coreSlots + 1) {
            // If the bonus slot wouldn't be enough to make them fit, then reject
            return false;
        } else {
            // If using exactly the slot count plus bonus slot, only allow if one of the spells is of the same source as the bonus
            return core.getBonusSlot() != null && spellSources.contains(core.getBonusSlot());
        }
    }

    @Override
    public boolean addSpell(ItemStack stack, SpellPackage spell) {
        if (this.canAddSpell(stack, spell)) {
            // Save the given spell into the wand stack's NBT data
            if (!stack.getTag().contains("Spells")) {
                ListNBT newList = new ListNBT();
                newList.add(spell.serializeNBT());
                stack.setTagInfo("Spells", newList);
                return true;
            } else {
                return stack.getTag().getList("Spells", Constants.NBT.TAG_COMPOUND).add(spell.serializeNBT());
            }
        } else {
            return false;
        }
    }
}
