package com.verdantartifice.primalmagic.common.loot.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * Definition of a loot condition that matches when the block is in the given tag.
 * 
 * @author Daedalus4096
 */
public class MatchBlockTag implements LootItemCondition {
    protected final Tag.Named<Block> tag;
    
    public MatchBlockTag(Tag.Named<Block> tag) {
        this.tag = tag;
    }

    public static LootItemCondition.Builder builder(Tag.Named<Block> tag) {
        return () -> new MatchBlockTag(tag);
    }
    
    @Override
    public boolean test(LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        return state != null && this.tag.contains(state.getBlock());
    }

    @Override
    public LootItemConditionType getType() {
        return LootConditionTypesPM.MATCH_BLOCK_TAG;
    }
    
    public static class ConditionSerializer implements Serializer<MatchBlockTag> {
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
