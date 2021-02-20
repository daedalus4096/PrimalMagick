package com.verdantartifice.primalmagic.common.items.misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagic.PrimalMagic;
import com.verdantartifice.primalmagic.common.runes.Rune;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Item definition for a rune.  May be used in combinations to enchant items.
 * 
 * @author Daedalus4096
 */
public class RuneItem extends Item {
    protected static final Map<Rune, Item> RUNES = new HashMap<>();
    
    protected final Rune rune;
    
    public RuneItem(@Nonnull Rune rune) {
        super(new Item.Properties().group(PrimalMagic.ITEM_GROUP).rarity(rune.getRarity()));
        this.rune = rune;
        register(rune, this);
    }
    
    public Rune getRune() {
        return this.rune;
    }
    
    @Override
    public boolean hasEffect(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof RuneItem) {
            return ((RuneItem)stack.getItem()).rune.hasGlint();
        } else {
            return super.hasEffect(stack);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack != null && stack.getItem() instanceof RuneItem) {
            String key = ((RuneItem)stack.getItem()).rune.getTooltipTranslationKey();
            tooltip.add(new TranslationTextComponent(key).mergeStyle(TextFormatting.ITALIC, TextFormatting.GRAY));
        }
    }
    
    protected static void register(@Nonnull Rune rune, @Nonnull Item item) {
        RUNES.put(rune, item);
    }
    
    @Nonnull
    public static ItemStack getRune(@Nullable Rune rune) {
        return getRune(rune, 1);
    }
    
    @Nonnull
    public static ItemStack getRune(@Nullable Rune rune, int count) {
        Item item = RUNES.get(rune);
        return (item == null) ? ItemStack.EMPTY : new ItemStack(item, count);
    }
}
