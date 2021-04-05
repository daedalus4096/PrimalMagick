package com.verdantartifice.primalmagic.datagen.theorycrafting;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

public class ProjectProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger();
    protected final DataGenerator generator;
    
    public ProjectProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Path path = this.generator.getOutputFolder();
        Map<ResourceLocation, IFinishedProject> map = new HashMap<>();
        this.registerProjects((project) -> {
            if (map.put(project.getId(), project) != null) {
                LOGGER.debug("Duplicate theorycrafting project in data generation: {}", project.getId().toString());
            }
        });
        for (Map.Entry<ResourceLocation, IFinishedProject> entry : map.entrySet()) {
            this.saveProject(cache, entry.getValue().getProjectJson(), path.resolve("data/" + entry.getKey().getNamespace() + "/theorycrafting/" + entry.getKey().getPath() + ".json"));
        }
    }

    private void saveProject(DirectoryCache cache, JsonObject json, Path path) {
        try {
            String jsonStr = GSON.toJson((JsonElement)json);
            String hash = HASH_FUNCTION.hashUnencodedChars(jsonStr).toString();
            if (!Objects.equals(cache.getPreviousHash(path), hash) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    writer.write(jsonStr);
                }
            }
            cache.recordHash(path, hash);
        } catch (IOException e) {
            LOGGER.error("Couldn't save theorycrafting project {}", path, e);
        }
    }
    
    protected void registerProjects(Consumer<IFinishedProject> consumer) {
        // TODO Method stub
        
    }

    @Override
    public String getName() {
        return "Primal Magic Theorycrafting Projects";
    }
}
