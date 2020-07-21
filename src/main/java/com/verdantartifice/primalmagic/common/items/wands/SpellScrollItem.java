package com.verdantartifice.primalmagic.common.items.wands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.spells.SpellManager;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;
import com.verdantartifice.primalmagic.common.stats.StatsManager;
import com.verdantartifice.primalmagic.common.stats.StatsPM;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * Item definition for a filled spell scroll.  These can be used to cast their held spell directly or
 * to be inscribed onto a wand.
 * 
 * @author Daedalus4096
 */
public class SpellScrollItem extends Item {
    public SpellScrollItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP));
    }
    
    @Nullable
    public SpellPackage getSpell(@Nonnull ItemStack stack) {
        // Get the held spell from the given scroll stack's NBT data
        SpellPackage spell = null;
        if (stack.hasTag() && stack.getTag().contains("Spell")) {
            spell = new SpellPackage(stack.getTag().getCompound("Spell"));
        }
        return spell;
    }
    
    public void setSpell(@Nonnull ItemStack stack, @Nonnull SpellPackage spell) {
        // Save the given spell into the scroll stack's NBT data
        stack.setTagInfo("Spell", spell.serializeNBT());
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        // A scroll's name is determined by that of the spell it holds (e.g. "Scroll of Lightning Bolt")
        SpellPackage spell = this.getSpell(stack);
        ITextComponent spellName = (spell == null) ? new TranslationTextComponent("tooltip.primalmagic.none") : spell.getName();
        return new TranslationTextComponent(this.getTranslationKey(stack), spellName).applyTextStyle(TextFormatting.ITALIC);
    }
    
    @Override
    public Rarity getRarity(ItemStack stack) {
        // A scroll's rarity is determined by that of its held spell
        SpellPackage spell = this.getSpell(stack);
        if (spell == null) {
            return Rarity.COMMON;
        } else {
            return spell.getRarity();
        }
    }
    
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        // Do nothing; don't include filled spell scrolls in the creative tab
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        // Cast the held spell, if any, and consume the scroll
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        if (!worldIn.isRemote) {
            SpellPackage spell = this.getSpell(stack);
            // Check to see if the player's spells are on cooldown
            if (spell != null && !SpellManager.isOnCooldown(playerIn)) {
                SpellManager.setCooldown(playerIn, spell.getCooldownTicks());
                spell.cast(worldIn, playerIn, stack);
                stack.shrink(1);
                return new ActionResult<>(ActionResultType.SUCCESS, stack);
            } else {
                return new ActionResult<>(ActionResultType.FAIL, stack);
            }
        } else {
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
    }
    
    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        // Increment spell crafting statistics
        super.onCreated(stack, worldIn, playerIn);
        SpellPackage spell = this.getSpell(stack);
        if (spell != null) {
            StatsManager.incrementValue(playerIn, StatsPM.SPELLS_CRAFTED, stack.getCount());
            StatsManager.setValueIfMax(playerIn, StatsPM.SPELLS_CRAFTED_MAX_COST, spell.getManaCost().getManaSize());
        }
    }
}
