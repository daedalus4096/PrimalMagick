package com.verdantartifice.primalmagick.common.research.keys;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.misc.IconDefinition;
import com.verdantartifice.primalmagick.common.research.ResearchManager;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public class StackCraftedKey extends AbstractResearchKey<StackCraftedKey> {
    public static final MapCodec<StackCraftedKey> CODEC = ItemStackTemplate.CODEC.fieldOf("stack").xmap(StackCraftedKey::new, key -> key.stack);
    public static final StreamCodec<RegistryFriendlyByteBuf, StackCraftedKey> STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.map(StackCraftedKey::new, key -> key.stack);
    
    private static final String PREFIX = "[#]";
    
    protected final ItemStackTemplate stack;
    
    public StackCraftedKey(ItemStackTemplate stack) {
        if (stack == null) {
            throw new IllegalArgumentException("Item stack template may not be null");
        }
        this.stack = stack.withCount(1);    // Preserve the stack components but not its count
        ResearchManager.addCraftingReference(this.hashCode());
    }
    
    public StackCraftedKey(ItemLike itemLike) {
        this(new ItemStackTemplate(itemLike.asItem()));
    }
    
    @Override
    public String toString() {
        return PREFIX + this.hashCode();
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
        return IconDefinition.of(this.stack.item().value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Services.ITEMS_REGISTRY.getKey(this.stack.item().value()), this.stack.components());
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
        return this.stack.equals(other.stack);
    }
}
