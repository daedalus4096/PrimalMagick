package com.verdantartifice.primalmagick.common.theorycrafting.materials;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.research.requirements.AbstractRequirement;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

/**
 * Definition of a project material that requires a specific item stack, which may or may not be
 * consumed as part of the project.
 * 
 * @author Daedalus4096
 */
public class ItemProjectMaterial extends AbstractProjectMaterial<ItemProjectMaterial> {
    public static final Codec<ItemProjectMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("stack").forGetter(ItemProjectMaterial::getItemStack),
            Codec.BOOL.fieldOf("consumed").forGetter(ItemProjectMaterial::isConsumed),
            Codec.BOOL.fieldOf("matchNBT").forGetter(material -> material.matchNBT),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("afterCrafting").forGetter(ItemProjectMaterial::getAfterCrafting),
            Codec.DOUBLE.fieldOf("weight").forGetter(ItemProjectMaterial::getWeight),
            Codec.DOUBLE.fieldOf("bonusReward").forGetter(ItemProjectMaterial::getBonusReward),
            AbstractRequirement.CODEC.optionalFieldOf("requirement").forGetter(ItemProjectMaterial::getRequirement)
        ).apply(instance, ItemProjectMaterial::new));
    
    protected ItemStack stack;
    protected boolean consumed;
    protected boolean matchNBT;
    protected int afterCrafting;
    
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

    @Nonnull
    public static ItemProjectMaterial fromNetworkInner(FriendlyByteBuf buf, double weight, double bonusReward, Optional<AbstractRequirement<?>> requirement) {
        return new ItemProjectMaterial(buf.readItem(), buf.readBoolean(), buf.readBoolean(), buf.readVarInt(), weight, bonusReward, requirement);
    }
    
    @Override
    protected void toNetworkInner(FriendlyByteBuf buf) {
        buf.writeItemStack(this.stack, false);
        buf.writeBoolean(this.consumed);
        buf.writeBoolean(this.matchNBT);
        buf.writeVarInt(this.afterCrafting);
    }
    
    public static class Builder extends AbstractProjectMaterial.Builder<ItemProjectMaterial, Builder> {
        protected final ItemStack stack;
        protected boolean consumed = false;
        protected boolean matchNBT = false;
        protected int afterCrafting = 0;
        
        public Builder(ItemStack stack) {
            this.stack = stack.copy();
        }
        
        public Builder(ItemLike item, int count) {
            this(new ItemStack(item.asItem(), count));
        }
        
        public Builder(ItemLike item) {
            this(item, 1);
        }
        
        public Builder consumed() {
            this.consumed = true;
            return this;
        }
        
        public Builder matchNBT() {
            this.matchNBT = true;
            return this;
        }
        
        public Builder afterCrafting(int count) {
            this.afterCrafting = count;
            return this;
        }

        @Override
        public ItemProjectMaterial build() {
            return new ItemProjectMaterial(this.stack, this.consumed, this.matchNBT, this.afterCrafting, this.weight, this.bonusReward, this.getFinalRequirement());
        }
    }
}
