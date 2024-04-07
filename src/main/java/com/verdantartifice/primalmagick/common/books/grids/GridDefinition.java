package com.verdantartifice.primalmagick.common.books.grids;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nullable;

import org.joml.Vector2i;
import org.joml.Vector2ic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.verdantartifice.primalmagick.common.books.BookLanguage;
import com.verdantartifice.primalmagick.common.registries.RegistryKeysPM;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Class encapsulating a data-defined definition for a linguistics grid.  These definitions determine
 * the layout and contents of the linguistics comprehension grids for each ancient language.  Node
 * coordinates increase right and down, with (0,0) being in the top-left corner to match screen coords.
 * 
 * @author Daedalus4096
 */
public class GridDefinition implements INBTSerializable<CompoundTag> {
    public static final IGridDefinitionSerializer SERIALIZER = new Serializer();
    public static final int MIN_POS = 0;
    public static final int MAX_POS = 7;
    
    protected ResourceLocation key;
    protected ResourceKey<BookLanguage> language;
    protected Vector2ic startPos;
    protected final Map<Vector2ic, GridNodeDefinition> nodes = new HashMap<>();
    
    private GridDefinition() {}
    
    protected GridDefinition(ResourceLocation key, ResourceKey<BookLanguage> language, Vector2ic startPos, Map<Vector2ic, GridNodeDefinition> nodes) {
        this.key = key;
        this.language = language;
        this.startPos = startPos;
        this.nodes.putAll(nodes);
    }
    
    @Nullable
    public static GridDefinition fromNBT(CompoundTag tag) {
        if (tag == null) {
            return null;
        } else {
            GridDefinition retVal = new GridDefinition();
            retVal.deserializeNBT(tag);
            return retVal;
        }
    }
    
    public ResourceLocation getKey() {
        return this.key;
    }
    
    public ResourceKey<BookLanguage> getLanguage() {
        return this.language;
    }
    
    public Vector2ic getStartPos() {
        return this.startPos;
    }
    
    public Map<Vector2ic, GridNodeDefinition> getNodes() {
        return Collections.unmodifiableMap(this.nodes);
    }
    
    public boolean isValidPos(int x, int y) {
        return this.isValidPos(new Vector2i(x, y));
    }
    
    public boolean isValidPos(Vector2ic pos) {
        return this.nodes.containsKey(pos);
    }
    
    public Optional<GridNodeDefinition> getNode(int x, int y) {
        return this.getNode(new Vector2i(x, y));
    }
    
    public Optional<GridNodeDefinition> getNode(Vector2ic pos) {
        return this.isValidPos(pos) ? Optional.ofNullable(this.nodes.get(pos)) : Optional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag retVal = new CompoundTag();
        retVal.putString("Key", this.key.toString());
        retVal.putString("Language", this.language.location().toString());
        retVal.putInt("StartX", this.startPos.x());
        retVal.putInt("StartY", this.startPos.y());
        
        ListTag nodeListTag = new ListTag();
        for (Map.Entry<Vector2ic, GridNodeDefinition> entry : this.nodes.entrySet()) {
            CompoundTag nodeTag = new CompoundTag();
            nodeTag.putInt("PosX", entry.getKey().x());
            nodeTag.putInt("PosY", entry.getKey().y());
            nodeTag.put("NodeDef", entry.getValue().serializeNBT());
            nodeListTag.add(nodeTag);
        }
        retVal.put("Nodes", nodeListTag);
        
        return retVal;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.key = new ResourceLocation(nbt.getString("Key"));
        this.language = ResourceKey.create(RegistryKeysPM.BOOK_LANGUAGES, new ResourceLocation(nbt.getString("Language")));
        this.startPos = new Vector2i(nbt.getInt("StartX"), nbt.getInt("StartY"));
        
        this.nodes.clear();
        ListTag nodeListTag = nbt.getList("Nodes", Tag.TAG_COMPOUND);
        for (int index = 0; index < nodeListTag.size(); index++) {
            CompoundTag nodeTag = nodeListTag.getCompound(index);
            this.nodes.put(new Vector2i(nodeTag.getInt("PosX"), nodeTag.getInt("PosY")), GridNodeDefinition.fromNBT(nodeTag.getCompound("NodeDef")));
        }
    }
    
    public static class Serializer implements IGridDefinitionSerializer {
        @Override
        public GridDefinition read(ResourceLocation gridId, JsonObject json) {
            ResourceLocation key = new ResourceLocation(json.getAsJsonPrimitive("key").getAsString());
            ResourceKey<BookLanguage> langKey = ResourceKey.create(RegistryKeysPM.BOOK_LANGUAGES, new ResourceLocation(json.getAsJsonPrimitive("language").getAsString()));
            Vector2i startPos = new Vector2i(json.getAsJsonPrimitive("start_x").getAsInt(), json.getAsJsonPrimitive("start_y").getAsInt());
            
            // Because JSON doesn't do maps with anything but string keys, the X and Y coordinates of the node are packed into
            // the same JSON object as the node definition itself.  These JSON objects are then contained in a single JSON list.
            Map<Vector2ic, GridNodeDefinition> nodes = new HashMap<>();
            Set<Vector2ic> posSet = new HashSet<>();
            JsonArray nodesJson = json.getAsJsonArray("nodes");
            for (JsonElement nodeElement : nodesJson) {
                try {
                    JsonObject nodeObj = nodeElement.getAsJsonObject();
                    Vector2i nodePos = new Vector2i(nodeObj.getAsJsonPrimitive("x").getAsInt(), nodeObj.getAsJsonPrimitive("y").getAsInt());
                    if (posSet.contains(nodePos)) {
                        throw new IllegalArgumentException("Duplicate node position (" + nodePos.x() + "," + nodePos.y() + ") detected");
                    } else {
                        GridNodeDefinition nodeDef = GridNodeDefinition.SERIALIZER.read(gridId, nodeObj);
                        nodes.put(nodePos, nodeDef);
                        posSet.add(nodePos);
                    }
                } catch (Exception e) {
                    throw new JsonSyntaxException("Invalid node definition in grid definition JSON for " + gridId.toString(), e);
                }
            }
            
            return new GridDefinition(key, langKey, startPos, nodes);
        }

        @Override
        public GridDefinition fromNetwork(FriendlyByteBuf buf) {
            ResourceLocation key = buf.readResourceLocation();
            ResourceKey<BookLanguage> language = buf.readResourceKey(RegistryKeysPM.BOOK_LANGUAGES);
            Vector2i startPos = new Vector2i(buf.readVarInt(), buf.readVarInt());
            
            Map<Vector2ic, GridNodeDefinition> nodes = new HashMap<>();
            int nodeCount = buf.readVarInt();
            for (int index = 0; index < nodeCount; index++) {
                nodes.put(new Vector2i(buf.readVarInt(), buf.readVarInt()), GridNodeDefinition.SERIALIZER.fromNetwork(buf));
            }
            
            return new GridDefinition(key, language, startPos, nodes);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GridDefinition gridDef) {
            buf.writeResourceLocation(gridDef.key);
            buf.writeResourceKey(gridDef.language);
            buf.writeVarInt(gridDef.startPos.x());
            buf.writeVarInt(gridDef.startPos.y());
            buf.writeVarInt(gridDef.nodes.size());
            for (Map.Entry<Vector2ic, GridNodeDefinition> entry : gridDef.nodes.entrySet()) {
                buf.writeVarInt(entry.getKey().x());
                buf.writeVarInt(entry.getKey().y());
                GridNodeDefinition.SERIALIZER.toNetwork(buf, entry.getValue());
            }
        }
    }
}
