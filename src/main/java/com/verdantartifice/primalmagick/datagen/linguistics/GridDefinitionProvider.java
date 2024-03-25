package com.verdantartifice.primalmagick.datagen.linguistics;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.research.KnowledgeType;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class GridDefinitionProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final PackOutput packOutput;
    
    public GridDefinitionProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
        Map<ResourceLocation, IFinishedGrid> map = new HashMap<>();
        this.registerGrids(gridDef -> {
            if (map.put(gridDef.getId(), gridDef) != null) {
                LOGGER.debug("Duplicate linguistics grid definition in data generation: {}", gridDef.getId().toString());
            }
        });
        map.entrySet().forEach(entry -> {
            futuresBuilder.add(DataProvider.saveStable(pOutput, entry.getValue().getGridJson(), this.getPath(this.packOutput, entry.getKey())));
        });
        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    private Path getPath(PackOutput output, ResourceLocation entryLoc) {
        return output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(entryLoc.getNamespace()).resolve("linguistics_grids").resolve(entryLoc.getPath() + ".json");
    }
    
    protected void registerGrids(Consumer<IFinishedGrid> consumer) {
        // TODO Define remaining language grids
        GridDefinitionBuilder.grid("earth").language(BookLanguagesPM.EARTH.get()).startPos(3, 7)
                .node(GridNodeDefinitionBuilder.node(3, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 1).cost(1).reward(AttunementRewardBuilder.reward(Source.EARTH).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 2).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 3).cost(1).reward(AttunementRewardBuilder.reward(Source.EARTH).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 3).cost(1).reward(AttunementRewardBuilder.reward(Source.EARTH).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 4).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 5).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 5).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 5).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 5).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 6).cost(1).reward(AttunementRewardBuilder.reward(Source.EARTH).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 7).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 7).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 7).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.EARTH.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 7).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 7).cost(1).reward(AttunementRewardBuilder.reward(Source.EARTH).points(1).build()).build())
                .build(consumer);
        GridDefinitionBuilder.grid("sea").language(BookLanguagesPM.SEA.get()).startPos(0, 7)
                .node(GridNodeDefinitionBuilder.node(4, 1).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 3).cost(1).reward(AttunementRewardBuilder.reward(Source.SEA).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 3).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 4).cost(1).reward(AttunementRewardBuilder.reward(Source.SEA).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 4).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 5).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 5).cost(1).reward(AttunementRewardBuilder.reward(Source.SEA).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 5).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 6).cost(1).reward(AttunementRewardBuilder.reward(Source.SEA).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 7).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 7).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SEA.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 7).cost(1).reward(AttunementRewardBuilder.reward(Source.SEA).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 7).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .build(consumer);
        GridDefinitionBuilder.grid("sky").language(BookLanguagesPM.SKY.get()).startPos(3, 3)
                .node(GridNodeDefinitionBuilder.node(1, 0).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 0).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 0).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 1).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 1).cost(1).reward(AttunementRewardBuilder.reward(Source.SKY).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 1).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 2).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 2).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 2).cost(1).reward(AttunementRewardBuilder.reward(Source.SKY).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 3).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 4).cost(1).reward(AttunementRewardBuilder.reward(Source.SKY).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 4).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 5).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 5).cost(1).reward(AttunementRewardBuilder.reward(Source.SKY).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SKY.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 7).cost(1).reward(AttunementRewardBuilder.reward(Source.SKY).points(1).build()).build())
                .build(consumer);
        GridDefinitionBuilder.grid("sun").language(BookLanguagesPM.SUN.get()).startPos(3, 3)
                .node(GridNodeDefinitionBuilder.node(3, 0).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 0).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 1).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 1).cost(1).reward(AttunementRewardBuilder.reward(Source.SUN).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 1).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 2).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 3).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 3).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 3).cost(1).reward(AttunementRewardBuilder.reward(Source.SUN).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 3).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 4).cost(1).reward(AttunementRewardBuilder.reward(Source.SUN).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 4).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 4).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 5).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 5).cost(1).reward(AttunementRewardBuilder.reward(Source.SUN).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 6).cost(1).reward(AttunementRewardBuilder.reward(Source.SUN).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.SUN.get()).points(4).build()).build())
                .build(consumer);
        GridDefinitionBuilder.grid("moon").language(BookLanguagesPM.MOON.get()).startPos(0, 3)
                .node(GridNodeDefinitionBuilder.node(2, 0).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 0).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 0).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 0).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 1).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 1).cost(1).reward(AttunementRewardBuilder.reward(Source.MOON).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 1).cost(1).reward(AttunementRewardBuilder.reward(Source.MOON).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 1).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 2).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 3).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 4).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 4).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 5).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 6).cost(1).reward(AttunementRewardBuilder.reward(Source.MOON).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 6).cost(1).reward(AttunementRewardBuilder.reward(Source.MOON).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 6).cost(1).reward(AttunementRewardBuilder.reward(Source.MOON).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 7).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.MOON.get()).points(4).build()).build())
                .build(consumer);
        GridDefinitionBuilder.grid("trade").language(BookLanguagesPM.TRADE.get()).startPos(4, 3)
                .node(GridNodeDefinitionBuilder.node(1, 0).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 0).cost(1).reward(AttunementRewardBuilder.reward(Source.SUN).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 0).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 0).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 0).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 0).cost(1).reward(AttunementRewardBuilder.reward(Source.MOON).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 0).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 1).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 2).cost(1).reward(AttunementRewardBuilder.reward(Source.SKY).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 2).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 2).cost(1).reward(AttunementRewardBuilder.reward(Source.SEA).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(7, 2).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 3).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 3).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 3).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 4).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.OBSERVATION).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 4).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 5).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(2).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 5).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(0, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 6).cost(1).reward(AttunementRewardBuilder.reward(Source.EARTH).points(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 6).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(6, 6).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(1, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(4).build()).build())
                .node(GridNodeDefinitionBuilder.node(2, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(5).build()).build())
                .node(GridNodeDefinitionBuilder.node(3, 7).cost(1).reward(KnowledgeRewardBuilder.reward(KnowledgeType.THEORY).levels(1).build()).build())
                .node(GridNodeDefinitionBuilder.node(4, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(3).build()).build())
                .node(GridNodeDefinitionBuilder.node(5, 7).cost(1).reward(ComprehensionRewardBuilder.reward(BookLanguagesPM.TRADE.get()).points(4).build()).build())
                .build(consumer);
    }
    
    @Override
    public String getName() {
        return "Primal Magick Linguistics Grids";
    }
}
