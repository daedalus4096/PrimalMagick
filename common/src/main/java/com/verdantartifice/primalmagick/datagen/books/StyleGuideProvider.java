package com.verdantartifice.primalmagick.datagen.books;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.JsonOps;
import com.verdantartifice.primalmagick.client.books.StyleGuide;
import com.verdantartifice.primalmagick.common.books.BookLanguagesPM;
import com.verdantartifice.primalmagick.common.sources.Sources;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

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
        String illegibleKey = "tooltip.primalmagick.written_language.illegible_text";
        
        StyleGuide.builder(BookLanguagesPM.DEFAULT)
                .entry(illegibleKey).setStyle(Style.EMPTY.withObfuscated(true)).hoverText("tooltip.primalmagick.written_language.illegible_hover").end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.GALACTIC).save(consumer);    // No styling for galactic language
        StyleGuide.builder(BookLanguagesPM.ILLAGER).save(consumer);     // No styling for illager language
        StyleGuide.builder(BookLanguagesPM.BABELTONGUE).save(consumer); // No styling for babeltongue language
        StyleGuide.builder(BookLanguagesPM.EARTH)
                .entry(Sources.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.EARTH.getColor())).end()
                .entry(Sources.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SEA.getColor())).end()
                .entry(Sources.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SKY.getColor())).end()
                .entry(Sources.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SUN.getColor())).end()
                .entry(Sources.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.MOON.getColor())).end()
                .entry(Sources.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.HALLOWED.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(illegibleKey).setStyle(Style.EMPTY.withObfuscated(true)).hoverText("tooltip.primalmagick.written_language.illegible_hover").end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.SEA)
                .entry(Sources.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.EARTH.getColor())).end()
                .entry(Sources.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SEA.getColor())).end()
                .entry(Sources.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SKY.getColor())).end()
                .entry(Sources.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SUN.getColor())).end()
                .entry(Sources.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.MOON.getColor())).end()
                .entry(Sources.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.HALLOWED.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(illegibleKey).setStyle(Style.EMPTY.withObfuscated(true)).hoverText("tooltip.primalmagick.written_language.illegible_hover").end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.SKY)
                .entry(Sources.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.EARTH.getColor())).end()
                .entry(Sources.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SEA.getColor())).end()
                .entry(Sources.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SKY.getColor())).end()
                .entry(Sources.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SUN.getColor())).end()
                .entry(Sources.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.MOON.getColor())).end()
                .entry(Sources.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.HALLOWED.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(illegibleKey).setStyle(Style.EMPTY.withObfuscated(true)).hoverText("tooltip.primalmagick.written_language.illegible_hover").end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.SUN)
                .entry(Sources.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.EARTH.getColor())).end()
                .entry(Sources.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SEA.getColor())).end()
                .entry(Sources.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SKY.getColor())).end()
                .entry(Sources.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SUN.getColor())).end()
                .entry(Sources.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.MOON.getColor())).end()
                .entry(Sources.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.HALLOWED.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(illegibleKey).setStyle(Style.EMPTY.withObfuscated(true)).hoverText("tooltip.primalmagick.written_language.illegible_hover").end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.MOON)
                .entry(Sources.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.EARTH.getColor())).end()
                .entry(Sources.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SEA.getColor())).end()
                .entry(Sources.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SKY.getColor())).end()
                .entry(Sources.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SUN.getColor())).end()
                .entry(Sources.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.MOON.getColor())).end()
                .entry(Sources.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.HALLOWED.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(illegibleKey).setStyle(Style.EMPTY.withObfuscated(true)).hoverText("tooltip.primalmagick.written_language.illegible_hover").end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.TRADE)
                .entry(Sources.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.EARTH.getColor())).end()
                .entry(Sources.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SEA.getColor())).end()
                .entry(Sources.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SKY.getColor())).end()
                .entry(Sources.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SUN.getColor())).end()
                .entry(Sources.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.MOON.getColor())).end()
                .entry(Sources.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.BLOOD.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.INFERNAL.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.VOID.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(Sources.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.HALLOWED.getColor()).withObfuscated(true))
                    .hoverText("tooltip.primalmagick.written_language.obfuscated_word").end()
                .entry(illegibleKey).setStyle(Style.EMPTY.withObfuscated(true)).hoverText("tooltip.primalmagick.written_language.illegible_hover").end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.FORBIDDEN)
                .entry(Sources.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.EARTH.getColor())).end()
                .entry(Sources.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SEA.getColor())).end()
                .entry(Sources.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SKY.getColor())).end()
                .entry(Sources.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SUN.getColor())).end()
                .entry(Sources.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.MOON.getColor())).end()
                .entry(Sources.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.BLOOD.getColor())).end()
                .entry(Sources.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.INFERNAL.getColor())).end()
                .entry(Sources.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.VOID.getColor())).end()
                .entry(Sources.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.HALLOWED.getColor()).withStrikethrough(true)).end()
                .entry(illegibleKey).setStyle(Style.EMPTY.withObfuscated(true)).hoverText("tooltip.primalmagick.written_language.illegible_hover").end()
                .save(consumer);
        StyleGuide.builder(BookLanguagesPM.HALLOWED)
                .entry(Sources.EARTH.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.EARTH.getColor())).end()
                .entry(Sources.SEA.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SEA.getColor())).end()
                .entry(Sources.SKY.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SKY.getColor())).end()
                .entry(Sources.SUN.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.SUN.getColor())).end()
                .entry(Sources.MOON.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.MOON.getColor())).end()
                .entry(Sources.BLOOD.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.BLOOD.getColor())).end()
                .entry(Sources.INFERNAL.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.INFERNAL.getColor())).end()
                .entry(Sources.VOID.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.VOID.getColor())).end()
                .entry(Sources.HALLOWED.getNameTranslationKey()).setStyle(Style.EMPTY.withColor(Sources.HALLOWED.getColor())).end()
                .entry(illegibleKey).setStyle(Style.EMPTY.withObfuscated(true)).hoverText("tooltip.primalmagick.written_language.illegible_hover").end()
                .save(consumer);
    }
    
    @Override
    public String getName() {
        return "Primal Magick Style Guides";
    }

}
