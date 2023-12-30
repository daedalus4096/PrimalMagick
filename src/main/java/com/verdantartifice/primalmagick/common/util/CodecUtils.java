package com.verdantartifice.primalmagick.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.util.ExtraCodecs;
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

    public static final Codec<Block> BLOCK_NONAIR_CODEC = ExtraCodecs.validate(ForgeRegistries.BLOCKS.getCodec(), block -> {
        return block == Blocks.AIR ? DataResult.error(() -> "Block must not be minecraft:air") : DataResult.success(block);
    });
}
