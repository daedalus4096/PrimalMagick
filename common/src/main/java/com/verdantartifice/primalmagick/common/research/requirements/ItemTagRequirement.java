package com.verdantartifice.primalmagick.common.research.requirements;

import java.util.stream.Stream;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.verdantartifice.primalmagick.common.util.InventoryUtils;
import com.verdantartifice.primalmagick.common.util.StreamCodecUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

/**
 * Requirement that the player possesses a stack(s) of the given tag at least as large as the
 * given stack, with the same NBT.  The item is intended for consumption as part of the check.
 * 
 * @author Daedalus4096
 */
public class ItemTagRequirement extends AbstractRequirement<ItemTagRequirement> {
    public static final MapCodec<ItemTagRequirement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(req -> req.tag),
            ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(req -> req.amount)
        ).apply(instance, ItemTagRequirement::new));
    public static final StreamCodec<ByteBuf, ItemTagRequirement> STREAM_CODEC = StreamCodec.composite(
            StreamCodecUtils.tagKey(Registries.ITEM),
            ItemTagRequirement::getTag,
            ByteBufCodecs.VAR_INT,
            ItemTagRequirement::getAmount,
            ItemTagRequirement::new);
    
    protected final TagKey<Item> tag;
    protected final int amount;
    
    public ItemTagRequirement(TagKey<Item> tag, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.tag = Preconditions.checkNotNull(tag);
        this.amount = amount;
    }
    
    public TagKey<Item> getTag() {
        return this.tag;
    }
    
    public int getAmount() {
        return this.amount;
    }

    @Override
    public boolean isMetBy(Player player) {
        return player != null && InventoryUtils.isPlayerCarrying(player, this.tag, this.amount);
    }

    @Override
    public void consumeComponents(Player player) {
        if (player != null && this.isMetBy(player)) {
            InventoryUtils.consumeItem(player, this.tag, this.amount);
        }
    }

    @Override
    public boolean forceComplete(Player player) {
        // Do nothing; materials are not consumed when force completing an item requirement
        return true;
    }

    @Override
    public RequirementCategory getCategory() {
        return RequirementCategory.MUST_OBTAIN;
    }

    @Override
    public Stream<AbstractRequirement<?>> streamByCategory(RequirementCategory category) {
        return category == this.getCategory() ? Stream.of(this) : Stream.empty();
    }

    @Override
    protected RequirementType<ItemTagRequirement> getType() {
        return RequirementsPM.ITEM_TAG.get();
    }
}
