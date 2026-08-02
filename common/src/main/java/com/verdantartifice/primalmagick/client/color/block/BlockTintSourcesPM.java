package com.verdantartifice.primalmagick.client.color.block;

import com.verdantartifice.primalmagick.common.blocks.base.IHasTintColor;
import com.verdantartifice.primalmagick.common.blocks.rituals.SaltTrailBlock;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BlockTintSourcesPM {
    public static BlockTintSource salt() {
        return new BlockTintSource() {
            @Override
            public int color(@NotNull BlockState state) {
                return SaltTrailBlock.colorMultiplier(state.getValue(SaltTrailBlock.POWER));
            }

            @Override
            @NotNull
            public Set<Property<?>> relevantProperties() {
                return Set.of(SaltTrailBlock.POWER);
            }
        };
    }

    public static BlockTintSource tinted() {
        return state -> state.getBlock() instanceof IHasTintColor tintBlock ? tintBlock.getColor().getFireworkColor() : DyeColor.WHITE.getFireworkColor();
    }
}
