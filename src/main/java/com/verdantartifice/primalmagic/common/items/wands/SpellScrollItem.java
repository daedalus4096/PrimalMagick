package com.verdantartifice.primalmagic.common.items.wands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.spells.SpellPackage;

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

public class SpellScrollItem extends Item {
    public SpellScrollItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(Rarity.UNCOMMON));
        this.setRegistryName(PrimalMagic.MODID, "spell_scroll_filled");
    }
    
    @Nullable
    public SpellPackage getSpell(@Nonnull ItemStack stack) {
        SpellPackage spell = null;
        if (stack.hasTag() && stack.getTag().contains("Spell")) {
            spell = new SpellPackage(stack.getTag().getCompound("Spell"));
        }
        return spell;
    }
    
    public void setSpell(@Nonnull ItemStack stack, @Nonnull SpellPackage spell) {
        stack.setTagInfo("Spell", spell.serializeNBT());
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        SpellPackage spell = this.getSpell(stack);
        String spellName = (spell == null) ? "none" : spell.getName();
        return new TranslationTextComponent(this.getTranslationKey(stack), spellName).applyTextStyle(TextFormatting.ITALIC);
    }
    
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        // Do nothing; don't include filled spell scrolls in the creative tab
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        if (!worldIn.isRemote) {
            SpellPackage spell = this.getSpell(stack);
            if (spell != null) {
                spell.cast(worldIn, playerIn);
                stack.shrink(1);
                return new ActionResult<>(ActionResultType.SUCCESS, stack);
            } else {
                return new ActionResult<>(ActionResultType.FAIL, stack);
            }
        } else {
            return new ActionResult<>(ActionResultType.SUCCESS, stack);
        }
    }
}
