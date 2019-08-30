package com.verdantartifice.primalmagic.common.research;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.util.ResourceLocation;

public class ResearchManager {
    public static void parseAllResearch() {
        JsonParser parser = new JsonParser();
        for (ResourceLocation location : ResearchDisciplines.getAllDataFileLocations()) {
            String locStr = "/data/" + location.getNamespace() + "/" + location.getPath();
            if (!locStr.endsWith(".json")) {
                locStr += ".json";
            }
            InputStream stream = ResearchManager.class.getResourceAsStream(locStr);
            if (stream != null) {
                try {
                    JsonObject obj = parser.parse(new InputStreamReader(stream)).getAsJsonObject();
                    JsonArray entries = obj.get("entries").getAsJsonArray();
                    int index = 0;
                    for (JsonElement element : entries) {
                        try {
                            ResearchEntry entry = ResearchEntry.parse(element.getAsJsonObject());
                            ResearchDiscipline discipline = ResearchDisciplines.getDiscipline(entry.getDisciplineKey());
                            if (discipline == null || !discipline.addEntry(entry)) {
                                PrimalMagic.LOGGER.warn("Could not add invalid entry: {}", entry.getKey());
                            } else {
                                index++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrimalMagic.LOGGER.warn("Invalid research entry #{} found in {}", index, location.toString());
                        }
                    }
                    PrimalMagic.LOGGER.info("Loaded {} research entries from {}", index, location.toString());
                } catch (Exception e) {
                    PrimalMagic.LOGGER.warn("Invalid research file: {}", location.toString());
                }
            } else {
                PrimalMagic.LOGGER.warn("Research file not found: {}", location.toString());
            }
        }
    }
}
