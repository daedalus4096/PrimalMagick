package com.verdantartifice.primalmagick.common.loot.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * Definition of a loot condition that matches when the block is in the given tag.
 * 
 * @author Daedalus4096
 */
public class MatchBlockTag implements LootItemCondition {
    public static final Codec<MatchBlockTag> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(TagKey.codec(Registries.BLOCK).fieldOf("tag").forGetter(mbt -> mbt.tag)).apply(instance, MatchBlockTag::new);
    });
    
    protected final TagKey<Block> tag;
    
    public MatchBlockTag(TagKey<Block> tag) {
        this.tag = tag;
    }

    public static LootItemCondition.Builder builder(TagKey<Block> tag) {
        return () -> new MatchBlockTag(tag);
    }
    
    @Override
    public boolean test(LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        return state != null && state.is(this.tag);
    }

    @Override
    public LootItemConditionType getType() {
        return LootConditionTypesPM.MATCH_BLOCK_TAG;
    }
}
