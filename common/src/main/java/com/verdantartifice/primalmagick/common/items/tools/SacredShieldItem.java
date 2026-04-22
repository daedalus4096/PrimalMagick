package com.verdantartifice.primalmagick.common.items.tools;

import com.google.common.collect.ImmutableMap;
import com.verdantartifice.primalmagick.common.enchantments.EnchantmentsPM;
import com.verdantartifice.primalmagick.common.items.IEnchantedByDefault;
import com.verdantartifice.primalmagick.common.util.ResourceUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Definition of a shield that comes pre-enchanted with Bulwark.
 * 
 * @author Daedalus4096
 */
public class SacredShieldItem extends AbstractTieredShieldItem implements IEnchantedByDefault {
    public static final Identifier TEXTURE = ResourceUtils.loc("entity/shield/sacred_shield");
    
    public SacredShieldItem(Item.Properties properties) {
        super(ToolMaterialsPM.HALLOWSTEEL, properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext level, @NotNull TooltipDisplay tooltipDisplay, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag flag) {
        // Do nothing; we don't support banner patterns
    }

    @Override
    public boolean canDecorate() {
        // Does not support banner patterns
        return false;
    }

    @Override
    public Map<ResourceKey<Enchantment>, Integer> getDefaultEnchantments() {
        return ImmutableMap.of(EnchantmentsPM.BULWARK, 2);
    }
}
