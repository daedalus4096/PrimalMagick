package com.verdantartifice.primalmagick.datagen.theorycrafting;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.verdantartifice.primalmagick.common.theorycrafting.rewards.ItemReward;
import com.verdantartifice.primalmagick.common.util.ItemUtils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class ItemRewardBuilder {
    protected final ItemStack stack;
    
    protected ItemRewardBuilder(@Nonnull ItemStack stack) {
        this.stack = stack.copy();
    }
    
    public static ItemRewardBuilder item(@Nonnull ItemStack stack) {
        return new ItemRewardBuilder(stack);
    }
    
    public static ItemRewardBuilder item(@Nonnull ItemLike item, int count) {
        return item(new ItemStack(item.asItem(), count));
    }
    
    public static ItemRewardBuilder item(@Nonnull ItemLike item) {
        return item(item, 1);
    }
    
    private void validate() {
        if (this.stack == null || this.stack == ItemStack.EMPTY) {
            throw new IllegalStateException("No item stack for item project reward");
        }
    }
    
    public IFinishedProjectReward build() {
        this.validate();
        return new ItemRewardBuilder.Result(this.stack);
    }
    
    public static class Result implements IFinishedProjectReward {
        protected final ItemStack stack;
        
        public Result(@Nonnull ItemStack stack) {
            this.stack = stack.copy();
        }

        @Override
        public void serialize(JsonObject json) {
            json.addProperty("type", ItemReward.TYPE);
            json.addProperty("stack", ItemUtils.serializeItemStack(this.stack));
        }
    }
}
