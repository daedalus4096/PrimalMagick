package com.verdantartifice.primalmagic.common.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

/**
 * Definition of a loot condition that matches when the block is in the given tag.
 * 
 * @author Daedalus4096
 */
public class MatchBlockTag implements ILootCondition {
    protected final ITag.INamedTag<Block> tag;
    
    public MatchBlockTag(ITag.INamedTag<Block> tag) {
        this.tag = tag;
    }

    public static ILootCondition.IBuilder builder(ITag.INamedTag<Block> tag) {
        return () -> new MatchBlockTag(tag);
    }
    
    @Override
    public boolean test(LootContext context) {
        BlockState state = context.get(LootParameters.BLOCK_STATE);
        return state != null && state.getBlock().isIn(this.tag);
    }

    @Override
    public LootConditionType getConditionType() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public static class Serializer implements ILootSerializer<MatchBlockTag> {
        @Override
        public void serialize(JsonObject obj, MatchBlockTag condition, JsonSerializationContext context) {
            obj.addProperty("tag", condition.tag.getName().toString());
        }

        @Override
        public MatchBlockTag deserialize(JsonObject obj, JsonDeserializationContext context) {
            ResourceLocation name = new ResourceLocation(obj.getAsJsonPrimitive("tag").getAsString());
            return new MatchBlockTag(BlockTags.createOptional(name));
        }
    }
}
