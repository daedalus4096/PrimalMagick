package com.verdantartifice.primalmagic.common.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.verdantartifice.primalmagic.PrimalMagic;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

/**
 * Collection of utility methods pertaining to VoxelShapes.
 * 
 * @author Daedalus4096
 */
public class VoxelShapeUtils {
    private static final int HISTORY_LIMIT = 100;
    
    /**
     * Calculate a VoxelShape defined by the specified model file.
     * 
     * @param location the resource location of the model file
     * @return a VoxelShape defined by the specified model file, empty if calculation failed
     */
    @Nonnull
    public static VoxelShape fromModel(@Nullable ResourceLocation location) {
        return fromModel(location, new ArrayList<>());
    }
    
    /**
     * Calculate a VoxelShape defined by the specified model file.
     * 
     * @param location the resource location of the model file
     * @param history history of parent model files parsed, to prevent cycles
     * @return a VoxelShape defined by the specified model file, empty if calculation failed
     */
    @Nonnull
    protected static VoxelShape fromModel(@Nullable ResourceLocation location, @Nonnull List<ResourceLocation> history) {
        if (location == null) {
            return VoxelShapes.empty();
        }
        
        // Prevent cycles in model search
        if (history.contains(location)) {
            PrimalMagic.LOGGER.warn("Cycle detected while getting VoxelShape from model file: {}", location.toString());
            return VoxelShapes.empty();
        }
        history.add(location);
        if (history.size() >= HISTORY_LIMIT) {
            PrimalMagic.LOGGER.warn("History limit exceeded while getting VoxelShape from model file: {}", location.toString());
            return VoxelShapes.empty();
        }
        
        // Load the specified model file as a stream
        String locStr = "/assets/" + location.getNamespace() + "/models/" + location.getPath();
        if (!locStr.endsWith(".json")) {
            locStr += ".json";
        }
        InputStream stream = VoxelShapeUtils.class.getResourceAsStream(locStr);
        if (stream != null) {
            try {
                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(new InputStreamReader(stream)).getAsJsonObject();
                if (obj.has("elements")) {
                    // Parse the elements defined in the specified model file
                    List<VoxelShape> shapes = new ArrayList<>();
                    JsonArray elements = obj.getAsJsonArray("elements");
                    for (JsonElement element : elements) {
                        shapes.add(fromModelElement(location, element.getAsJsonObject()));
                    }
                    
                    // Combine the parsed shapes
                    if (shapes.isEmpty()) {
                        return VoxelShapes.empty();
                    } else if (shapes.size() == 1) {
                        return shapes.get(0);
                    } else {
                        VoxelShape firstShape = shapes.remove(0);
                        return VoxelShapes.or(firstShape, shapes.toArray(new VoxelShape[shapes.size()]));
                    }
                } else if (obj.has("parent")) {
                    // Attempt to load and parse the parent model file
                    return fromModel(new ResourceLocation(obj.getAsJsonPrimitive("parent").getAsString()), history);
                } else {
                    PrimalMagic.LOGGER.warn("No elements or parent found in VoxelShape model file: {}", location.toString());
                    return VoxelShapes.empty();
                }
            } catch (Exception e) {
                PrimalMagic.LOGGER.warn("Invalid VoxelShape model file: {}", location.toString());
                return VoxelShapes.empty();
            }
        } else {
            PrimalMagic.LOGGER.warn("VoxelShape model file not found: {}", location.toString());
            return VoxelShapes.empty();
        }
    }
    
    /**
     * Calculate a VoxelShape defined by the given element in the specified model file.
     * 
     * @param location the resource location of the model file
     * @param obj the model element to parse
     * @return a VoxelShape defined by the specified model element, empty if calculation failed
     */
    @Nonnull
    protected static VoxelShape fromModelElement(@Nonnull ResourceLocation location, @Nonnull JsonObject obj) {
        try {
            JsonArray fromArray = obj.getAsJsonArray("from");
            JsonArray toArray = obj.getAsJsonArray("to");
            return Block.makeCuboidShape(
                fromArray.get(0).getAsDouble(), 
                fromArray.get(1).getAsDouble(), 
                fromArray.get(2).getAsDouble(), 
                toArray.get(0).getAsDouble(), 
                toArray.get(1).getAsDouble(), 
                toArray.get(2).getAsDouble()
            );
        } catch (Exception e) {
            PrimalMagic.LOGGER.warn("Invalid element in VoxelShape model file: {}", location.toString());
            return VoxelShapes.empty();
        }
    }
}
