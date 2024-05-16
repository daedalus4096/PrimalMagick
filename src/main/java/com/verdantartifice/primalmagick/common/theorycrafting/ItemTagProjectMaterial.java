package com.verdantartifice.primalmagick.common.theorycrafting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.research.CompoundResearchKey;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Definition of a project material that requires an item stack from a given tag, which may or may not be
 * consumed as part of the project.
 * 
 * @author Daedalus4096
 */
public class ItemTagProjectMaterial extends AbstractProjectMaterial {
    public static final String TYPE = "tag";
    public static final IProjectMaterialSerializer<ItemTagProjectMaterial> SERIALIZER = new ItemTagProjectMaterial.Serializer();
    
    protected TagKey<Item> tag;
    protected int quantity;
    protected boolean consumed;
    
    public ItemTagProjectMaterial() {
        super();
        this.tag = null;
        this.quantity = -1;
        this.consumed = false;
    }
    
    public ItemTagProjectMaterial(@Nonnull TagKey<Item> tag, int quantity, boolean consumed) {
        super();
        this.tag = tag;
        this.quantity = quantity;
        this.consumed = consumed;
    }
    
    public ItemTagProjectMaterial(@Nonnull TagKey<Item> tag, boolean consumed) {
        this(tag, 1, consumed);
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        if (this.tag != null) {
            tag.putString("TagName", this.tag.location().toString());
        }
        tag.putInt("Quantity", this.quantity);
        tag.putBoolean("Consumed", this.consumed);
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        if (nbt.contains("TagName")) {
            this.tag = ItemTags.create(new ResourceLocation(nbt.getString("TagName")));
        } else {
            this.tag = null;
        }
        this.quantity = nbt.getInt("Quantity");
        this.consumed = nbt.getBoolean("Consumed");
    }

    @Override
    protected String getMaterialType() {
        return TYPE;
    }

    @Override
    public boolean isSatisfied(Player player, Set<Block> surroundings) {
        if (InventoryUtils.isPlayerCarrying(player, this.tag, this.quantity)) {
            return true;
        } else if (!this.consumed && this.quantity == 1 && surroundings != null) {
            // Only allow satisfaction from surroundings if not consuming the material and only one item is required
            TagKey<Block> blockTagKey = BlockTags.create(this.tag.location());
            List<Block> tagContents = new ArrayList<Block>();
            ForgeRegistries.BLOCKS.tags().getTag(blockTagKey).forEach(b -> tagContents.add(b));
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
    public void toNetwork(FriendlyByteBuf buf) {
        SERIALIZER.toNetwork(buf, this);
    }

    @Override
    public AbstractProjectMaterial copy() {
        ItemTagProjectMaterial material = new ItemTagProjectMaterial();
        material.tag = ItemTags.create(this.tag.location());
        material.quantity = this.quantity;
        material.consumed = this.consumed;
        material.selected = this.selected;
        material.weight = this.weight;
        material.bonusReward = this.bonusReward;
        if (this.requiredResearch != null) {
            material.requiredResearch = this.requiredResearch.copy();
        }
        return material;
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
    
    public static class Serializer implements IProjectMaterialSerializer<ItemTagProjectMaterial> {
        @Override
        public ItemTagProjectMaterial read(ResourceLocation projectId, JsonObject json) {
            String nameStr = json.getAsJsonPrimitive("name").getAsString();
            if (nameStr == null) {
                throw new JsonSyntaxException("Illegal tag name in material JSON for project " + projectId.toString());
            }
            ResourceLocation tagName = new ResourceLocation(nameStr);
            TagKey<Item> tag = ItemTags.create(tagName);
            
            boolean consumed = json.getAsJsonPrimitive("consumed").getAsBoolean();
            int quantity = json.getAsJsonPrimitive("quantity").getAsInt();
            
            ItemTagProjectMaterial retVal = new ItemTagProjectMaterial(tag, quantity, consumed);
            
            retVal.setWeight(json.getAsJsonPrimitive("weight").getAsDouble());
            if (json.has("bonus_reward")) {
                retVal.setBonusReward(json.getAsJsonPrimitive("bonus_reward").getAsDouble());
            }
            if (json.has("required_research")) {
                retVal.setRequiredResearch(CompoundResearchKey.parse(json.getAsJsonPrimitive("required_research").getAsString()));
            }

            return retVal;
        }

        @Override
        public ItemTagProjectMaterial fromNetwork(FriendlyByteBuf buf) {
            ItemTagProjectMaterial material = new ItemTagProjectMaterial(ItemTags.create(buf.readResourceLocation()), buf.readVarInt(), buf.readBoolean());
            material.setWeight(buf.readDouble());
            material.setBonusReward(buf.readDouble());
            CompoundResearchKey research = CompoundResearchKey.parse(buf.readUtf());
            if (research != null) {
                material.setRequiredResearch(research);
            }
            return material;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ItemTagProjectMaterial material) {
            buf.writeResourceLocation(material.tag.location());
            buf.writeVarInt(material.quantity);
            buf.writeBoolean(material.consumed);
            buf.writeDouble(material.weight);
            buf.writeDouble(material.bonusReward);
            buf.writeUtf(material.requiredResearch == null ? "" : material.requiredResearch.toString());
        }
    }
}
