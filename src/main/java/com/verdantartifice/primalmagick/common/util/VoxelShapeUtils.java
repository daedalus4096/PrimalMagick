package com.verdantartifice.primalmagick.common.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Collection of utility methods pertaining to VoxelShapes.
 * 
 * @author Daedalus4096
 */
public class VoxelShapeUtils {
    private static final int HISTORY_LIMIT = 100;
    private static final Logger LOGGER = LogManager.getLogger();

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
            return Shapes.empty();
        }
        
        // Prevent cycles in model search
        if (history.contains(location)) {
            LOGGER.warn("Cycle detected while getting VoxelShape from model file: {}", location.toString());
            return Shapes.empty();
        }
        history.add(location);
        if (history.size() >= HISTORY_LIMIT) {
            LOGGER.warn("History limit exceeded while getting VoxelShape from model file: {}", location.toString());
            return Shapes.empty();
        }
        
        // Load the specified model file as a stream
        String locStr = "/assets/" + location.getNamespace() + "/models/" + location.getPath();
        if (!locStr.endsWith(".json")) {
            locStr += ".json";
        }
        InputStream stream = VoxelShapeUtils.class.getResourceAsStream(locStr);
        if (stream != null) {
            try {
                JsonObject obj = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
                if (obj.has("elements")) {
                    // Parse the elements defined in the specified model file
                    List<VoxelShape> shapes = new ArrayList<>();
                    JsonArray elements = obj.getAsJsonArray("elements");
                    for (JsonElement element : elements) {
                        shapes.add(fromModelElement(location, element.getAsJsonObject()));
                    }
                    
                    // Combine the parsed shapes
                    if (shapes.isEmpty()) {
                        return Shapes.empty();
                    } else if (shapes.size() == 1) {
                        return shapes.get(0);
                    } else {
                        VoxelShape firstShape = shapes.remove(0);
                        return Shapes.or(firstShape, shapes.toArray(new VoxelShape[shapes.size()]));
                    }
                } else if (obj.has("parent")) {
                    // Attempt to load and parse the parent model file
                    return fromModel(ResourceLocation.parse(obj.getAsJsonPrimitive("parent").getAsString()), history);
                } else {
                    LOGGER.warn("No elements or parent found in VoxelShape model file: {}", location.toString());
                    return Shapes.empty();
                }
            } catch (Exception e) {
                LOGGER.warn("Invalid VoxelShape model file: {}", location.toString());
                return Shapes.empty();
            }
        } else {
            LOGGER.warn("VoxelShape model file not found: {}", location.toString());
            return Shapes.empty();
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
            return Block.box(
                fromArray.get(0).getAsDouble(), 
                fromArray.get(1).getAsDouble(), 
                fromArray.get(2).getAsDouble(), 
                toArray.get(0).getAsDouble(), 
                toArray.get(1).getAsDouble(), 
                toArray.get(2).getAsDouble()
            );
        } catch (Exception e) {
            LOGGER.warn("Invalid element in VoxelShape model file: {}", location.toString());
            return Shapes.empty();
        }
    }
    
    /**
     * Rotate a VoxelShape around a given axis.
     * 
     * @param shape the VoxelShape to be rotated
     * @param axis the axis around which the VoxelShape is to be rotated 
     * @param rot the degree of rotation to be applied
     * @return the rotated VoxelShape
     */
    @Nonnull
    public static VoxelShape rotate(@Nullable VoxelShape shape, @Nullable Direction.Axis axis, @Nullable Rotation rot) {
        if (shape == null) {
            return Shapes.empty();
        } else if (axis == null || rot == null) {
            return shape;
        }
        
        VoxelShape[] buffer = new VoxelShape[] { shape, Shapes.empty() };
        
        int ccwRotations = (4 - rot.ordinal()) % 4;
        for (int index = 0; index < ccwRotations; index++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
                VoxelShape newBox;
                switch (axis) {
                case X:
                    newBox = Shapes.box(minX, minZ, 1-maxY, maxX, maxZ, 1-minY);
                    break;
                case Y:
                    newBox = Shapes.box(1-maxZ, minY, minX, 1-minZ, maxY, maxX);
                    break;
                case Z:
                    newBox = Shapes.box(minY, 1-maxX, minZ, maxY, 1-minX, maxZ);
                    break;
                default:
                    throw new Error("Invalid axis in voxel shape rotation!");
                }
                buffer[1] = Shapes.or(buffer[1], newBox);
            });
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        
        return buffer[0];
    }
}
