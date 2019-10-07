package com.verdantartifice.primalmagic.common.items.wands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.wands.WandCap;
import com.verdantartifice.primalmagic.common.wands.WandCore;
import com.verdantartifice.primalmagic.common.wands.WandGem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ModularWandItem extends AbstractWandItem {
    public ModularWandItem() {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1)); // TODO set TEISR
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
            items.add(stack);
        }
    }
}
