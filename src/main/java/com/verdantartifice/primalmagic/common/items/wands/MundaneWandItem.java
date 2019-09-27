package com.verdantartifice.primalmagic.common.items.wands;

import java.util.List;

import javax.annotation.Nonnull;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.items.base.ItemPM;
import com.verdantartifice.primalmagic.common.sources.Source;
import com.verdantartifice.primalmagic.common.sources.SourceList;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MundaneWandItem extends ItemPM implements IWand {
    public MundaneWandItem() {
        super("mundane_wand", new Item.Properties().group(PrimalMagic.ITEM_GROUP).maxStackSize(1));
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (worldIn.isRemote) {
            PrimalMagic.LOGGER.info("Equipped wand mana:");
            SourceList mana = this.getAllMana(playerIn.getHeldItem(handIn));
            for (Source source : mana.getSources()) {
                ITextComponent nameComp = new TranslationTextComponent(source.getNameTranslationKey());
                PrimalMagic.LOGGER.info(nameComp.getString() + ": " + mana.getAmount(source));
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public int getMana(ItemStack stack, Source source) {
        int retVal = 0;
        if (stack != null && source != null && stack.hasTag() && stack.getTag().contains(source.getTag())) {
            retVal = stack.getTag().getInt(source.getTag());
        }
        return retVal;
    }

    @Override
    public SourceList getAllMana(ItemStack stack) {
        SourceList retVal = new SourceList();
        for (Source source : Source.SORTED_SOURCES) {
            if (stack.hasTag() && stack.getTag().contains(source.getTag())) {
                retVal.merge(source, stack.getTag().getInt(source.getTag()));
            } else {
                retVal.merge(source, 0);
            }
        }
        return retVal;
    }

    @Override
    public int getMaxMana(ItemStack stack) {
        return 25;
    }
    
    protected void setMana(@Nonnull ItemStack stack, @Nonnull Source source, int amount) {
        stack.setTagInfo(source.getTag(), new IntNBT(amount));
    }

    @Override
    public int addMana(ItemStack stack, Source source, int amount) {
        if (stack == null || source == null) {
            return 0;
        }
        int toStore = this.getMana(stack, source) + amount;
        int leftover = Math.max(this.getMaxMana(stack) - toStore, 0);
        this.setMana(stack, source, Math.min(toStore, this.getMaxMana(stack)));
        return leftover;
    }

    @Override
    public boolean consumeMana(ItemStack stack, PlayerEntity player, Source source, int amount) {
        if (stack == null || player == null || source == null) {
            return false;
        }
        if (this.getMana(stack, source) >= amount) {
            this.setMana(stack, source, this.getMana(stack, source) - amount);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (PrimalMagic.proxy.isShiftDown()) {
            for (Source source : Source.SORTED_SOURCES) {
                ITextComponent nameComp = new TranslationTextComponent(source.getNameTranslationKey()).applyTextStyle(source.getChatColor());
                ITextComponent line = new TranslationTextComponent("primalmagic.source.mana_tooltip", nameComp.getFormattedText(), this.getMana(stack, source), this.getMaxMana(stack));
                tooltip.add(line);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (Source source : Source.SORTED_SOURCES) {
                if (!first) {
                    sb.append("/");
                }
                int mana = this.getMana(stack, source);
                ITextComponent manaStr = new StringTextComponent(Integer.toString(mana)).applyTextStyle(source.getChatColor());
                sb.append(manaStr.getFormattedText());
                first = false;
            }
            tooltip.add(new StringTextComponent(sb.toString()));
        }
    }
}
