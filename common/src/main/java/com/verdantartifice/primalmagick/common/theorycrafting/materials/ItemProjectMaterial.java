package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Definition of a project material that requires a specific item stack, which may or may not be
 * consumed as part of the project.
 * 
 * @author Daedalus4096
 */
public class ItemProjectMaterial extends AbstractProjectMaterial<ItemProjectMaterial> {
    public static MapCodec<ItemProjectMaterial> codec() { 
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                ItemStack.CODEC.fieldOf("stack").forGetter(ItemProjectMaterial::getItemStack),
                Codec.BOOL.fieldOf("consumed").forGetter(ItemProjectMaterial::isConsumed),
                Codec.BOOL.fieldOf("matchNBT").forGetter(material -> material.matchNBT),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("afterCrafting").forGetter(ItemProjectMaterial::getAfterCrafting),
                Codec.DOUBLE.fieldOf("weight").forGetter(ItemProjectMaterial::getWeight),
                Codec.DOUBLE.fieldOf("bonusReward").forGetter(ItemProjectMaterial::getBonusReward),
                AbstractRequirement.dispatchCodec().optionalFieldOf("requirement").forGetter(ItemProjectMaterial::getRequirement)
            ).apply(instance, ItemProjectMaterial::new));
    }
    
    public static StreamCodec<RegistryFriendlyByteBuf, ItemProjectMaterial> streamCodec() {
        return StreamCodecUtils.composite(
                ItemStack.STREAM_CODEC,
                ItemProjectMaterial::getItemStack,
                ByteBufCodecs.BOOL,
                ItemProjectMaterial::isConsumed,
                ByteBufCodecs.BOOL,
                mat -> mat.matchNBT,
                ByteBufCodecs.VAR_INT,
                ItemProjectMaterial::getAfterCrafting,
                ByteBufCodecs.DOUBLE,
                ItemProjectMaterial::getWeight,
                ByteBufCodecs.DOUBLE,
                ItemProjectMaterial::getBonusReward,
                ByteBufCodecs.optional(AbstractRequirement.dispatchStreamCodec()),
                ItemProjectMaterial::getRequirement,
                ItemProjectMaterial::new);
    }
    
    protected final ItemStack stack;
    protected final boolean consumed;
    protected final boolean matchNBT;
    protected final int afterCrafting;
    
    protected ItemProjectMaterial(ItemStack stack, boolean consumed, boolean matchNBT, int afterCrafting, double weight, double bonusReward, Optional<AbstractRequirement<?>> requirement) {
        super(weight, bonusReward, requirement);
        this.stack = stack.copy();
        this.consumed = consumed;
        this.matchNBT = matchNBT;
        this.afterCrafting = afterCrafting;
    }
    
    @Override
    protected ProjectMaterialType<ItemProjectMaterial> getType() {
        return ProjectMaterialTypesPM.ITEM.get();
    }

    @Override
    public boolean isSatisfied(Player player, Set<Block> surroundings) {
        if (InventoryUtils.isPlayerCarrying(player, this.stack, this.matchNBT)) {
            return true;
        } else if (!this.consumed && this.stack.getCount() == 1 && surroundings != null && this.stack.getItem() instanceof BlockItem blockItem && surroundings.contains(blockItem.getBlock())) {
            // Only allow satisfaction from surroundings if not consuming the material and only one item is required
            return true;
        }
        return false;
    }

    @Override
    public boolean consume(Player player) {
        // Remove this material's item from the player's inventory if it's supposed to be consumed
        if (this.consumed) {
            return InventoryUtils.consumeItem(player, this.stack, this.matchNBT);
        } else {
            return true;
        }
    }
    
    @Nonnull
    public ItemStack getItemStack() {
        return this.stack;
    }
    
    @Override
    public boolean isConsumed() {
        return this.consumed;
    }
    
    public int getAfterCrafting() {
        return this.afterCrafting;
    }
    
    @Override
    public boolean isAllowedInProject(ServerPlayer player) {
        return super.isAllowedInProject(player) && player.getStats().getValue(Stats.ITEM_CRAFTED.get(this.getItemStack().getItem())) >= this.getAfterCrafting();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(afterCrafting, consumed, matchNBT, stack);
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
        ItemProjectMaterial other = (ItemProjectMaterial) obj;
        return afterCrafting == other.afterCrafting && consumed == other.consumed && matchNBT == other.matchNBT
                && ItemStack.matches(stack, other.stack);
    }

    public static Builder builder(ItemStack stack) {
        return new Builder(stack);
    }
    
    public static Builder builder(ItemLike item, int count) {
        return builder(new ItemStack(Preconditions.checkNotNull(item).asItem(), count));
    }
    
    public static Builder builder(ItemLike item) {
        return builder(item, 1);
    }
    
    public static class Builder extends AbstractProjectMaterial.Builder<ItemProjectMaterial, Builder> {
        protected final ItemStack stack;
        protected boolean consumed = false;
        protected boolean matchNBT = false;
        protected int afterCrafting = 0;
        
        protected Builder(ItemStack stack) {
            this.stack = Preconditions.checkNotNull(stack).copy();
        }
        
        public Builder consumed() {
            this.consumed = true;
            return this;
        }
        
        public Builder matchNbt() {
            this.matchNBT = true;
            return this;
        }
        
        public Builder afterCrafting(int count) {
            this.afterCrafting = count;
            return this;
        }

        @Override
        protected void validate() {
            super.validate();
            if (this.afterCrafting < 0) {
                throw new IllegalStateException("Material crafting minimum must be non-negative");
            }
        }

        @Override
        public ItemProjectMaterial build() {
            this.validate();
            return new ItemProjectMaterial(this.stack, this.consumed, this.matchNBT, this.afterCrafting, this.weight, this.bonusReward, this.getFinalRequirement());
        }
    }
}
