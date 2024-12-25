package com.verdantartifice.primalmagick.common.research.requirements;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

/**
 * Requirement that the player possesses a stack(s) of the given item at least as large as the
 * given stack, with the same NBT.  The item is intended for consumption as part of the check.
 * 
 * @author Daedalus4096
 */
public class ItemStackRequirement extends AbstractRequirement<ItemStackRequirement> {
    public static final MapCodec<ItemStackRequirement> CODEC = ItemStack.CODEC.fieldOf("stack").xmap(ItemStackRequirement::new, req -> req.stack);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemStackRequirement> STREAM_CODEC = ItemStack.STREAM_CODEC.map(ItemStackRequirement::new, req -> req.stack);
    
    protected final ItemStack stack;
    
    public ItemStackRequirement(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            throw new IllegalArgumentException("Item stack may not be empty");
        }
        this.stack = stack.copy();
    }
    
    public ItemStack getStack() {
        return this.stack;
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
    public boolean forceComplete(Player player) {
        // Do nothing; materials are not consumed when force completing an item requirement
        return true;
    }

    @Override
    public RequirementCategory getCategory() {
        return RequirementCategory.MUST_OBTAIN;
    }

    @Override
    public Stream<AbstractRequirement<?>> streamByCategory(RequirementCategory category) {
        return category == this.getCategory() ? Stream.of(this) : Stream.empty();
    }

    @Override
    protected RequirementType<ItemStackRequirement> getType() {
        return RequirementsPM.ITEM_STACK.get();
    }
}
