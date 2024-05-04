package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Requirement that the player possesses a stack(s) of the given item at least as large as the
 * given stack, with the same tags.  The item is intended for consumption as part of the check.
 * 
 * @author Daedalus4096
 */
public class ItemStackRequirement extends AbstractRequirement {
    public static final Codec<ItemStackRequirement> CODEC = ItemStack.CODEC.fieldOf("stack").xmap(ItemStackRequirement::new, req -> req.stack).codec();
    
    protected final ItemStack stack;
    
    public ItemStackRequirement(ItemStack stack) {
        this.stack = stack.copy();
    }

    @Override
    public boolean isMetBy(Player player) {
        return player != null && InventoryUtils.isPlayerCarrying(player, this.stack);
    }

    @Override
    public void consumeComponents(Player player) {
        if (player != null && this.isMetBy(player)) {
            InventoryUtils.consumeItem(player, this.stack);
        }
    }

    @Override
    protected RequirementType<?> getType() {
        return RequirementsPM.ITEM_STACK.get();
    }
}
