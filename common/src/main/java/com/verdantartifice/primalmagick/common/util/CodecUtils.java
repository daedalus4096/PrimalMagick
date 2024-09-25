package com.verdantartifice.primalmagick.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector2i;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Function;

public class CodecUtils {
    public static final Codec<String> SINGLE_CHARACTER_STRING_CODEC = Codec.STRING.flatXmap(str -> {
        if (str.length() != 1) {
            return DataResult.error(() -> "Invalid key entry: '" + str + "' is an invalid symbol (must be 1 character only).");
        } else {
            return " ".equals(str) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(str);
        }
    }, DataResult::success);

    public static final Codec<Block> BLOCK_NONAIR_CODEC = ForgeRegistries.BLOCKS.getCodec().validate(block -> {
        return block == Blocks.AIR ? DataResult.error(() -> "Block must not be minecraft:air") : DataResult.success(block);
    });
    
    public static final Codec<Vector2i> VECTOR2I = Codec.INT.listOf().comapFlatMap(intList -> {
        return Util.fixedSize(intList, 2).map(values -> new Vector2i(values.get(0), values.get(1)));
    }, vec -> {
        return List.of(vec.x(), vec.y());
    });
    
    private static final Function<Optional<Integer>, OptionalInt> TO_OPTIONAL_INT = intOpt -> intOpt.map(OptionalInt::of).orElseGet(OptionalInt::empty);
    private static final Function<OptionalInt, Optional<Integer>> FROM_OPTIONAL_INT = intOpt -> intOpt.isPresent() ? Optional.of(intOpt.getAsInt()) : Optional.empty();
    public static MapCodec<OptionalInt> asOptionalInt(MapCodec<Optional<Integer>> codec) {
        return codec.xmap(TO_OPTIONAL_INT, FROM_OPTIONAL_INT);
    }
    
    private static final Function<Optional<Double>, OptionalDouble> TO_OPTIONAL_DOUBLE = doubleOpt -> doubleOpt.map(OptionalDouble::of).orElseGet(OptionalDouble::empty);
    private static final Function<OptionalDouble, Optional<Double>> FROM_OPTIONAL_DOUBLE = doubleOpt -> doubleOpt.isPresent() ? Optional.of(doubleOpt.getAsDouble()) : Optional.empty();
    public static MapCodec<OptionalDouble> asOptionalDouble(MapCodec<Optional<Double>> codec) {
        return codec.xmap(TO_OPTIONAL_DOUBLE, FROM_OPTIONAL_DOUBLE);
    }
}
