package com.verdantartifice.primalmagick.datagen.models;

import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public record ModelConnectionSet(String name, List<ModelConnection> modelConnections, BiFunction<Block, Map<ModelConnection, MultiVariant>, BlockModelDefinitionGenerator> generatorFactory) {
}
