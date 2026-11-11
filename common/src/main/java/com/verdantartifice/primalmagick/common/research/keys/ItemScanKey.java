package com.verdantartifice.primalmagick.common.research.keys;

import com.mojang.serialization.MapCodec;
import com.verdantartifice.primalmagick.common.misc.IconDefinition;
import com.verdantartifice.primalmagick.common.research.requirements.RequirementCategory;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;

import java.util.Objects;

public class ItemScanKey extends AbstractResearchKey<ItemScanKey> {
    public static final MapCodec<ItemScanKey> CODEC = ItemStackTemplate.CODEC.fieldOf("stack").xmap(ItemScanKey::new, key -> key.stack);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemScanKey> STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.map(ItemScanKey::new, key -> key.stack);
    
    private static final String PREFIX = "!";
    
    protected final ItemStackTemplate stack;
    
    public ItemScanKey(ItemStackTemplate stack) {
        if (stack == null) {
            throw new IllegalArgumentException("Item stack may not be null or empty");
        }
        this.stack = stack;
    }

    public ItemScanKey(ItemLike itemLike) {
        this(new ItemStackTemplate(itemLike.asItem()));
    }

    public ItemStackTemplate getStack() {
        return this.stack;
    }
    
    @Override
    public String toString() {
        return PREFIX + this.hashCode();
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
    public IconDefinition getIcon(RegistryAccess registryAccess) {
        return IconDefinition.of(this.stack.item().value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Services.ITEMS_REGISTRY.getKey(this.stack.item().value()));
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
        return this.stack.equals(other.stack);
    }
}
