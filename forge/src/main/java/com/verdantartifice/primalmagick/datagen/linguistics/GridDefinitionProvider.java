package com.verdantartifice.primalmagick.datagen.linguistics;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.books.grids.GridDefinition;
import com.verdantartifice.primalmagick.common.books.grids.GridNodeDefinition;
import com.verdantartifice.primalmagick.common.books.grids.rewards.AttunementReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.ComprehensionReward;
import com.verdantartifice.primalmagick.common.books.grids.rewards.KnowledgeReward;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.sources.Sources;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class GridDefinitionProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final PackOutput packOutput;
    protected final CompletableFuture<HolderLookup.Provider> lookupProviderFuture;
    
    public GridDefinitionProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProviderFuture) {
        this.packOutput = packOutput;
        this.lookupProviderFuture = lookupProviderFuture;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        return this.lookupProviderFuture.thenCompose(p -> {
            ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
            Map<ResourceLocation, GridDefinition> map = new HashMap<>();
            this.registerGrids(p, gridDef -> {
                if (map.put(gridDef.getKey(), gridDef) != null) {
                    LOGGER.debug("Duplicate linguistics grid definition in data generation: {}", gridDef.getKey().toString());
                }
            });
            map.entrySet().forEach(entry -> {
                GridDefinition.codec().encodeStart(JsonOps.INSTANCE, entry.getValue()).resultOrPartial(err -> {
                    LOGGER.error("Failed to encode grid {}: {}", entry.getKey(), err);
                }).ifPresent(encodedVal -> {
                    futuresBuilder.add(DataProvider.saveStable(pOutput, encodedVal, this.getPath(this.packOutput, entry.getKey())));
                });
            });
            return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
        });
    }

    private Path getPath(PackOutput output, ResourceLocation entryLoc) {
        return output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(entryLoc.getNamespace()).resolve("linguistics_grids").resolve(entryLoc.getPath() + ".json");
    }
    
    protected void registerGrids(HolderLookup.Provider lookupProvider, Consumer<GridDefinition> consumer) {
        consumer.accept(GridDefinition.Builder.grid("earth", lookupProvider).language(BookLanguagesPM.EARTH).startPos(3, 7)
                .node(3, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(5).build()).build())
                .node(4, 1, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.EARTH).points(1).build()).build())
                .node(1, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(5).build()).build())
                .node(4, 2, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(5, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(3).build()).build())
                .node(6, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(4).build()).build())
                .node(0, 3, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.EARTH).points(1).build()).build())
                .node(1, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(2).build()).build())
                .node(2, 3, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.EARTH).points(1).build()).build())
                .node(5, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(4).build()).build())
                .node(7, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(5).build()).build())
                .node(0, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(3).build()).build())
                .node(2, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(3).build()).build())
                .node(4, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(4).build()).build())
                .node(5, 4, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(6, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(3).build()).build())
                .node(7, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(4).build()).build())
                .node(0, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(1, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(2).build()).build())
                .node(2, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(3, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(5).build()).build())
                .node(4, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(5, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(2).build()).build())
                .node(7, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(0, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(1).build()).build())
                .node(1, 6, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.EARTH).points(1).build()).build())
                .node(5, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(7, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(2).build()).build())
                .node(0, 7, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(1, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(1).build()).build())
                .node(2, 7, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(3, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(1).build()).build())
                .node(4, 7, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(5, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.EARTH).points(1).build()).build())
                .node(6, 7, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(7, 7, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.EARTH).points(1).build()).build())
                .build());
        consumer.accept(GridDefinition.Builder.grid("sea", lookupProvider).language(BookLanguagesPM.SEA).startPos(0, 7)
                .node(4, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(5, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(4).build()).build())
                .node(6, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(1).build()).build())
                .node(3, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(5).build()).build())
                .node(4, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(4).build()).build())
                .node(6, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(5).build()).build())
                .node(2, 3, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SEA).points(1).build()).build())
                .node(3, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(4, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(4).build()).build())
                .node(1, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(2).build()).build())
                .node(2, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(3).build()).build())
                .node(3, 4, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SEA).points(1).build()).build())
                .node(4, 4, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(5, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(5).build()).build())
                .node(1, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(2, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(2).build()).build())
                .node(3, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(3).build()).build())
                .node(4, 5, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SEA).points(1).build()).build())
                .node(5, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(6, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(4).build()).build())
                .node(0, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(1, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(1).build()).build())
                .node(2, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(3, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(2).build()).build())
                .node(4, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(3).build()).build())
                .node(5, 6, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SEA).points(1).build()).build())
                .node(6, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(5).build()).build())
                .node(0, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(1).build()).build())
                .node(1, 7, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(2, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(1).build()).build())
                .node(3, 7, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(4, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(2).build()).build())
                .node(5, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SEA).points(3).build()).build())
                .node(6, 7, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SEA).points(1).build()).build())
                .node(7, 7, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .build());
        consumer.accept(GridDefinition.Builder.grid("sky", lookupProvider).language(BookLanguagesPM.SKY).startPos(3, 3)
                .node(1, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(4).build()).build())
                .node(2, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(3).build()).build())
                .node(6, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(5).build()).build())
                .node(0, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(5).build()).build())
                .node(1, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(2, 1, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SKY).points(1).build()).build())
                .node(3, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(1).build()).build())
                .node(6, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(4).build()).build())
                .node(2, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(2).build()).build())
                .node(3, 2, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(5, 2, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(6, 2, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SKY).points(1).build()).build())
                .node(7, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(3).build()).build())
                .node(3, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(1).build()).build())
                .node(4, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(5, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(1).build()).build())
                .node(6, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(2).build()).build())
                .node(1, 4, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SKY).points(1).build()).build())
                .node(2, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(2).build()).build())
                .node(3, 4, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(4, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(1).build()).build())
                .node(0, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(4).build()).build())
                .node(1, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(2, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(3).build()).build())
                .node(4, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(2).build()).build())
                .node(5, 5, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SKY).points(1).build()).build())
                .node(0, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(1, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(4, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(3).build()).build())
                .node(5, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(6, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(5).build()).build())
                .node(1, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(5).build()).build())
                .node(5, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SKY).points(4).build()).build())
                .node(6, 7, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SKY).points(1).build()).build())
                .build());
        consumer.accept(GridDefinition.Builder.grid("sun", lookupProvider).language(BookLanguagesPM.SUN).startPos(3, 3)
                .node(3, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(4).build()).build())
                .node(6, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(5).build()).build())
                .node(0, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(5).build()).build())
                .node(1, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(3, 1, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SUN).points(1).build()).build())
                .node(5, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(3).build()).build())
                .node(6, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(1, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(3).build()).build())
                .node(2, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(1).build()).build())
                .node(3, 2, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(4, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(1).build()).build())
                .node(5, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(2).build()).build())
                .node(2, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(3, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(1).build()).build())
                .node(4, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(5, 3, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SUN).points(1).build()).build())
                .node(6, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(4).build()).build())
                .node(0, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(4).build()).build())
                .node(1, 4, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SUN).points(1).build()).build())
                .node(2, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(1).build()).build())
                .node(3, 4, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(4, 4, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(5, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(2).build()).build())
                .node(2, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(2).build()).build())
                .node(3, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(4, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(2).build()).build())
                .node(5, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(3).build()).build())
                .node(6, 5, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SUN).points(1).build()).build())
                .node(1, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(2, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(3).build()).build())
                .node(4, 6, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SUN).points(1).build()).build())
                .node(6, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(5).build()).build())
                .node(1, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(5).build()).build())
                .node(4, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.SUN).points(4).build()).build())
                .build());
        consumer.accept(GridDefinition.Builder.grid("moon", lookupProvider).language(BookLanguagesPM.MOON).startPos(0, 3)
                .node(2, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(3).build()).build())
                .node(3, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(5).build()).build())
                .node(4, 0, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(5, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(4).build()).build())
                .node(1, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(2).build()).build())
                .node(2, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(3, 1, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.MOON).points(1).build()).build())
                .node(4, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(3).build()).build())
                .node(5, 1, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.MOON).points(1).build()).build())
                .node(6, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(5).build()).build())
                .node(0, 2, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(1, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(1).build()).build())
                .node(2, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(2).build()).build())
                .node(3, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(4).build()).build())
                .node(0, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(1).build()).build())
                .node(1, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(2, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(1).build()).build())
                .node(0, 4, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(1, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(1).build()).build())
                .node(2, 4, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(0, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(2).build()).build())
                .node(1, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(2, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(2).build()).build())
                .node(3, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(4).build()).build())
                .node(1, 6, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.MOON).points(1).build()).build())
                .node(2, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(3, 6, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.MOON).points(1).build()).build())
                .node(4, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(3).build()).build())
                .node(5, 6, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.MOON).points(1).build()).build())
                .node(6, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(5).build()).build())
                .node(2, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(3).build()).build())
                .node(3, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(5).build()).build())
                .node(4, 7, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(5, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.MOON).points(4).build()).build())
                .build());
        consumer.accept(GridDefinition.Builder.grid("trade", lookupProvider).language(BookLanguagesPM.TRADE).startPos(4, 3)
                .node(1, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(5).build()).build())
                .node(2, 0, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SUN).points(1).build()).build())
                .node(3, 0, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(4, 0, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(5, 0, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(6, 0, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.MOON).points(1).build()).build())
                .node(7, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(5).build()).build())
                .node(3, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(2).build()).build())
                .node(5, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(2).build()).build())
                .node(1, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(3).build()).build())
                .node(2, 2, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SKY).points(1).build()).build())
                .node(3, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(1).build()).build())
                .node(4, 2, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(5, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(1).build()).build())
                .node(6, 2, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.SEA).points(1).build()).build())
                .node(7, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(3).build()).build())
                .node(3, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(4, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(1).build()).build())
                .node(5, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(4, 4, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(5, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(1).build()).build())
                .node(6, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(2).build()).build())
                .node(0, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(5).build()).build())
                .node(5, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(2).build()).build())
                .node(6, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(0, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(1, 6, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.EARTH).points(1).build()).build())
                .node(2, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(4, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(4).build()).build())
                .node(5, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(3).build()).build())
                .node(6, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(4).build()).build())
                .node(1, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(4).build()).build())
                .node(2, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(5).build()).build())
                .node(3, 7, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(4, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(3).build()).build())
                .node(5, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.TRADE).points(4).build()).build())
                .build());
        consumer.accept(GridDefinition.Builder.grid("forbidden", lookupProvider).language(BookLanguagesPM.FORBIDDEN).startPos(1, 0)
                .node(1, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(1).build()).build())
                .node(2, 0, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(5, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(3).build()).build())
                .node(6, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(5).build()).build())
                .node(0, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(1).build()).build())
                .node(1, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(2, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(1).build()).build())
                .node(3, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(2).build()).build())
                .node(4, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(1).build()).build())
                .node(5, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(2).build()).build())
                .node(6, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(4).build()).build())
                .node(7, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(3).build()).build())
                .node(0, 2, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.BLOOD).points(1).build()).build())
                .node(1, 2, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(3, 2, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.INFERNAL).points(1).build()).build())
                .node(4, 2, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.INFERNAL).points(1).build()).build())
                .node(6, 2, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 2, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.BLOOD).points(1).build()).build())
                .node(1, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(2).build()).build())
                .node(2, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(3, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(5).build()).build())
                .node(4, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(4).build()).build())
                .node(5, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(6, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(2).build()).build())
                .node(3, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(3).build()).build())
                .node(4, 4, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(3, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(4).build()).build())
                .node(4, 5, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.VOID).points(1).build()).build())
                .node(5, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(6, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(5).build()).build())
                .node(3, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(3).build()).build())
                .node(4, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(3, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(4).build()).build())
                .node(4, 7, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.VOID).points(1).build()).build())
                .node(5, 7, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(6, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.FORBIDDEN).points(5).build()).build())
                .build());
        consumer.accept(GridDefinition.Builder.grid("hallowed", lookupProvider).language(BookLanguagesPM.HALLOWED).startPos(3, 1)
                .node(0, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(5).build()).build())
                .node(7, 0, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(5).build()).build())
                .node(0, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(2, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(3, 1, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(1).build()).build())
                .node(4, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(5, 1, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.HALLOWED).points(1).build()).build())
                .node(7, 1, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(0, 2, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.HALLOWED).points(1).build()).build())
                .node(1, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(2).build()).build())
                .node(2, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(1).build()).build())
                .node(5, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(1).build()).build())
                .node(6, 2, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(2).build()).build())
                .node(7, 2, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.HALLOWED).points(1).build()).build())
                .node(0, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(3).build()).build())
                .node(1, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(1).build()).build())
                .node(6, 3, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 3, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(3).build()).build())
                .node(0, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(4).build()).build())
                .node(1, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(2).build()).build())
                .node(6, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(2).build()).build())
                .node(7, 4, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(4).build()).build())
                .node(0, 5, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.HALLOWED).points(1).build()).build())
                .node(1, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(2, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(3).build()).build())
                .node(5, 5, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(3).build()).build())
                .node(6, 5, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 5, GridNodeDefinition.Builder.node().cost(1).reward(AttunementReward.Builder.reward(Sources.HALLOWED).points(1).build()).build())
                .node(0, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(2, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(3, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(4).build()).build())
                .node(4, 6, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(4).build()).build())
                .node(5, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(7, 6, GridNodeDefinition.Builder.node().cost(1).reward(KnowledgeReward.Builder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(0, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(5).build()).build())
                .node(7, 7, GridNodeDefinition.Builder.node().cost(1).reward(ComprehensionReward.Builder.reward(BookLanguagesPM.HALLOWED).points(5).build()).build())
                .build());
    }
    
    @Override
    public String getName() {
        return "Primal Magick Linguistics Grids";
    }
}
