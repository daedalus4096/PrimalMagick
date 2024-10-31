package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import com.verdantartifice.primalmagick.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Definition of a project material that requires an item stack from a given tag, which may or may not be
 * consumed as part of the project.
 * 
 * @author Daedalus4096
 */
public class ItemTagProjectMaterial extends AbstractProjectMaterial<ItemTagProjectMaterial> {
    public static MapCodec<ItemTagProjectMaterial> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(ItemTagProjectMaterial::getTag),
                ExtraCodecs.POSITIVE_INT.fieldOf("quantity").forGetter(ItemTagProjectMaterial::getQuantity),
                Codec.BOOL.fieldOf("consumed").forGetter(ItemTagProjectMaterial::isConsumed),
                Codec.DOUBLE.fieldOf("weight").forGetter(ItemTagProjectMaterial::getWeight),
                Codec.DOUBLE.fieldOf("bonusReward").forGetter(ItemTagProjectMaterial::getBonusReward),
                AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(ItemTagProjectMaterial::getRequirement)
            ).apply(instance, ItemTagProjectMaterial::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, ItemTagProjectMaterial> streamCodec() {
        return StreamCodec.composite(
                StreamCodecUtils.tagKey(Registries.ITEM),
                ItemTagProjectMaterial::getTag,
                ByteBufCodecs.VAR_INT,
                ItemTagProjectMaterial::getQuantity,
                ByteBufCodecs.BOOL,
                ItemTagProjectMaterial::isConsumed,
                ByteBufCodecs.DOUBLE,
                ItemTagProjectMaterial::getWeight,
                ByteBufCodecs.DOUBLE,
                ItemTagProjectMaterial::getBonusReward,
                ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()),
                ItemTagProjectMaterial::getRequirement,
                ItemTagProjectMaterial::new);
    }
    
    protected final TagKey<Item> tag;
    protected final int quantity;
    protected final boolean consumed;
    
    protected ItemTagProjectMaterial(TagKey<Item> tag, int quantity, boolean consumed, double weight, double bonusReward, Optional<AbstractRequirement<?>> requirement) {
        super(weight, bonusReward, requirement);
        this.tag = tag;
        this.quantity = quantity;
        this.consumed = consumed;
    }
    
    @Override
    protected ProjectMaterialType<ItemTagProjectMaterial> getType() {
        return ProjectMaterialTypesPM.ITEM_TAG.get();
    }

    @Override
    public boolean isSatisfied(Player player, Set<Block> surroundings) {
        if (InventoryUtils.isPlayerCarrying(player, this.tag, this.quantity)) {
            return true;
        } else if (!this.consumed && this.quantity == 1 && surroundings != null) {
            // Only allow satisfaction from surroundings if not consuming the material and only one item is required
            TagKey<Block> blockTagKey = BlockTags.create(this.tag.location());
            List<Block> tagContents = new ArrayList<>();
            Services.TAGS.block(blockTagKey).forEach(tagContents::add);
            Set<Block> intersection = new HashSet<>(surroundings);
            intersection.retainAll(tagContents);
            return !intersection.isEmpty();
        }
        return false;
    }

    @Override
    public boolean consume(Player player) {
        // Remove items matching this material's tag from the player's inventory if it's supposed to be consumed
        if (this.consumed) {
            return InventoryUtils.consumeItem(player, this.tag, this.quantity);
        } else {
            return true;
        }
    }
    
    @Nullable
    public TagKey<Item> getTag() {
        return this.tag;
    }
    
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public boolean isConsumed() {
        return this.consumed;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (consumed ? 1231 : 1237);
        result = prime * result + quantity;
        result = prime * result + ((tag == null) ? 0 : tag.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemTagProjectMaterial other = (ItemTagProjectMaterial) obj;
        if (consumed != other.consumed)
            return false;
        if (quantity != other.quantity)
            return false;
        if (tag == null) {
            if (other.tag != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        return true;
    }

    public static Builder builder(TagKey<Item> tag) {
        return new Builder(tag);
    }
    
    public static class Builder extends AbstractProjectMaterial.Builder<ItemTagProjectMaterial, Builder> {
        protected final TagKey<Item> tag;
        protected int quantity = 1;
        protected boolean consumed = false;
        
        protected Builder(TagKey<Item> tag) {
            this.tag = Preconditions.checkNotNull(tag);
        }
        
        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }
        
        public Builder consumed() {
            this.consumed = true;
            return this;
        }

        @Override
        protected void validate() {
            super.validate();
            if (this.quantity <= 0) {
                throw new IllegalStateException("Material quantity must be positive");
            }
        }

        @Override
        public ItemTagProjectMaterial build() {
            this.validate();
            return new ItemTagProjectMaterial(this.tag, this.quantity, this.consumed, this.weight, this.bonusReward, this.getFinalRequirement());
        }
    }
}
