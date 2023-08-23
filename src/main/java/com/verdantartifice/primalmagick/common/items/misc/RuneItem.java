package com.verdantartifice.primalmagick.common.items.misc;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.runes.Rune;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Item definition for a rune.  May be used in combinations to enchant items.
 * 
 * @author Daedalus4096
 */
public class RuneItem extends Item {
    protected static final Map<Rune, Item> RUNES = new HashMap<>();
    
    protected final Rune rune;
    
    public RuneItem(@Nonnull Rune rune) {
        super(new Item.Properties().rarity(rune.getRarity()));
        this.rune = rune;
        register(rune, this);
    }
    
    public Rune getRune() {
        return this.rune;
    }
    
    @Override
    public boolean isFoil(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof RuneItem) {
            return ((RuneItem)stack.getItem()).rune.hasGlint();
        } else {
            return super.isFoil(stack);
        }
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack != null && stack.getItem() instanceof RuneItem runeItem) {
            String key = String.join(".", "item", PrimalMagick.MODID, ForgeRegistries.ITEMS.getKey(this).getPath(), "tooltip");
            tooltip.add(Component.translatable(key).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
            
            if (runeItem.rune.hasLimit()) {
                tooltip.add(Component.translatable("tooltip.primalmagick.rune_limit", runeItem.rune.getLimit()));
            }
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
    
    public static Collection<Item> getAllRunes() {
        return Collections.unmodifiableCollection(RUNES.values());
    }
}
