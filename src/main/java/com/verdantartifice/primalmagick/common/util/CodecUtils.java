package com.verdantartifice.primalmagick.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public class CodecUtils {
    public static final Codec<String> SINGLE_CHARACTER_STRING_CODEC = Codec.STRING.flatXmap(str -> {
        if (str.length() != 1) {
            return DataResult.error(() -> "Invalid key entry: '" + str + "' is an invalid symbol (must be 1 character only).");
        } else {
            return " ".equals(str) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(str);
        }
    }, DataResult::success);

    private static final Codec<Item> ITEM_NONAIR_CODEC = ExtraCodecs.validate(ForgeRegistries.ITEMS.getCodec(), item -> {
        return item == Items.AIR ? DataResult.error(() -> "Item must not be minecraft:air") : DataResult.success(item);
    });

    public static final Codec<ItemStack> ITEMSTACK_WITH_NBT_CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                ITEM_NONAIR_CODEC.fieldOf("item").forGetter(ItemStack::getItem),
                ExtraCodecs.strictOptionalField(ExtraCodecs.POSITIVE_INT, "count", 1).forGetter(ItemStack::getCount),
                CompoundTag.CODEC.optionalFieldOf("tag", null).forGetter(ItemStack::getTag)
            ).apply(instance, ItemStack::new);
    });
    
    public static final Codec<Block> BLOCK_NONAIR_CODEC = ExtraCodecs.validate(ForgeRegistries.BLOCKS.getCodec(), block -> {
        return block == Blocks.AIR ? DataResult.error(() -> "Block must not be minecraft:air") : DataResult.success(block);
    });
}
