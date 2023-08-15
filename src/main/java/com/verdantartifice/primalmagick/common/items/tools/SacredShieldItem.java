package com.verdantartifice.primalmagick.common.items.tools;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.PrimalMagick;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

/**
 * Definition of a shield that comes pre-enchanted with Bulwark.
 * 
 * @author Daedalus4096
 */
public class SacredShieldItem extends AbstractTieredShieldItem {
    public static final ResourceLocation TEXTURE = PrimalMagick.resource("entity/shield/sacred_shield");
    
    public SacredShieldItem(Item.Properties properties) {
        super(ItemTierPM.HALLOWSTEEL, properties);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        // Don't use the version defined in ShieldItem; we don't support coloring
        return this.getDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        // Do nothing; we don't support banner patterns
    }

    @Override
    public boolean canDecorate() {
        // Does not support banner patterns
        return false;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(ImmutableMap.of(EnchantmentsPM.BULWARK.get(), 2), stack);
        return stack;
    }
}
