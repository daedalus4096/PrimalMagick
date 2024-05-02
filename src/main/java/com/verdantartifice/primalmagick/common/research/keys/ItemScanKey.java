package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.world.item.ItemStack;

public class ItemScanKey extends AbstractResearchKey {
    public static final Codec<ItemScanKey> CODEC = ItemStack.SINGLE_ITEM_CODEC.fieldOf("stack").xmap(ItemScanKey::new, key -> key.stack).codec();
    private static final String PREFIX = "!";
    
    protected final ItemStack stack;
    
    public ItemScanKey(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            throw new IllegalArgumentException("Item stack may not be null or empty");
        }
        this.stack = stack.copy();
    }

    @Override
    public String toString() {
        return PREFIX + ItemUtils.getHashCode(this.stack, true);
    }

    @Override
    protected ResearchKeyType<?> getType() {
        return ResearchKeyTypesPM.ITEM_SCAN.get();
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemScanKey other = (ItemScanKey) obj;
        return ItemStack.isSameItem(this.stack, other.stack);
    }
}
