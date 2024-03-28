package com.verdantartifice.primalmagick.common.books.grids;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.books.grids.rewards.AbstractReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.EmptyReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.IReward;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Class encapsulating a data-defined node definition for a linguistics grid.  These definitions
 * determine the contents of each node of a specific linguistics comprehension grid.
 * 
 * @author Daedalus4096
 */
public class GridNodeDefinition implements INBTSerializable<CompoundTag> {
    public static final IGridNodeDefinitionSerializer SERIALIZER = new Serializer();
    
    protected int vocabularyCost;
    protected IReward reward;
    
    private GridNodeDefinition() {}
    
    protected GridNodeDefinition(int vocabularyCost, IReward reward) {
        this.vocabularyCost = vocabularyCost;
        this.reward = reward;
    }
    
    @Nullable
    public static GridNodeDefinition fromNBT(@Nullable CompoundTag tag) {
        if (tag == null) {
            return null;
        } else {
            GridNodeDefinition retVal = new GridNodeDefinition();
            retVal.deserializeNBT(tag);
            return retVal;
        }
    }

    public int getVocabularyCost() {
        return this.vocabularyCost;
    }
    
    @Nonnull
    public IReward getReward() {
        return this.reward == null ? EmptyReward.INSTANCE : this.reward;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = new CompoundTag();
        retVal.putInt("Cost", this.vocabularyCost);
        retVal.put("Reward", this.reward.serializeNBT());
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.vocabularyCost = nbt.getInt("Cost");
        this.reward = AbstractReward.fromNBT(nbt.getCompound("Reward"));
    }
    
    public static class Serializer implements IGridNodeDefinitionSerializer {
        @Override
        public GridNodeDefinition read(ResourceLocation gridId, JsonObject json) {
            int cost = json.getAsJsonPrimitive("cost").getAsInt();
            IReward reward = AbstractReward.fromJson(json.getAsJsonObject("reward"), gridId);
            return new GridNodeDefinition(cost, reward);
        }

        @Override
        public GridNodeDefinition fromNetwork(FriendlyByteBuf buf) {
            int cost = buf.readVarInt();
            IReward reward = AbstractReward.fromNetwork(buf);
            return new GridNodeDefinition(cost, reward);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GridNodeDefinition nodeDef) {
            buf.writeVarInt(nodeDef.vocabularyCost);
            AbstractReward.toNetwork(buf, nodeDef.reward);
        }
    }
}
