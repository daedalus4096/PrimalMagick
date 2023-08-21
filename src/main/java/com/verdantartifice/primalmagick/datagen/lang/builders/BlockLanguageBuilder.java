package com.verdantartifice.primalmagick.datagen.lang.builders;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Helper for specifying block-related localizations in a structured way.
 * 
 * @author Daedalus4096
 */
public class BlockLanguageBuilder extends AbstractLanguageBuilder<Block, BlockLanguageBuilder> {
    public BlockLanguageBuilder(Block block, Consumer<ILanguageBuilder> untracker, BiConsumer<String, String> adder) {
        super(block, block::getDescriptionId, untracker, adder);
    }

    @Override
    public ResourceKey<?> getKey() {
        return ResourceKey.create(Registries.BLOCK, this.getBaseRegistryKey());
    }

    @Override
    protected ResourceLocation getBaseRegistryKey(Block base) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(base));
    }
}
