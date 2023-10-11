package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
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
public class ItemProjectMaterial extends AbstractProjectMaterial {
    protected static final Logger LOGGER = LogManager.getLogger();
    
    public static final String TYPE = "item";
    public static final IProjectMaterialSerializer<ItemProjectMaterial> SERIALIZER = new ItemProjectMaterial.Serializer();
    
    protected ItemStack stack;
    protected boolean consumed;
    protected boolean matchNBT;
    protected int afterCrafting;
    
    public ItemProjectMaterial() {
        super();
        this.stack = ItemStack.EMPTY;
        this.consumed = false;
        this.matchNBT = false;
        this.afterCrafting = 0;
    }
    
    public ItemProjectMaterial(@Nonnull ItemStack stack, boolean consumed, boolean matchNBT) {
        super();
        this.stack = stack;
        this.consumed = consumed;
        this.matchNBT = matchNBT;
        this.afterCrafting = 0;
    }
    
    public ItemProjectMaterial(@Nonnull ItemStack stack, boolean consumed) {
        this(stack, consumed, false);
    }
    
    public ItemProjectMaterial(@Nonnull ItemLike item, boolean consumed, boolean matchNBT) {
        this(new ItemStack(item), consumed, matchNBT);
    }
    
    public ItemProjectMaterial(@Nonnull ItemLike item, boolean consumed) {
        this(item, consumed, false);
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.put("Stack", this.stack.save(new CompoundTag()));
        tag.putBoolean("Consumed", this.consumed);
        tag.putBoolean("MatchNBT", this.matchNBT);
        tag.putInt("AfterCrafting", this.afterCrafting);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.stack = ItemStack.of(nbt.getCompound("Stack"));
        this.consumed = nbt.getBoolean("Consumed");
        this.matchNBT = nbt.getBoolean("MatchNBT");
        this.afterCrafting = nbt.getInt("AfterCrafting");
    }

    @Override
    protected String getMaterialType() {
        return TYPE;
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
    
    public void setAfterCrafting(int after) {
        this.afterCrafting = after;
    }
    
    @Override
    public boolean isAllowedInProject(ServerPlayer player) {
        return super.isAllowedInProject(player) && player.getStats().getValue(Stats.ITEM_CRAFTED.get(this.getItemStack().getItem())) >= this.getAfterCrafting();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        SERIALIZER.toNetwork(buf, this);
    }

    @Override
    public AbstractProjectMaterial copy() {
        ItemProjectMaterial material = new ItemProjectMaterial();
        material.stack = this.stack.copy();
        material.consumed = this.consumed;
        material.matchNBT = this.matchNBT;
        material.selected = this.selected;
        material.weight = this.weight;
        material.bonusReward = this.bonusReward;
        material.afterCrafting = this.afterCrafting;
        if (this.requiredResearch != null) {
            material.requiredResearch = this.requiredResearch.copy();
        }
        return material;
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

    public static class Serializer implements IProjectMaterialSerializer<ItemProjectMaterial> {
        @Override
        public ItemProjectMaterial read(ResourceLocation projectId, JsonObject json) {
            ItemStack stack = ItemUtils.parseItemStack(json.getAsJsonPrimitive("stack").getAsString());
            if (stack == null || stack.isEmpty()) {
                throw new JsonSyntaxException("Invalid item stack for material in project " + projectId.toString());
            }
            
            boolean consumed = json.getAsJsonPrimitive("consumed").getAsBoolean();
            boolean matchNbt = json.has("match_nbt") ? json.getAsJsonPrimitive("match_nbt").getAsBoolean() : false;
            
            ItemProjectMaterial retVal = new ItemProjectMaterial(stack, consumed, matchNbt);
            
            retVal.setWeight(json.getAsJsonPrimitive("weight").getAsDouble());
            if (json.has("bonus_reward")) {
                retVal.setBonusReward(json.getAsJsonPrimitive("bonus_reward").getAsDouble());
            }
            if (json.has("after_crafting")) {
                retVal.setAfterCrafting(json.getAsJsonPrimitive("after_crafting").getAsInt());
            }
            if (json.has("required_research")) {
                retVal.setRequiredResearch(CompoundResearchKey.parse(json.getAsJsonPrimitive("required_research").getAsString()));
            }
            
            return retVal;
        }

        @Override
        public ItemProjectMaterial fromNetwork(FriendlyByteBuf buf) {
            ItemProjectMaterial material = new ItemProjectMaterial(buf.readItem(), buf.readBoolean(), buf.readBoolean());
            material.setWeight(buf.readDouble());
            material.setBonusReward(buf.readDouble());
            material.setAfterCrafting(buf.readVarInt());
            CompoundResearchKey research = CompoundResearchKey.parse(buf.readUtf());
            if (research != null) {
                material.setRequiredResearch(research);
            }
            return material;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ItemProjectMaterial material) {
            buf.writeItemStack(material.stack, false);
            buf.writeBoolean(material.consumed);
            buf.writeBoolean(material.matchNBT);
            buf.writeDouble(material.weight);
            buf.writeDouble(material.bonusReward);
            buf.writeVarInt(material.afterCrafting);
            buf.writeUtf(material.requiredResearch == null ? "" : material.requiredResearch.toString());
        }
    }
}
