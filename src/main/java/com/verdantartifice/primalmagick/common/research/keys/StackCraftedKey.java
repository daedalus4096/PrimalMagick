package com.verdantartifice.primalmagick.common.research.keys;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.verdantartifice.primalmagick.common.research.IconDefinition;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public class StackCraftedKey extends AbstractResearchKey<StackCraftedKey> {
    public static final Codec<StackCraftedKey> CODEC = ItemStack.CODEC.fieldOf("stack").xmap(StackCraftedKey::new, key -> key.stack).codec();
    private static final String PREFIX = "[#]";
    
    protected final ItemStack stack;
    
    public StackCraftedKey(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            throw new IllegalArgumentException("Item stack may not be null or empty");
        }
        this.stack = stack.copyWithCount(1);    // Preserve the stack NBT but not its count
        ResearchManager.addCraftingReference(ItemUtils.getHashCode(this.stack));
    }
    
    public StackCraftedKey(ItemLike itemLike) {
        this(new ItemStack(itemLike.asItem()));
    }
    
    @Override
    public String toString() {
        return PREFIX + ItemUtils.getHashCode(this.stack);
    }

    @Override
    public RequirementCategory getRequirementCategory() {
        return RequirementCategory.MUST_CRAFT;
    }

    @Override
    protected ResearchKeyType<StackCraftedKey> getType() {
        return ResearchKeyTypesPM.STACK_CRAFTED.get();
    }

    @Override
    public IconDefinition getIcon(RegistryAccess registryAccess) {
        return IconDefinition.of(this.stack.getItem());
    }

    @Override
    public int hashCode() {
        return this.stack.hasTag() ? Objects.hash(ForgeRegistries.ITEMS.getKey(this.stack.getItem()), this.stack.getTag()) : Objects.hash(ForgeRegistries.ITEMS.getKey(this.stack.getItem()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StackCraftedKey other = (StackCraftedKey) obj;
        return ItemStack.isSameItemSameTags(this.stack, other.stack);
    }

    @Nonnull
    static StackCraftedKey fromNetworkInner(FriendlyByteBuf buf) {
        return new StackCraftedKey(buf.readItem());
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeItemStack(this.stack, false);
    }
}
