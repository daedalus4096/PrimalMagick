package com.verdantartifice.primalmagick.datagen.books;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.client.books.StyleGuide;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.sources.Source;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class StyleGuideProvider implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    protected final PackOutput packOutput;
    
    public StyleGuideProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        ImmutableList.Builder<CompletableFuture<?>> futuresBuilder = new ImmutableList.Builder<>();
        Map<ResourceLocation, StyleGuide> map = new HashMap<>();
        this.registerStyleGuides((langId, guide) -> {
            if (map.put(langId, guide) != null) {
                LOGGER.warn("Duplicate style guide in data generation: {}", langId.toString());
            }
        });
        map.entrySet().forEach(entry -> {
            StyleGuide.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue())
                    .resultOrPartial(LOGGER::error)
                    .ifPresent(json -> futuresBuilder.add(DataProvider.saveStable(pOutput, json, this.getPath(this.packOutput, entry.getKey()))));
        });
        return CompletableFuture.allOf(futuresBuilder.build().toArray(CompletableFuture[]::new));
    }

    private Path getPath(PackOutput output, ResourceLocation entryLoc) {
        return output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve(entryLoc.getNamespace()).resolve("style_guides").resolve(entryLoc.getPath() + ".json");
    }
    
    protected void registerStyleGuides(BiConsumer<ResourceLocation, StyleGuide> consumer) {
        StyleGuide.builder(BookLanguagesPM.DEFAULT.get()).save(consumer);   // No styling for default language
        StyleGuide.builder(BookLanguagesPM.GALACTIC.get()).save(consumer);  // No styling for galactic language
        StyleGuide.builder(BookLanguagesPM.ILLAGER.get())
                .entry(Source.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.EARTH.getColor())).end()
                .entry(Source.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SEA.getColor())).end()
                .entry(Source.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SKY.getColor())).end()
                .entry(Source.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SUN.getColor())).end()
                .entry(Source.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.MOON.getColor())).end()
                .entry(Source.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.HALLOWED.getColor())).end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.EARTH.get())
                .entry(Source.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.EARTH.getColor())).end()
                .entry(Source.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SEA.getColor())).end()
                .entry(Source.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SKY.getColor())).end()
                .entry(Source.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SUN.getColor())).end()
                .entry(Source.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.MOON.getColor())).end()
                .entry(Source.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.HALLOWED.getColor())).end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.SEA.get())
                .entry(Source.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.EARTH.getColor())).end()
                .entry(Source.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SEA.getColor())).end()
                .entry(Source.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SKY.getColor())).end()
                .entry(Source.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SUN.getColor())).end()
                .entry(Source.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.MOON.getColor())).end()
                .entry(Source.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.HALLOWED.getColor())).end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.SKY.get())
                .entry(Source.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.EARTH.getColor())).end()
                .entry(Source.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SEA.getColor())).end()
                .entry(Source.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SKY.getColor())).end()
                .entry(Source.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SUN.getColor())).end()
                .entry(Source.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.MOON.getColor())).end()
                .entry(Source.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.HALLOWED.getColor())).end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.SUN.get())
                .entry(Source.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.EARTH.getColor())).end()
                .entry(Source.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SEA.getColor())).end()
                .entry(Source.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SKY.getColor())).end()
                .entry(Source.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SUN.getColor())).end()
                .entry(Source.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.MOON.getColor())).end()
                .entry(Source.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.HALLOWED.getColor())).end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.MOON.get())
                .entry(Source.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.EARTH.getColor())).end()
                .entry(Source.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SEA.getColor())).end()
                .entry(Source.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SKY.getColor())).end()
                .entry(Source.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SUN.getColor())).end()
                .entry(Source.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.MOON.getColor())).end()
                .entry(Source.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.HALLOWED.getColor())).end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.TRADE.get())
                .entry(Source.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.EARTH.getColor())).end()
                .entry(Source.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SEA.getColor())).end()
                .entry(Source.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SKY.getColor())).end()
                .entry(Source.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SUN.getColor())).end()
                .entry(Source.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.MOON.getColor())).end()
                .entry(Source.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Source.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.HALLOWED.getColor())).end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.FORBIDDEN.get())
                .entry(Source.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.EARTH.getColor())).end()
                .entry(Source.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SEA.getColor())).end()
                .entry(Source.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SKY.getColor())).end()
                .entry(Source.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SUN.getColor())).end()
                .entry(Source.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.MOON.getColor())).end()
                .entry(Source.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.BLOOD.getColor())).end()
                .entry(Source.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.INFERNAL.getColor())).end()
                .entry(Source.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.VOID.getColor())).end()
                .entry(Source.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.HALLOWED.getColor()).withStrikethrough(true)).end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.HALLOWED.get())
                .entry(Source.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.EARTH.getColor())).end()
                .entry(Source.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SEA.getColor())).end()
                .entry(Source.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SKY.getColor())).end()
                .entry(Source.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.SUN.getColor())).end()
                .entry(Source.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.MOON.getColor())).end()
                .entry(Source.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.BLOOD.getColor())).end()
                .entry(Source.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.INFERNAL.getColor())).end()
                .entry(Source.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.VOID.getColor())).end()
                .entry(Source.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Source.HALLOWED.getColor())).end()
                .save(consumer);
    }
    
    @Override
    public String getName() {
        return "Primal Magick Style Guides";
    }

}
