package com.verdantartifice.primalmagic.common.items.misc;

import java.util.List;

import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * Item definition for a dream vision talisman.  These items store experience picked up by the player,
 * converting them to observations when the player sleeps.
 * 
 * @author Daedalus4096
 */
public class DreamVisionTalismanItem extends Item {
    protected static final int CAPACITY = 64;
    
    public DreamVisionTalismanItem() {
        super(new Item.Properties().tab(PrimalMagic.ITEM_GROUP).durability(63));
    }
    
    public int getStoredExp(ItemStack stack) {
        return stack.getOrCreateTag().getInt("StoredExp");
    }
    
    public int getExpCapacity() {
        return CAPACITY;
    }
    
    public boolean isActive(ItemStack stack) {
        return !stack.getOrCreateTag().getBoolean("Disabled");
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("tooltip.primalmagic.dream_vision_talisman.exp", this.getStoredExp(stack), this.getExpCapacity()));
        if (this.isActive(stack)) {
            tooltip.add(new TranslatableComponent("tooltip.primalmagic.dream_vision_talisman.active").withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(new TranslatableComponent("tooltip.primalmagic.dream_vision_talisman.inactive").withStyle(ChatFormatting.RED));
        }
    }
}
