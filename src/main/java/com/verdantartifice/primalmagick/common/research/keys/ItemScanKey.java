package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ItemScanKey extends AbstractResearchKey<ItemScanKey> {
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
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.RESEARCH;
    }

    @Override
    protected ResearchKeyType<ItemScanKey> getType() {
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
    
    @Nonnull
    static ItemScanKey fromNetworkInner(FriendlyByteBuf buf) {
        return new ItemScanKey(buf.readItem());
    }

    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
    }
}
