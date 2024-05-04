package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Requirement that the player possesses a stack(s) of the given item at least as large as the
 * given stack, with the same NBT.  The item is intended for consumption as part of the check.
 * 
 * @author Daedalus4096
 */
public class ItemStackRequirement extends AbstractRequirement<ItemStackRequirement> {
    public static final Codec<ItemStackRequirement> CODEC = ItemStack.CODEC.fieldOf("stack").xmap(ItemStackRequirement::new, req -> req.stack).codec();
    
    protected final ItemStack stack;
    
    public ItemStackRequirement(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            throw new IllegalArgumentException("Item stack may not be empty");
        }
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

    @Nonnull
    public static ItemStackRequirement fromNetwork(FriendlyByteBuf buf) {
        return new ItemStackRequirement(buf.readItem());
    }
    
    @Override
    public void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeItemStack(this.stack, false);
    }
}
